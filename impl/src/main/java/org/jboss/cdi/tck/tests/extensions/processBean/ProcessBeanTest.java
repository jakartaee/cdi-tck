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

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.PB;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.ProcessBeanAttributes;
import javax.enterprise.inject.spi.ProcessManagedBean;
import javax.enterprise.inject.spi.ProcessProducerField;
import javax.enterprise.inject.spi.ProcessProducerMethod;
import java.util.Arrays;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
@Test(groups = INTEGRATION)
public class ProcessBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(ProcessBeanTest.class)
                .withClasses(Cat.class, Cow.class, Cowshed.class, Domestic.class, Chicken.class, ChickenHutch.class,
                        ProcessBeanObserver.class, CatInterceptor.class, CatInterceptorBinding.class, Animal.class, AnimalDecorator.class).
                        withBeansXml(Descriptors.create(BeansDescriptor.class).createInterceptors().clazz(CatInterceptor.class.getName())
                                .up().createDecorators().clazz(AnimalDecorator.class.getName()).up()).
                        withExtension(ProcessBeanObserver.class).build();
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = PB, id = "ba"), @SpecAssertion(section = PB, id = "bb"),
            @SpecAssertion(section = PB, id = "eda"), @SpecAssertion(section = PB, id = "efa"),
            @SpecAssertion(section = PB, id = "fa"), @SpecAssertion(section = PB, id = "l"),
            @SpecAssertion(section = BEAN_DISCOVERY, id = "fa") })
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

    @SpecAssertions({ @SpecAssertion(section = PB, id = "eaa"), @SpecAssertion(section = PB, id = "eab"),
            @SpecAssertion(section = PB, id = "edc"), @SpecAssertion(section = PB, id = "efc"),
            @SpecAssertion(section = PB, id = "fc"), @SpecAssertion(section = PB, id = "i"),
            @SpecAssertion(section = PB, id = "j"), @SpecAssertion(section = BEAN_DISCOVERY, id = "ha") })
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

    @SpecAssertions({ @SpecAssertion(section = PB, id = "eba"), @SpecAssertion(section = PB, id = "ebb"),
            @SpecAssertion(section = PB, id = "edd"), @SpecAssertion(section = PB, id = "efd"),
            @SpecAssertion(section = PB, id = "fd"), @SpecAssertion(section = PB, id = "n"),
            @SpecAssertion(section = BEAN_DISCOVERY, id = "hb") })
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

    @SpecAssertions({ @SpecAssertion(section = PB, id = "aa") })
    public void testProcessBeanFiredForInterceptor() {
        assertNotNull(ProcessBeanObserver.getInterceptor());
    }

    @SpecAssertions({ @SpecAssertion(section = PB, id = "aa") })
    public void testProcessBeanFiredForDecorator() {
        assertNotNull(ProcessBeanObserver.getDecorator());
    }

}
