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
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Decorator;
import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.inject.spi.Interceptor;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.asset.StringAsset;
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
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class SingleModuleProcessingTest extends AbstractJSR299Test {

    private static final String BEANS_XML_TEST = "/beans.xml.test";

    @Deployment
    public static WebArchive createTestArchive() {

        return new WebArchiveBuilder()
                .withTestClass(SingleModuleProcessingTest.class)
                .withClasses(Animal.class, Binding.class, Decorator1.class, Decorator2.class, Decorator3.class,
                        Interceptor1.class, Interceptor2.class, Interceptor3.class, Lion.class,
                        SingleModuleProcessingExtension.class, ModuleProcessingExtension.class, Tiger.class)
                .withBeansXml(getBeansDescriptor()).withExtension(SingleModuleProcessingExtension.class).build()
                .addAsResource(new StringAsset(getBeansDescriptor().exportAsString()), BEANS_XML_TEST)
                .addPackages(true, IOUtils.class.getPackage());
    }

    private static BeansDescriptor getBeansDescriptor() {
        return Descriptors.create(BeansDescriptor.class).createAlternatives().clazz(Lion.class.getName()).up()
                .createDecorators().clazz(Decorator1.class.getName(), Decorator2.class.getName()).up().createInterceptors()
                .clazz(Interceptor1.class.getName(), Interceptor2.class.getName()).up();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "11.5.5", id = "a")
    public void testProcessedModulesCount(SingleModuleProcessingExtension moduleProcessingExtension) {
        // Single module: WEB-INF/classes
        assertEquals(moduleProcessingExtension.getModules().size(), 1);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "11.5.5", id = "bd") })
    public void testAnnotatedTypes(SingleModuleProcessingExtension moduleProcessingExtension) {
        Set<AnnotatedType<?>> types = moduleProcessingExtension.getFirstModule().getAnnotatedTypes();
        assertContainsAll(types, Animal.class, Decorator1.class, Decorator2.class, Decorator3.class, Interceptor1.class,
                Interceptor2.class, Interceptor3.class, Lion.class, SingleModuleProcessingExtension.class,
                ModuleProcessingExtension.class, Tiger.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "11.5.5", id = "ba"), @SpecAssertion(section = "11.5.5", id = "ca"),
            @SpecAssertion(section = "11.5.5", id = "cb") })
    public void testAlternatives(SingleModuleProcessingExtension moduleProcessingExtension) {

        Set<Class<?>> enabledAlternatives = moduleProcessingExtension.getFirstModule().getAlternatives();
        assertEquals(enabledAlternatives.size(), 1);
        assertTrue(enabledAlternatives.contains(Lion.class));

        Bean<?> bean = getCurrentManager().resolve(getCurrentManager().getBeans(Animal.class));
        assertEquals(bean.getBeanClass(), Tiger.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "11.5.5", id = "bc"), @SpecAssertion(section = "11.5.5", id = "ce"),
            @SpecAssertion(section = "11.5.5", id = "cf") })
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

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "11.5.5", id = "bb"), @SpecAssertion(section = "11.5.5", id = "cc"),
            @SpecAssertion(section = "11.5.5", id = "cd") })
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

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "11.5.5", id = "be") })
    public void testBeansXml(SingleModuleProcessingExtension moduleProcessingExtension) throws Exception {
        assertEquals(moduleProcessingExtension.getFirstModule().getBeansXml(),
                IOUtils.toString(this.getClass().getResourceAsStream(BEANS_XML_TEST)));
    }

    private void assertContainsAll(Collection<AnnotatedType<?>> annotatedTypes, Class<?>... types) {
        Set<Class<?>> typeSet = new HashSet<Class<?>>(Arrays.asList(types));
        for (AnnotatedType<?> item : annotatedTypes) {
            typeSet.remove(item.getJavaClass());
        }
        if (!typeSet.isEmpty()) {
            throw new IllegalStateException("The following types are not contained: " + typeSet);
        }
    }
}
