/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.beanManager.bean;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BINDING_INTERCEPTOR_TO_BEAN;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_BEAN;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.PassivationCapable;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * </p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class SyntheticBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SyntheticBeanTest.class)
                .withBeansXml(
                        new BeansXml(BeanDiscoveryMode.ALL).interceptors(SimpleInterceptor.class)
                                .decorators(VehicleDecorator.class))
                .withExtension(BeanExtension.class).build();
    }

    @Inject
    @Large
    Office office;

    @Inject
    @Hungry
    Lion lion;

    @Inject
    @Hungry
    Tiger tiger;

    @Inject
    FireTruck truck;

    @Inject
    SerializableOffice serializableOffice;

    @Test
    @SpecAssertion(section = BM_OBTAIN_BEAN, id = "a")
    public void testRegisteredBean() {
        Bean<Office> bean = getUniqueBean(Office.class, Large.Literal.INSTANCE);
        assertEquals(bean.getInjectionPoints().size(), 3);
        for (InjectionPoint ip : bean.getInjectionPoints()) {
            assertEquals(bean, ip.getBean());
        }
        testOffice(bean);
    }

    @Test
    @SpecAssertion(section = BM_OBTAIN_BEAN, id = "a")
    public void testSerializableBean() {
        @SuppressWarnings("unchecked")
        Bean<Office> bean = (Bean<Office>) getCurrentManager().resolve(
                getCurrentManager().getBeans(SerializableOffice.class, Any.Literal.INSTANCE));
        assertTrue(bean instanceof PassivationCapable);
        testOffice(bean);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BINDING_INTERCEPTOR_TO_BEAN, id = "a") })
    public void testSyntheticBeanIntercepted() {
        assertTrue(office.intercepted());
        assertTrue(serializableOffice.intercepted());
    }

    @Test
    @SpecAssertion(section = BM_OBTAIN_BEAN, id = "b")
    public void testSyntheticProducerField() {
        assertNotNull(lion);
        lion.foo();
        Bean<Lion> bean = getUniqueBean(Lion.class, Hungry.Literal.INSTANCE);
        assertTrue(bean.getQualifiers().contains(Hungry.Literal.INSTANCE));
    }

    @Test
    @SpecAssertion(section = BM_OBTAIN_BEAN, id = "b")
    public void testSyntheticProducerMethod() {
        assertNotNull(tiger);
        tiger.foo();
        Bean<Tiger> bean = getUniqueBean(Tiger.class, Hungry.Literal.INSTANCE);
        assertTrue(bean.getQualifiers().contains(Hungry.Literal.INSTANCE));
    }

    @Test
    @SpecAssertion(section = BM_OBTAIN_BEAN, id = "a")
    public void testSyntheticDecorator() {
        assertTrue(truck.decorated());
    }

    private void testOffice(Bean<Office> bean) {
        Office.reset();
        CreationalContext<Office> ctx = getCurrentManager().createCreationalContext(bean);
        Office office = bean.create(ctx);
        assertNotNull(office);
        assertNotNull(office.getConstructorEmployee());
        assertNotNull(office.getFieldEmployee());
        assertNotNull(office.getInitializerEmployee());
        assertTrue(office.isPostConstructCalled());
        bean.destroy(office, ctx);
        assertTrue(Office.isPreDestroyCalled());
    }
}
