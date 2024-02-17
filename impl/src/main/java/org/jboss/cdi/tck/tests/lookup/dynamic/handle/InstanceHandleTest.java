/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.tests.lookup.dynamic.handle;

import static org.jboss.cdi.tck.cdi.Sections.DYNAMIC_LOOKUP;
import static org.jboss.cdi.tck.cdi.Sections.HANDLE_INTERFACE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.List;

@SpecVersion(spec = "cdi", version = "4.0")
public class InstanceHandleTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InstanceHandleTest.class).build();
    }

    @SpecAssertion(section = DYNAMIC_LOOKUP, id = "ma")
    @Test
    public void testIsResolvable() {
        Client client = getContextualReference(Client.class);
        ActionSequence.reset();
        assertNotNull(client);
        assertTrue(client.getAlphaInstance().isResolvable());
        assertFalse(client.getBigDecimalInstance().isResolvable());
    }

    @SpecAssertion(section = DYNAMIC_LOOKUP, id = "qa")
    @SpecAssertion(section = DYNAMIC_LOOKUP, id = "qa")
    @Test
    public void testGetHandle() {
        Client client = getContextualReference(Client.class);
        BeanManager beanManager = getCurrentManager();

        ActionSequence.reset();
        assertNotNull(client);

        Bean<?> alphaBean = beanManager.resolve(beanManager.getBeans(Alpha.class));
        Instance<Alpha> instance = client.getAlphaInstance();

        Instance.Handle<Alpha> alpha1 = instance.getHandle();
        assertEquals(alphaBean, alpha1.getBean());
        assertEquals(Dependent.class, alpha1.getBean().getScope());

        String alpha2Id;

        // Test try-with-resource
        try (Instance.Handle<Alpha> alpha2 = instance.getHandle()) {
            alpha2Id = alpha2.get().getId();
            assertFalse(alpha1.get().getId().equals(alpha2Id));
        }

        List<String> sequence = ActionSequence.getSequenceData();
        assertEquals(1, sequence.size());
        assertEquals(alpha2Id, sequence.get(0));

        alpha1.destroy();
        // Subsequent invocations are no-op
        alpha1.destroy();

        sequence = ActionSequence.getSequenceData();
        assertEquals(2, sequence.size());

        // Test normal scoped bean is also destroyed
        Instance<Bravo> bravoInstance = client.getInstance().select(Bravo.class);
        String bravoId = bravoInstance.get().getId();
        try (Instance.Handle<Bravo> bravo = bravoInstance.getHandle()) {
            assertEquals(bravoId, bravo.get().getId());
            ActionSequence.reset();
        }
        sequence = ActionSequence.getSequenceData();
        assertEquals(1, sequence.size());
        assertEquals(bravoId, sequence.get(0));
    }

    @SpecAssertion(section = DYNAMIC_LOOKUP, id = "qa")
    @SpecAssertion(section = HANDLE_INTERFACE, id = "ab", note = "get called after destroy")
    @Test
    public void testGetAfterDestroyingContextualInstance() {
        ActionSequence.reset();
        Client client = getContextualReference(Client.class);
        assertNotNull(client);

        Instance.Handle<Alpha> alphaHandle = client.getAlphaInstance().getHandle();
        // trigger bean creation
        alphaHandle.get();
        // trigger bean destruction
        alphaHandle.destroy();
        // verify that the destruction happened
        List<String> sequence = ActionSequence.getSequenceData();
        assertEquals(1, sequence.size());

        // try to invoke Handle.get() again; this should throw an exception
        try {
            alphaHandle.get();
            fail("Invoking Handle.get() after destroying contextual instance should throw an exception.");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @SpecAssertion(section = DYNAMIC_LOOKUP, id = "m", note = "when isAmbiguous is true")
    @SpecAssertion(section = DYNAMIC_LOOKUP, id = "ra", note = "handles() iteration")
    @Test
    public void testHandles() {
        Instance<Processor> instance = getCurrentManager().createInstance().select(Processor.class);
        ActionSequence.reset();
        assertTrue(instance.isAmbiguous());
        for (Instance.Handle<Processor> handle : instance.handles()) {
            handle.get().ping();
            if (handle.getBean().getScope().equals(Dependent.class)) {
                handle.destroy();
            }
        }
        assertEquals(3, ActionSequence.getSequenceSize());
        ActionSequence.assertSequenceDataContainsAll("firstPing", "secondPing", "firstDestroy");

        ActionSequence.reset();
        assertTrue(instance.isAmbiguous());
        for (Iterator<? extends Instance.Handle<Processor>> iterator = instance.handles().iterator(); iterator.hasNext(); ) {
            try (Instance.Handle<Processor> handle = iterator.next()) {
                handle.get().ping();
            }
        }
        assertEquals(4, ActionSequence.getSequenceSize());
        ActionSequence.assertSequenceDataContainsAll("firstPing", "secondPing", "firstDestroy", "secondDestroy");
    }
}
