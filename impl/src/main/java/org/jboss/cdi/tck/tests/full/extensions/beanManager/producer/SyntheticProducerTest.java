/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.beanManager.producer;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_PRODUCER;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedMember;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedParameter;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.Producer;

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
 * This test was originally part of the Weld test suite.
 * </p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class SyntheticProducerTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SyntheticProducerTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL)).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_PRODUCER, id = "b") })
    public void testStaticProducerField() {
        AnnotatedField<? super Factory> field = this.<Factory, AnnotatedField<Factory>> getAnnotatedMember(Factory.class,
                "WOODY");
        Producer<Toy> producer = cast(getCurrentManager().getProducerFactory(field, null).createProducer(null));
        assertNotNull(producer);
        assertTrue(producer.getInjectionPoints().isEmpty());
        Toy woody = producer.produce(getCurrentManager().<Toy> createCreationalContext(null));
        assertEquals("Woody", woody.getName());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_PRODUCER, id = "b") })
    public void testNonStaticProducerField() {
        AnnotatedField<? super AnotherFactory> field = this
                .<AnotherFactory, AnnotatedField<AnotherFactory>> getAnnotatedMember(AnotherFactory.class, "jessie");
        Bean<AnotherFactory> declaringBean = cast(getCurrentManager().resolve(
                getCurrentManager().getBeans(AnotherFactory.class)));
        Producer<Toy> producer = cast(getCurrentManager().getProducerFactory(field, declaringBean).createProducer(null));
        assertNotNull(producer);
        assertTrue(producer.getInjectionPoints().isEmpty());
        Toy jessie = producer.produce(getCurrentManager().<Toy> createCreationalContext(null));
        assertEquals("Jessie", jessie.getName());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_PRODUCER, id = "a") })
    public void testStaticProducerMethod() {
        AnnotatedMethod<? super Factory> method = this.<Factory, AnnotatedMethod<Factory>> getAnnotatedMember(Factory.class,
                "getBuzz");
        Producer<Toy> producer = cast(getCurrentManager().getProducerFactory(method, null).createProducer(null));
        assertNotNull(producer);
        validateInjectionPoints(producer.getInjectionPoints());
        Toy buzz = producer.produce(getCurrentManager().<Toy> createCreationalContext(null));
        assertEquals("Buzz Lightyear", buzz.getName());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_PRODUCER, id = "a") })
    public void testNonStaticProducerMethod() {
        AnnotatedMethod<? super AnotherFactory> method = this
                .<AnotherFactory, AnnotatedMethod<AnotherFactory>> getAnnotatedMember(AnotherFactory.class, "getRex");
        Bean<AnotherFactory> declaringBean = cast(getCurrentManager().resolve(
                getCurrentManager().getBeans(AnotherFactory.class)));
        Producer<Toy> producer = cast(getCurrentManager().getProducerFactory(method, declaringBean).createProducer(null));
        assertNotNull(producer);
        validateInjectionPoints(producer.getInjectionPoints());
        Toy rex = producer.produce(getCurrentManager().<Toy> createCreationalContext(null));
        assertEquals("Rex", rex.getName());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_PRODUCER, id = "c") })
    public void testInvalidProducerMethod1() {
        AnnotatedMethod<? super Factory> method = this.<Factory, AnnotatedMethod<Factory>> getAnnotatedMember(Factory.class,
                "invalidProducerMethod1");
        getCurrentManager().getProducerFactory(method, null).createProducer(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_PRODUCER, id = "c") })
    public void testInvalidProducerMethod2() {
        // Producer method is not static but no declaringBean is provided
        AnnotatedMethod<? super Factory> method = this.<Factory, AnnotatedMethod<Factory>> getAnnotatedMember(Factory.class,
                "invalidProducerMethod2");
        getCurrentManager().getProducerFactory(method, null).createProducer(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_PRODUCER, id = "d") })
    public void testInvalidProducerField1() {
        // Producer field type contains a wildcard type parameter
        AnnotatedField<? super Factory> field = this.<Factory, AnnotatedField<Factory>> getAnnotatedMember(Factory.class,
                "INVALID_FIELD1");
        getCurrentManager().getProducerFactory(field, null).createProducer(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_PRODUCER, id = "d") })
    public void testInvalidProducerField2() {
        // Producer field is annotated @Inject
        AnnotatedField<? super Factory> field = this.<Factory, AnnotatedField<Factory>> getAnnotatedMember(Factory.class,
                "INVALID_FIELD2");
        getCurrentManager().getProducerFactory(field, null).createProducer(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_PRODUCER, id = "d") })
    public void testInvalidProducerField3() {
        // Producer field is not static but no declaringBean is provided
        AnnotatedField<? super Factory> field = this.<Factory, AnnotatedField<Factory>> getAnnotatedMember(Factory.class,
                "INVALID_FIELD3");
        getCurrentManager().getProducerFactory(field, null).createProducer(null);
    }

    @SuppressWarnings("unchecked")
    private <X, A extends AnnotatedMember<? super X>> A getAnnotatedMember(Class<X> javaClass, String memberName) {
        AnnotatedType<X> type = getCurrentManager().createAnnotatedType(javaClass);
        for (AnnotatedField<? super X> field : type.getFields()) {
            if (field.getJavaMember().getName().equals(memberName)) {
                return (A) field;
            }
        }
        for (AnnotatedMethod<? super X> method : type.getMethods()) {
            if (method.getJavaMember().getName().equals(memberName)) {
                return (A) method;
            }
        }
        throw new IllegalArgumentException("Member " + memberName + " not found on " + javaClass);
    }

    private void validateInjectionPoints(Set<InjectionPoint> injectionPoints) {
        assertEquals(2, injectionPoints.size());
        for (InjectionPoint ip : injectionPoints) {
            AnnotatedParameter<Factory> parameter = this.<AnnotatedParameter<Factory>> cast(ip.getAnnotated());
            if (parameter.getPosition() == 0) {
                // BeanManager
                assertEquals(BeanManager.class, parameter.getBaseType());
            } else if (parameter.getPosition() == 1) {
                // SpaceSuit<Toy>
                Type baseType = parameter.getBaseType();
                if (baseType instanceof ParameterizedType && ((ParameterizedType) baseType).getRawType() instanceof Class<?>) {
                    assertEquals(((ParameterizedType) baseType).getRawType(), SpaceSuit.class);
                } else {
                    fail();
                }
            } else {
                fail("Unexpected injection point " + ip);
            }
            assertFalse(ip.isDelegate());
            assertFalse(ip.isTransient());
            assertNull(ip.getBean());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T cast(Object obj) {
        return (T) obj;
    }
}
