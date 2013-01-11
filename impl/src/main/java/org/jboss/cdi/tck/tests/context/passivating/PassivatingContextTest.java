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
package org.jboss.cdi.tck.tests.context.passivating;

import static org.jboss.cdi.tck.cdi.Sections.PASSIVATING_SCOPE;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATING_SCOPES;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_DEPENDENCY;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_VALIDATION;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.IllegalProductException;
import javax.enterprise.inject.spi.Bean;

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
 * @author Nicklas Karlsson
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class PassivatingContextTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(PassivatingContextTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PASSIVATION_CAPABLE, id = "ba"), @SpecAssertion(section = PASSIVATING_SCOPES, id = "a") })
    public void testManagedBeanWithSerializableImplementationClassOK() {
        Set<Bean<Jyvaskyla>> beans = getBeans(Jyvaskyla.class);
        assert !beans.isEmpty();
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE, id = "bb")
    public void testManagedBeanWithSerializableInterceptorClassOK() {
        Set<Bean<Kokkola>> beans = getBeans(Kokkola.class);
        assert !beans.isEmpty();
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE, id = "bc")
    public void testManagedBeanWithSerializableDecoratorOK() {
        Set<Bean<City>> beans = getBeans(City.class);
        assert !beans.isEmpty();
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE, id = "ca")
    public void testPassivationCapableProducerMethodIsOK() {
        Set<Bean<Record>> beans = getBeans(Record.class);
        assert !beans.isEmpty();
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE, id = "da")
    public void testPassivationCapableProducerFieldIsOK() {
        Set<Bean<Wheat>> beans = getBeans(Wheat.class);
        assert !beans.isEmpty();
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY, id = "c")
    public void testInjectionOfDependentPrimitiveProductIntoNormalBean() {
        getInstanceByType(NumberConsumer.class).ping();
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY, id = "c")
    public void testInjectionOfDependentSerializableProductIntoNormalBean() {
        getInstanceByType(SerializableCityConsumer.class).ping();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PASSIVATING_SCOPE, id = "a") })
    public void testPassivationOccurs() throws IOException, ClassNotFoundException {
        Kajaani instance = getInstanceByType(Kajaani.class);
        instance.setTheNumber(100);
        Context sessionContext = getCurrentManager().getContext(SessionScoped.class);
        setContextInactive(sessionContext);
        setContextActive(sessionContext);
        instance = getInstanceByType(Kajaani.class);
        assert instance.getTheNumber() == 100;
    }

    @Test
    @SpecAssertion(section = PASSIVATION_VALIDATION, id = "aa")
    public void testBeanWithNonSerializableImplementationInjectedIntoTransientFieldOK() {
        Set<Bean<Joensuu>> beans = getBeans(Joensuu.class);
        assert !beans.isEmpty();
    }

    @Test(expectedExceptions = IllegalProductException.class)
    @SpecAssertion(section = PASSIVATION_VALIDATION, id = "ea")
    public void testPassivatingScopeProducerMethodReturnsUnserializableObjectNotOk() {
        getInstanceByType(Television.class).turnOn();
    }

    @Test(expectedExceptions = IllegalProductException.class)
    @SpecAssertion(section = PASSIVATION_VALIDATION, id = "eb")
    public void testNonSerializableProducerFieldDeclaredPassivatingThrowsIllegalProductException() {
        getInstanceByType(HelsinkiNonSerializable.class).ping();
    }

    public static boolean isSerializable(Class<?> clazz) {
        return clazz.isPrimitive() || Serializable.class.isAssignableFrom(clazz);
    }

}
