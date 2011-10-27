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
package org.jboss.jsr299.tck.tests.context.passivating;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.IllegalProductException;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
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
public class PassivatingContextTest extends AbstractJSR299Test {

    @Deployment
    public static EnterpriseArchive createTestArchive() {
        return new EnterpriseArchiveBuilder().withTestClassPackage(PassivatingContextTest.class).withBeansXml("beans.xml")
                .build();
    }

    @Test(groups = { "contexts", "passivation", "rewrite" })
    @SpecAssertions({ @SpecAssertion(section = "6.6.1", id = "ba"), @SpecAssertion(section = "6.6.3", id = "a") })
    public void testManagedBeanWithSerializableImplementationClassOK() {
        Set<Bean<Jyvaskyla>> beans = getBeans(Jyvaskyla.class);
        assert !beans.isEmpty();
    }

    @Test(groups = { "contexts", "passivation" })
    @SpecAssertion(section = "6.6.1", id = "bb")
    public void testManagedBeanWithSerializableInterceptorClassOK() {
        Set<Bean<Kokkola>> beans = getBeans(Kokkola.class);
        assert !beans.isEmpty();
    }

    @Test(groups = { "contexts", "passivation" })
    @SpecAssertion(section = "6.6.1", id = "bc")
    public void testManagedBeanWithSerializableDecoratorOK() {
        Set<Bean<City>> beans = getBeans(City.class);
        assert !beans.isEmpty();
    }

    @Test(groups = { "contexts", "passivation" })
    @SpecAssertion(section = "6.6.1", id = "ca")
    public void testPassivationCapableProducerMethodIsOK() {
        Set<Bean<Record>> beans = getBeans(Record.class);
        assert !beans.isEmpty();
    }

    @Test(groups = { "contexts", "passivation" })
    @SpecAssertion(section = "6.6.1", id = "da")
    public void testPassivationCapableProducerFieldIsOK() {
        Set<Bean<Wheat>> beans = getBeans(Wheat.class);
        assert !beans.isEmpty();
    }

    @Test
    @SpecAssertion(section = "6.6.2", id = "c")
    public void testInjectionOfDependentPrimitiveProductIntoNormalBean() {
        getInstanceByType(NumberConsumer.class).ping();
    }

    @Test
    @SpecAssertion(section = "6.6.2", id = "c")
    public void testInjectionOfDependentSerializableProductIntoNormalBean() {
        getInstanceByType(SerializableCityConsumer.class).ping();
    }

    @Test(groups = { "contexts", "passivation" })
    @SpecAssertions({ @SpecAssertion(section = "6.6", id = "a") })
    public void testPassivationOccurs() throws IOException, ClassNotFoundException {
        Kajaani instance = getInstanceByType(Kajaani.class);
        instance.setTheNumber(100);
        Context sessionContext = getCurrentManager().getContext(SessionScoped.class);
        setContextInactive(sessionContext);
        setContextActive(sessionContext);
        instance = getInstanceByType(Kajaani.class);
        assert instance.getTheNumber() == 100;
    }

    @Test(groups = { "contexts", "passivation" })
    @SpecAssertion(section = "6.6.4", id = "aaba")
    public void testBeanWithNonSerializableImplementationInjectedIntoTransientFieldOK() {
        Set<Bean<Joensuu>> beans = getBeans(Joensuu.class);
        assert !beans.isEmpty();
    }

    @Test(expectedExceptions = IllegalProductException.class)
    @SpecAssertion(section = "6.6.4", id = "ea")
    public void testPassivatingScopeProducerMethodReturnsUnserializableObjectNotOk() {
        getInstanceByType(Television.class).turnOn();
    }

    @Test(expectedExceptions = IllegalProductException.class)
    @SpecAssertion(section = "6.6.4", id = "eb")
    public void testNonSerializableProducerFieldDeclaredPassivatingThrowsIllegalProductException() {
        getInstanceByType(HelsinkiNonSerializable.class).ping();
    }

    public static boolean isSerializable(Class<?> clazz) {
        return clazz.isPrimitive() || Serializable.class.isAssignableFrom(clazz);
    }

}
