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
package org.jboss.cdi.tck.tests.extensions.modules;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.PM;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.extensions.modules.ModuleProcessingExtension.ProcessModuleHolder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class MultiModuleProcessingTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(MultiModuleProcessingTest.class)
                .withClasses(Lion.class)
                .withBeanLibrary(
                        Descriptors.create(BeansDescriptor.class).createAlternatives().clazz(Tiger.class.getName()).up()
                                .createDecorators().clazz(Decorator1.class.getName()).up().createInterceptors()
                                .clazz(Interceptor1.class.getName()).up(), Animal.class, Decorator1.class, Interceptor1.class,
                        Tiger.class, Binding.class, ModuleProcessingExtension.class)
                .withLibrary(Elephant.class, ElephantExtension.class).build().addPackages(true, IOUtils.class.getPackage());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = PM, id = "a")
    public void testProcessedModulesCount(ModuleProcessingExtension moduleProcessingExtension) {
        // WEB-INF/classes and bean library
        assertEquals(moduleProcessingExtension.getModules().size(), 2);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = PM, id = "cg")
    public void test(ModuleProcessingExtension moduleProcessingExtension) {

        List<ProcessModuleHolder> modules = new ArrayList<ProcessModuleHolder>(moduleProcessingExtension.getModules());
        for (Iterator<ProcessModuleHolder> i = modules.iterator(); i.hasNext();) {

            ProcessModuleHolder module = i.next();

            if (module.getClasses().contains(Tiger.class)) {
                // Bean library
                assertEquals(module.getAlternatives().size(), 1);
                assertEquals(module.getAlternatives().iterator().next(), Tiger.class);
                assertEquals(module.getDecorators().size(), 1);
                assertEquals(module.getDecorators().get(0), Decorator1.class);
                assertEquals(module.getInterceptors().size(), 1);
                assertEquals(module.getInterceptors().get(0), Interceptor1.class);
                assertTrue(module.getClasses().contains(Animal.class));
                assertTrue(module.getClasses().contains(Decorator1.class));
                assertTrue(module.getClasses().contains(Interceptor1.class));
                assertTrue(module.getClasses().contains(Tiger.class));
                assertTrue(module.getClasses().contains(ModuleProcessingExtension.class));
                assertFalse(module.getClasses().contains(Elephant.class));
                assertFalse(module.getClasses().contains(ElephantExtension.class));
                assertFalse(module.getClasses().contains(Lion.class));
                i.remove();
            } else if (module.getClasses().contains(Lion.class)) {
                // WEB-INF/classes
                assertTrue(module.getAlternatives().isEmpty());
                assertTrue(module.getInterceptors().isEmpty());
                assertTrue(module.getDecorators().isEmpty());
                assertFalse(module.getClasses().contains(Tiger.class));
                assertFalse(module.getClasses().contains(ModuleProcessingExtension.class));
                assertFalse(module.getClasses().contains(ElephantExtension.class));
                i.remove();
            }
        }
        assertEquals(modules.size(), 0);
    }
}
