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
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Decorator;
import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.inject.spi.Interceptor;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
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
@SpecVersion(spec = "cdi", version = "20091101")
public class SingleModuleProcessingTest extends AbstractTest {

    private static final String BEANS_XML_TEST = "/beans.xml.test";

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(SingleModuleProcessingTest.class)
                .withClasses(Animal.class, Binding.class, Decorator1.class, Decorator2.class, Decorator3.class,
                        Interceptor1.class, Interceptor2.class, Interceptor3.class, Lion.class,
                        SingleModuleProcessingExtension.class, ModuleProcessingExtension.class, Tiger.class)
                .withBeansXml(getBeansDescriptor()).withExtension(SingleModuleProcessingExtension.class)
                .withLibrary(ShrinkWrap.create(JavaArchive.class).addPackages(true, IOUtils.class.getPackage())).build()
                .addAsResource(new StringAsset(getBeansDescriptor().exportAsString()), BEANS_XML_TEST);
    }

    private static BeansDescriptor getBeansDescriptor() {
        return Descriptors.create(BeansDescriptor.class).createAlternatives().clazz(Lion.class.getName()).up()
                .createDecorators().clazz(Decorator1.class.getName(), Decorator2.class.getName()).up().createInterceptors()
                .clazz(Interceptor1.class.getName(), Interceptor2.class.getName()).up();
    }

    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = PM, id = "a")
    public void testProcessedModulesCount(SingleModuleProcessingExtension moduleProcessingExtension) {
        // Single module: WEB-INF/classes
        assertEquals(moduleProcessingExtension.getModules().size(), 1);
    }

    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = PM, id = "ba"), @SpecAssertion(section = PM, id = "ca"),
            @SpecAssertion(section = PM, id = "cb") })
    public void testAlternatives(SingleModuleProcessingExtension moduleProcessingExtension) {

        List<Class<?>> enabledAlternatives = moduleProcessingExtension.getFirstModule().getAlternatives();
        assertEquals(enabledAlternatives.size(), 1);
        assertTrue(enabledAlternatives.contains(Lion.class));

        Bean<?> bean = getCurrentManager().resolve(getCurrentManager().getBeans(Animal.class));
        assertEquals(bean.getBeanClass(), Tiger.class);
    }

    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = PM, id = "bc"), @SpecAssertion(section = PM, id = "ce"),
            @SpecAssertion(section = PM, id = "cf") })
    public void testDecorators(SingleModuleProcessingExtension moduleProcessingExtension) {

        List<Class<?>> enabledDecorators = moduleProcessingExtension.getFirstModule().getDecorators();
        assertEquals(enabledDecorators.size(), 2);
        assertEquals(enabledDecorators.get(0), Decorator1.class);
        assertEquals(enabledDecorators.get(1), Decorator2.class);

        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(Collections.<Type> singleton(Animal.class));
        assertEquals(decorators.size(), 2);
        assertEquals(decorators.get(0).getBeanClass(), Decorator1.class);
        assertEquals(decorators.get(1).getBeanClass(), Decorator3.class);
    }

    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = PM, id = "bb"), @SpecAssertion(section = PM, id = "cc"),
            @SpecAssertion(section = PM, id = "cd") })
    public void testInterceptors(SingleModuleProcessingExtension moduleProcessingExtension) {

        List<Class<?>> enabledInterceptors = moduleProcessingExtension.getFirstModule().getInterceptors();
        assertEquals(enabledInterceptors.size(), 2);
        assertEquals(enabledInterceptors.get(0), Interceptor1.class);
        assertEquals(enabledInterceptors.get(1), Interceptor2.class);

        List<Interceptor<?>> interceptors = getCurrentManager().resolveInterceptors(InterceptionType.AROUND_INVOKE,
                new Binding.Literal());
        assertEquals(interceptors.size(), 2);
        assertEquals(interceptors.get(0).getBeanClass(), Interceptor3.class);
        assertEquals(interceptors.get(1).getBeanClass(), Interceptor1.class);
    }

    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = PM, id = "be") })
    public void testBeansXml(SingleModuleProcessingExtension moduleProcessingExtension) throws Exception {
        assertEquals(moduleProcessingExtension.getFirstModule().getBeansXml(),
                IOUtils.toString(this.getClass().getResourceAsStream(BEANS_XML_TEST)));
    }

}
