/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.jsr299.tck.tests.extensions.modules;

import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.jsr299.tck.tests.extensions.modules.ModuleProcessingExtension.ProcessModuleHolder;
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
public class MultiModuleProcessingTest extends AbstractJSR299Test {

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
    @SpecAssertion(section = "11.5.5", id = "a")
    public void testProcessedModulesCount(ModuleProcessingExtension moduleProcessingExtension) {
        // WEB-INF/classes and bean library
        assertEquals(moduleProcessingExtension.getModules().size(), 2);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "11.5.5", id = "cg")
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
