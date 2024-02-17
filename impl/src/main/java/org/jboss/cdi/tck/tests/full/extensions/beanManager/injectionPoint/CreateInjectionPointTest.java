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
package org.jboss.cdi.tck.tests.full.extensions.beanManager.injectionPoint;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_INJECTIONPOINT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;

import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.AnnotatedConstructor;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedParameter;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.InjectionPoint;

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
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class CreateInjectionPointTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(CreateInjectionPointTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL)).build();
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_INJECTIONPOINT, id = "a") })
    public void testField() {
        AnnotatedType<?> type = getCurrentManager().createAnnotatedType(Library.class);
        assertEquals(type.getFields().size(), 1);
        AnnotatedField<?> field = type.getFields().iterator().next();
        InjectionPoint ip = getCurrentManager().createInjectionPoint(field);
        validateParameterizedType(ip.getType(), Book.class, String.class);
        annotationSetMatches(ip.getQualifiers(), Monograph.class, Fictional.class);
        assertNull(ip.getBean());
        assertEquals(field.getJavaMember(), ip.getMember());
        assertNotNull(ip.getAnnotated());
        assertFalse(ip.isDelegate());
        assertTrue(ip.isTransient());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_INJECTIONPOINT, id = "b") })
    public void testConstructorParameter() {
        AnnotatedType<?> type = getCurrentManager().createAnnotatedType(Library.class);
        assertEquals(type.getConstructors().size(), 1);
        AnnotatedConstructor<?> constructor = type.getConstructors().iterator().next();
        AnnotatedParameter<?> parameter = constructor.getParameters().get(1);
        InjectionPoint ip = getCurrentManager().createInjectionPoint(parameter);
        validateParameterizedType(ip.getType(), Book.class, String.class);
        annotationSetMatches(ip.getQualifiers(), Fictional.class);
        assertNull(ip.getBean());
        assertEquals(constructor.getJavaMember(), ip.getMember());
        assertNotNull(ip.getAnnotated());
        assertFalse(ip.isDelegate());
        assertFalse(ip.isTransient());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_INJECTIONPOINT, id = "b") })
    public void testMethodParameter() {
        AnnotatedType<?> type = getCurrentManager().createAnnotatedType(Library.class);
        assertEquals(1, type.getMethods().size());
        AnnotatedMethod<?> method = type.getMethods().iterator().next();
        AnnotatedParameter<?> parameter = method.getParameters().get(2);
        InjectionPoint ip = getCurrentManager().createInjectionPoint(parameter);
        validateParameterizedType(ip.getType(), Book.class, Integer.class);
        annotationSetMatches(ip.getQualifiers(), Default.class);
        assertNull(ip.getBean());
        assertEquals(method.getJavaMember(), ip.getMember());
        assertNotNull(ip.getAnnotated());
        assertFalse(ip.isDelegate());
        assertFalse(ip.isTransient());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_INJECTIONPOINT, id = "c") })
    public void testInvalidField() {
        AnnotatedField<Magazine> invalidField = new AnnotatedField<Magazine>() {

            @Override
            public boolean isStatic() {
                return false;
            }

            @Override
            public AnnotatedType<Magazine> getDeclaringType() {
                return null;
            }

            @Override
            public Type getBaseType() {
                return null;
            }

            @Override
            public Set<Type> getTypeClosure() {
                return null;
            }

            @Override
            public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
                return null;
            }

            @Override
            public <T extends Annotation> Set<T> getAnnotations(Class<T> annotationType) {
                return null;
            }

            @Override
            public Set<Annotation> getAnnotations() {
                return null;
            }

            @Override
            public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
                return false;
            }

            @Override
            public Field getJavaMember() {
                return null;
            }
        };
        getCurrentManager().createInjectionPoint(invalidField);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_INJECTIONPOINT, id = "c") })
    public void testInvalidParameter() {
        AnnotatedType<?> type = getCurrentManager().createAnnotatedType(NotABean.class);
        assertEquals(1, type.getMethods().size());
        AnnotatedMethod<?> method = type.getMethods().iterator().next();
        AnnotatedParameter<?> parameter = method.getParameters().get(0);
        getCurrentManager().createInjectionPoint(parameter);
    }

    private void validateParameterizedType(Type type, Class<?> rawType, Type... types) {
        assertTrue(type instanceof ParameterizedType);
        ParameterizedType parameterized = (ParameterizedType) type;
        assertEquals(rawType, parameterized.getRawType());
        assertTrue(Arrays.equals(types, parameterized.getActualTypeArguments()));
    }
}
