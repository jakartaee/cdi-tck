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
package org.jboss.cdi.tck.tests.full.context.passivating;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATING_SCOPE;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATING_SCOPES;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_DEPENDENCY;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_INJECTION_POINTS;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_VALIDATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.io.IOException;
import java.util.Set;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.IllegalProductException;
import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Nicklas Karlsson
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class PassivatingContextTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(PassivatingContextTest.class).build();
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE, id = "ba")
    @SpecAssertion(section = PASSIVATING_SCOPES, id = "a")
    public void testManagedBeanWithSerializableImplementationClassOK() {
        Set<Bean<Jyvaskyla>> beans = getBeans(Jyvaskyla.class);
        assertFalse(beans.isEmpty());
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE, id = "bb")
    @SpecAssertion(section = PASSIVATING_SCOPE, id = "a")
    public void testManagedBeanWithSerializableInterceptorClassOK() throws ClassNotFoundException, IOException {
        Set<Bean<Kokkola>> beans = getBeans(Kokkola.class);
        assertFalse(beans.isEmpty());
        Bean<Kokkola> bean = beans.iterator().next();
        CreationalContext<Kokkola> ctx = getCurrentManager().createCreationalContext(bean);
        Kokkola instance = (Kokkola) getCurrentManager().getReference(bean, Kokkola.class, ctx);
        assertEquals(instance.ping(), 1);
        Kokkola instance2 = (Kokkola) activate(passivate(instance));
        assertEquals(instance2.ping(), 2);
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE, id = "bc")
    public void testManagedBeanWithSerializableDecoratorOK() {
        Set<Bean<City>> beans = getBeans(City.class);
        assertFalse(beans.isEmpty());
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE, id = "ca")
    public void testPassivationCapableProducerMethodIsOK() {
        Set<Bean<Record>> beans = getBeans(Record.class);
        assertFalse(beans.isEmpty());
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE, id = "da")
    public void testPassivationCapableProducerFieldIsOK() {
        Set<Bean<Wheat>> beans = getBeans(Wheat.class);
        assertFalse(beans.isEmpty());
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY, id = "c")
    public void testInjectionOfDependentPrimitiveProductIntoNormalBean() {
        getContextualReference(NumberConsumer.class).ping();
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY, id = "c")
    public void testInjectionOfDependentSerializableProductIntoNormalBean() {
        getContextualReference(SerializableCityConsumer.class).ping();
    }

    @Test
    @SpecAssertion(section = PASSIVATING_SCOPE, id = "a")
    public void testPassivationOccurs() throws IOException, ClassNotFoundException {
        Kajaani instance = getContextualReference(Kajaani.class);
        instance.setTheNumber(100);
        Context sessionContext = getCurrentManager().getContext(SessionScoped.class);
        setContextInactive(sessionContext);
        setContextActive(sessionContext);
        instance = getContextualReference(Kajaani.class);
        assertEquals(instance.getTheNumber(), 100);
    }

    @Test
    @SpecAssertion(section = PASSIVATION_VALIDATION, id = "ab")
    @SpecAssertion(section = PASSIVATION_CAPABLE_INJECTION_POINTS, id = "a")
    public void testBeanWithNonSerializableImplementationInjectedIntoTransientFieldOK() {
        Set<Bean<Joensuu>> beans = getBeans(Joensuu.class);
        assertFalse(beans.isEmpty());
    }

    @Test(expectedExceptions = IllegalProductException.class)
    @SpecAssertion(section = PASSIVATION_VALIDATION, id = "ea")
    public void testPassivatingScopeProducerMethodReturnsUnserializableObjectNotOk() {
        getContextualReference(Television.class).turnOn();
    }

    @Test(expectedExceptions = IllegalProductException.class)
    @SpecAssertion(section = PASSIVATION_VALIDATION, id = "eb")
    public void testNonSerializableProducerFieldDeclaredPassivatingThrowsIllegalProductException() {
        getContextualReference(HelsinkiNonSerializable.class).ping();
    }
}
