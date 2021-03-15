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
package org.jboss.cdi.tck.tests.extensions.processBean;

import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY_STEPS;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_BEAN;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedParameter;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;
import jakarta.enterprise.inject.spi.ProcessManagedBean;
import jakarta.enterprise.inject.spi.ProcessProducerField;
import jakarta.enterprise.inject.spi.ProcessProducerMethod;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test
public class ProcessBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(ProcessBeanTest.class)
                .withClasses(Cat.class, Cow.class, Cowshed.class, Domestic.class, Chicken.class, ChickenHutch.class,
                        ProcessBeanObserver.class, CatInterceptor.class, CatInterceptorBinding.class, Animal.class, AnimalDecorator.class)
                .withBeansXml(new BeansXml().interceptors(CatInterceptor.class).decorators(AnimalDecorator.class))
                .withExtension(ProcessBeanObserver.class).build();
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN, id = "ba"), @SpecAssertion(section = PROCESS_BEAN, id = "bb"),
            @SpecAssertion(section = PROCESS_BEAN, id = "eda"), @SpecAssertion(section = PROCESS_BEAN, id = "efa"),
            @SpecAssertion(section = PROCESS_BEAN, id = "fa"), @SpecAssertion(section = PROCESS_BEAN, id = "l"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "h"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "i") })
    public void testProcessBeanEvent() {

        assertNotNull(ProcessBeanObserver.getCatBean());
        assertEquals(ProcessBeanObserver.getCatBean().getBeanClass(), Cat.class);
        assertTrue(annotationSetMatches(ProcessBeanObserver.getCatBean().getQualifiers(), Domestic.class, Any.class));
        assertEquals(ProcessBeanObserver.getCatAnnotatedType().getBaseType(), Cat.class);
        assertEquals(ProcessBeanObserver.getCatProcessBeanCount(), 2);
        assertTrue(ProcessBeanObserver.getCatAnnotated() instanceof AnnotatedType<?>);

        assertEquals(ProcessBeanObserver.getCatActionSeq().getData(),
                Arrays.asList(ProcessBeanAttributes.class.getName(), ProcessManagedBean.class.getName()));
    }

    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN, id = "eaa"), @SpecAssertion(section = PROCESS_BEAN, id = "eab"),
            @SpecAssertion(section = PROCESS_BEAN, id = "edc"), @SpecAssertion(section = PROCESS_BEAN, id = "efc"),
            @SpecAssertion(section = PROCESS_BEAN, id = "fc"), @SpecAssertion(section = PROCESS_BEAN, id = "i"),
            @SpecAssertion(section = PROCESS_BEAN, id = "j"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "jb"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "jd") })
    @Test
    public void testProcessProducerMethodEvent() {

        assertTrue(ProcessBeanObserver.getCowBean().getTypes().contains(Cow.class));
        assertEquals(ProcessBeanObserver.getCowBean().getBeanClass(), Cowshed.class);
        assertEquals(ProcessBeanObserver.getCowMethod().getBaseType(), Cow.class);
        assertEquals(ProcessBeanObserver.getCowMethod().getDeclaringType().getBaseType(), Cowshed.class);

        // There are bugs in the API that mean generic type parameter ordering is wrong for ProcessProducerField and
        // ProcessProducerMethod
        // https://issues.jboss.org/browse/CDITCK-168
        // https://issues.jboss.org/browse/WELD-586
        assertEquals(ProcessBeanObserver.getCowShedProcessBeanCount(), 2);
        assertTrue(ProcessBeanObserver.getCowAnnotated() instanceof AnnotatedMethod<?>);

        assertEquals(ProcessBeanObserver.getCowMethod().getJavaMember().getName(), "getDaisy");
        assertEquals(ProcessBeanObserver.getCowMethod().getJavaMember().getDeclaringClass(), Cowshed.class);

        AnnotatedParameter<Cow> disposedParam = ProcessBeanObserver.getCowParameter();
        assertNotNull(disposedParam);
        assertTrue(disposedParam.isAnnotationPresent(Disposes.class));
        assertEquals(disposedParam.getBaseType(), Cow.class);
        assertEquals(disposedParam.getDeclaringCallable().getJavaMember().getName(), "disposeOfDaisy");
        assertEquals(disposedParam.getDeclaringCallable().getJavaMember().getDeclaringClass(), Cowshed.class);
        assertEquals(disposedParam.getDeclaringCallable().getDeclaringType().getJavaClass(), Cowshed.class);

        assertEquals(ProcessBeanObserver.getCowActionSeq().getData(),
                Arrays.asList(ProcessBeanAttributes.class.getName(), ProcessProducerMethod.class.getName()));
    }

    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN, id = "eba"), @SpecAssertion(section = PROCESS_BEAN, id = "ebb"),
            @SpecAssertion(section = PROCESS_BEAN, id = "edd"), @SpecAssertion(section = PROCESS_BEAN, id = "efd"),
            @SpecAssertion(section = PROCESS_BEAN, id = "fd"), @SpecAssertion(section = PROCESS_BEAN, id = "n"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "jb"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "jd") })
    @Test
    public void testProcessProducerFieldEvent() {

        assertTrue(ProcessBeanObserver.getChickenBean().getTypes().contains(Chicken.class));
        assertEquals(ProcessBeanObserver.getChickenBean().getBeanClass(), ChickenHutch.class);
        assertEquals(ProcessBeanObserver.getChickenField().getBaseType(), Chicken.class);
        assertEquals(ProcessBeanObserver.getChickenField().getDeclaringType().getBaseType(), ChickenHutch.class);

        // There are bugs in the API that mean generic type parameter ordering is wrong for ProcessProducerField and
        // ProcessProducerMethod
        // https://issues.jboss.org/browse/CDITCK-168
        // https://issues.jboss.org/browse/WELD-586
        assertEquals(ProcessBeanObserver.getChickenHutchProcessBeanCount(), 2);
        assertTrue(ProcessBeanObserver.getChickedAnnotated() instanceof AnnotatedField<?>);

        assertEquals(ProcessBeanObserver.getChickenField().getJavaMember().getName(), "chicken");
        assertEquals(ProcessBeanObserver.getChickenField().getJavaMember().getDeclaringClass(), ChickenHutch.class);

        AnnotatedParameter<Chicken> disposedParam = ProcessBeanObserver.getChickenParameter();
        assertNotNull(disposedParam);
        assertTrue(disposedParam.isAnnotationPresent(Disposes.class));
        assertEquals(disposedParam.getBaseType(), Chicken.class);
        assertEquals(disposedParam.getDeclaringCallable().getJavaMember().getName(), "disposeOfRocky");
        assertEquals(disposedParam.getDeclaringCallable().getJavaMember().getDeclaringClass(), ChickenHutch.class);
        assertEquals(disposedParam.getDeclaringCallable().getDeclaringType().getJavaClass(), ChickenHutch.class);

        assertEquals(ProcessBeanObserver.getChickenActionSeq().getData(),
                Arrays.asList(ProcessBeanAttributes.class.getName(), ProcessProducerField.class.getName()));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN, id = "aa"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "i") })
    public void testProcessBeanFiredForInterceptor() {
        assertNotNull(ProcessBeanObserver.getInterceptor());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN, id = "aa"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "i") })
    public void testProcessBeanFiredForDecorator() {
        assertNotNull(ProcessBeanObserver.getDecorator());
    }

}
