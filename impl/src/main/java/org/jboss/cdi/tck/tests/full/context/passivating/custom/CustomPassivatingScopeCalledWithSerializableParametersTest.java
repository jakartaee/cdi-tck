/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.context.passivating.custom;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.CONTEXT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.Instance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of the Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class CustomPassivatingScopeCalledWithSerializableParametersTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(CustomPassivatingScopeCalledWithSerializableParametersTest.class)
                .withExtensions(ClusteringExtension.class, BarExtension.class).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = CONTEXT, id = "s")
    public void testWithImplicitBean(Instance<Foo> foo, ClusteringExtension extension) {
        ClusterContext ctx = extension.getContext();
        ctx.reset();
        Foo instance = foo.get();
        assertNotNull(instance);
        assertEquals(instance.ping(), "pong");
        assertTrue(ctx.isGetCalled());
        // There are more assertions in the ClusterContext.get() method
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = CONTEXT, id = "s")
    public void testWithExtensionProvidedBean(Instance<Bar> bar, ClusteringExtension extension) {
        ClusterContext ctx = extension.getContext();
        ctx.reset();
        Bar instance = bar.get();
        assertEquals(instance.ping(), "pong");
        assertTrue(ctx.isGetCalled());
        // There are more assertions the ClusterContext.get() method
    }
}
