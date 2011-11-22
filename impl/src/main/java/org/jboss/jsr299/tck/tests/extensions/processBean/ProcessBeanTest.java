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
package org.jboss.jsr299.tck.tests.extensions.processBean;

import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Producer extension tests.
 * 
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class ProcessBeanTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProcessBeanTest.class)
                .withExtension("javax.enterprise.inject.spi.Extension").build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.11", id = "ba"), @SpecAssertion(section = "11.5.11", id = "eda"),
            @SpecAssertion(section = "11.5.11", id = "efa"), @SpecAssertion(section = "11.5.11", id = "fa"),
            @SpecAssertion(section = "11.5.11", id = "l"), @SpecAssertion(section = "12.4", id = "fa") })
    public void testProcessBeanEvent() {
        assert ProcessBeanObserver.getCatProcessManagedBean().getBean().getBeanClass().equals(Cat.class);
        assert ProcessBeanObserver.getCatProcessBeanCount() == 2;
        assert ProcessBeanObserver.getCatProcessManagedBean().getAnnotated() instanceof AnnotatedType<?>;
        assert ProcessBeanObserver.getCatProcessManagedBean().getAnnotatedBeanClass().getBaseType().equals(Cat.class);
    }

    @SpecAssertions({ @SpecAssertion(section = "11.5.11", id = "eaa"), @SpecAssertion(section = "11.5.11", id = "edc"),
            @SpecAssertion(section = "11.5.11", id = "efc"), @SpecAssertion(section = "11.5.11", id = "fc"),
            @SpecAssertion(section = "11.5.11", id = "i"), @SpecAssertion(section = "11.5.11", id = "j"),
            @SpecAssertion(section = "12.4", id = "ha") })
    @Test
    public void testProcessProducerMethodEvent() {
        assert ProcessBeanObserver.getCowProcessProducerMethod().getBean().getTypes().contains(Cow.class);
        assert ProcessBeanObserver.getCowProcessProducerMethod().getBean().getBeanClass().equals(Cowshed.class);
        assert ProcessBeanObserver.getCowProcessProducerMethod().getAnnotatedProducerMethod().getBaseType().equals(Cow.class);
        assert ProcessBeanObserver.getCowProcessProducerMethod().getAnnotatedProducerMethod().getDeclaringType().getBaseType()
                .equals(Cowshed.class);
        // There are bugs in the API that mean generic type parameter ordering is wrong for ProcessProducerField and
        // ProcessProducerMethod
        // https://issues.jboss.org/browse/CDITCK-168
        // https://issues.jboss.org/browse/WELD-586
        assert ProcessBeanObserver.getCowShedProcessBeanCount() == 2;
        assert ProcessBeanObserver.getCowProcessProducerMethod().getAnnotated() instanceof AnnotatedMethod<?>;
        assert ProcessBeanObserver.getCowProcessProducerMethod().getAnnotatedProducerMethod().getJavaMember().getName()
                .equals("getDaisy");
        assert ProcessBeanObserver.getCowProcessProducerMethod().getAnnotatedProducerMethod().getJavaMember()
                .getDeclaringClass().equals(Cowshed.class);
        assert ProcessBeanObserver.getCowProcessProducerMethod().getAnnotatedDisposedParameter().getDeclaringCallable()
                .getJavaMember().getName().equals("disposeOfDaisy");
        assert ProcessBeanObserver.getCowProcessProducerMethod().getAnnotatedDisposedParameter().getDeclaringCallable()
                .getJavaMember().getDeclaringClass().equals(Cowshed.class);
    }

    @SpecAssertions({ @SpecAssertion(section = "11.5.11", id = "eb"), @SpecAssertion(section = "11.5.11", id = "edd"),
            @SpecAssertion(section = "11.5.11", id = "efd"), @SpecAssertion(section = "11.5.11", id = "fd"),
            @SpecAssertion(section = "11.5.11", id = "n"), @SpecAssertion(section = "12.4", id = "hb") })
    @Test
    public void testProcessProducerFieldEvent() {
        assert ProcessBeanObserver.getChickenProcessProducerField().getBean().getTypes().contains(Chicken.class);
        assert ProcessBeanObserver.getChickenProcessProducerField().getBean().getBeanClass().equals(ChickenHutch.class);
        assert ProcessBeanObserver.getChickenProcessProducerField().getAnnotatedProducerField().getBaseType()
                .equals(Chicken.class);
        assert ProcessBeanObserver.getChickenProcessProducerField().getAnnotatedProducerField().getDeclaringType()
                .getBaseType().equals(ChickenHutch.class);
        // There are bugs in the API that mean generic type parameter ordering is wrong for ProcessProducerField and
        // ProcessProducerMethod
        // https://issues.jboss.org/browse/CDITCK-168
        // https://issues.jboss.org/browse/WELD-586
        assert ProcessBeanObserver.getChickenHutchProcessBeanCount() == 2;
        assert ProcessBeanObserver.getChickenProcessProducerField().getAnnotated() instanceof AnnotatedField<?>;
        assert ProcessBeanObserver.getChickenProcessProducerField().getAnnotatedProducerField().getJavaMember().getName()
                .equals("chicken");
        assert ProcessBeanObserver.getChickenProcessProducerField().getAnnotatedProducerField().getJavaMember()
                .getDeclaringClass().equals(ChickenHutch.class);
    }

}
