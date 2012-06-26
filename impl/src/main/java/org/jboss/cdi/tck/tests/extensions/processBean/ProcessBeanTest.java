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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.ProcessManagedBean;
import javax.enterprise.inject.spi.ProcessProducerField;
import javax.enterprise.inject.spi.ProcessProducerMethod;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class ProcessBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(ProcessBeanTest.class)
                .withClasses(Cat.class, Cow.class, Cowshed.class, Domestic.class, Chicken.class, ChickenHutch.class,
                        ProcessBeanObserver.class).withExtension(ProcessBeanObserver.class).build();
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.11", id = "ba"), @SpecAssertion(section = "11.5.11", id = "eda"),
            @SpecAssertion(section = "11.5.11", id = "efa"), @SpecAssertion(section = "11.5.11", id = "fa"),
            @SpecAssertion(section = "11.5.11", id = "l"), @SpecAssertion(section = "12.4", id = "fa") })
    public void testProcessBeanEvent() {

        ProcessManagedBean<Cat> event = ProcessBeanObserver.getCatProcessManagedBean();

        assertNotNull(event.getBean());
        assertEquals(event.getBean().getBeanClass(), Cat.class);
        assertTrue(annotationSetMatches(event.getBean().getQualifiers(), Domestic.class, Any.class));
        assertEquals(event.getAnnotatedBeanClass().getBaseType(), Cat.class);
        assertEquals(ProcessBeanObserver.getCatProcessBeanCount(), 2);
        assertTrue(ProcessBeanObserver.getCatProcessManagedBean().getAnnotated() instanceof AnnotatedType<?>);
    }

    @SpecAssertions({ @SpecAssertion(section = "11.5.11", id = "eaa"), @SpecAssertion(section = "11.5.11", id = "edc"),
            @SpecAssertion(section = "11.5.11", id = "efc"), @SpecAssertion(section = "11.5.11", id = "fc"),
            @SpecAssertion(section = "11.5.11", id = "i"), @SpecAssertion(section = "11.5.11", id = "j"),
            @SpecAssertion(section = "12.4", id = "ha") })
    @Test
    public void testProcessProducerMethodEvent() {

        ProcessProducerMethod<Cow, Cowshed> event = ProcessBeanObserver.getCowProcessProducerMethod();

        assertTrue(event.getBean().getTypes().contains(Cow.class));
        assertEquals(event.getBean().getBeanClass(), Cowshed.class);
        assertEquals(event.getAnnotatedProducerMethod().getBaseType(), Cow.class);
        assertEquals(event.getAnnotatedProducerMethod().getDeclaringType().getBaseType(), Cowshed.class);

        // There are bugs in the API that mean generic type parameter ordering is wrong for ProcessProducerField and
        // ProcessProducerMethod
        // https://issues.jboss.org/browse/CDITCK-168
        // https://issues.jboss.org/browse/WELD-586
        assertEquals(ProcessBeanObserver.getCowShedProcessBeanCount(), 2);
        assertTrue(event.getAnnotated() instanceof AnnotatedMethod<?>);

        assertEquals(event.getAnnotatedProducerMethod().getJavaMember().getName(), "getDaisy");
        assertEquals(event.getAnnotatedProducerMethod().getJavaMember().getDeclaringClass(), Cowshed.class);

        AnnotatedParameter<Cow> disposedParam = event.getAnnotatedDisposedParameter();
        assertNotNull(disposedParam);
        assertTrue(disposedParam.isAnnotationPresent(Disposes.class));
        assertEquals(disposedParam.getBaseType(), Cow.class);
        assertEquals(disposedParam.getDeclaringCallable().getJavaMember().getName(), "disposeOfDaisy");
        assertEquals(disposedParam.getDeclaringCallable().getJavaMember().getDeclaringClass(), Cowshed.class);
        assertEquals(disposedParam.getDeclaringCallable().getDeclaringType().getJavaClass(), Cowshed.class);
    }

    @SpecAssertions({ @SpecAssertion(section = "11.5.11", id = "eb"), @SpecAssertion(section = "11.5.11", id = "edd"),
            @SpecAssertion(section = "11.5.11", id = "efd"), @SpecAssertion(section = "11.5.11", id = "fd"),
            @SpecAssertion(section = "11.5.11", id = "n"), @SpecAssertion(section = "12.4", id = "hb") })
    @Test
    public void testProcessProducerFieldEvent() {

        ProcessProducerField<Chicken, ChickenHutch> event = ProcessBeanObserver.getChickenProcessProducerField();

        assertTrue(event.getBean().getTypes().contains(Chicken.class));
        assertEquals(event.getBean().getBeanClass(), ChickenHutch.class);
        assertEquals(event.getAnnotatedProducerField().getBaseType(), Chicken.class);
        assertEquals(event.getAnnotatedProducerField().getDeclaringType().getBaseType(), ChickenHutch.class);

        // There are bugs in the API that mean generic type parameter ordering is wrong for ProcessProducerField and
        // ProcessProducerMethod
        // https://issues.jboss.org/browse/CDITCK-168
        // https://issues.jboss.org/browse/WELD-586
        assertEquals(ProcessBeanObserver.getChickenHutchProcessBeanCount(), 2);
        assertTrue(event.getAnnotated() instanceof AnnotatedField<?>);

        assertEquals(event.getAnnotatedProducerField().getJavaMember().getName(), "chicken");
        assertEquals(event.getAnnotatedProducerField().getJavaMember().getDeclaringClass(), ChickenHutch.class);

        AnnotatedParameter<Chicken> disposedParam = event.getAnnotatedDisposedParameter();
        assertNotNull(disposedParam);
        assertTrue(disposedParam.isAnnotationPresent(Disposes.class));
        assertEquals(disposedParam.getBaseType(), Chicken.class);
        assertEquals(disposedParam.getDeclaringCallable().getJavaMember().getName(), "disposeOfRocky");
        assertEquals(disposedParam.getDeclaringCallable().getJavaMember().getDeclaringClass(), ChickenHutch.class);
        assertEquals(disposedParam.getDeclaringCallable().getDeclaringType().getJavaClass(), ChickenHutch.class);
    }

}
