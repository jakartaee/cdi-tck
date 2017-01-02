/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.cdi.tck.tests.extensions.afterBeanDiscovery;

import static org.jboss.cdi.tck.cdi.Sections.AFTER_BEAN_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.INJECTION_POINT;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_BEAN;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.context.spi.Context;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class AfterBeanDiscoveryTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AfterBeanDiscoveryTest.class)
                .withExtension(AfterBeanDiscoveryObserver.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = AFTER_BEAN_DISCOVERY, id = "db") })
    public void testBeanIsAdded() {
        assertEquals(1, getBeans(Cockatoo.class).size());
        assertEquals("Billy", getContextualReference(Cockatoo.class).getName());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = INJECTION_POINT, id = "ea") })
    public void testCustomDependentBeanInjectionPointIsAvailable(Cage cage) {
        assertNotNull(cage);
        assertNotNull(cage.getCockatoo());
        assertNotNull(cage.getCockatoo().getInjectionPoint());
        assertEquals(cage.getCockatoo().getInjectionPoint().getBean().getBeanClass(), Cage.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = AFTER_BEAN_DISCOVERY, id = "da"), @SpecAssertion(section = PROCESS_BEAN, id = "eca") })
    public void testProcessBeanIsFired() {
        assertEquals(getCurrentManager().getExtension(AfterBeanDiscoveryObserver.class).getCockatooPBObservedCount().get(), 1);
        assertEquals(getCurrentManager().getExtension(AfterBeanDiscoveryObserver.class).getCockatooPSBObservedCount().get(), 1);
    }

    @Test
    @SpecAssertion(section = AFTER_BEAN_DISCOVERY, id = "ea")
    public void testProcessObserverMethodFiredWhileAddingObserverMethod() {
        assertEquals(getCurrentManager().getExtension(AfterBeanDiscoveryObserver.class).getTalkPOMObservedCount().get(), 1);
        assertEquals(getCurrentManager().getExtension(AfterBeanDiscoveryObserver.class).getTalkPSOMObservedCount().get(), 1);
    }

    @Test
    @SpecAssertion(section = AFTER_BEAN_DISCOVERY, id = "eb")
    public void testObserverMethodRegistered() {
        getCurrentManager().fireEvent(new Talk("Hello"));
        assertTrue(AfterBeanDiscoveryObserver.addedObserverMethod.isObserved());
    }

    @Test
    @SpecAssertion(section = AFTER_BEAN_DISCOVERY, id = "f")
    public void testAddContext() {
        Context context = getCurrentManager().getContext(SuperScoped.class);
        assertNotNull(context);
        assertTrue(context.isActive());
        assertTrue(context instanceof SuperContext);
    }

}
