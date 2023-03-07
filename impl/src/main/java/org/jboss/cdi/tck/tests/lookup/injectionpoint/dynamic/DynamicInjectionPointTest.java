/*
 * JBoss, Home of Professional Open Source
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

package org.jboss.cdi.tck.tests.lookup.injectionpoint.dynamic;

import static org.jboss.cdi.tck.cdi.Sections.INJECTION_POINT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Set;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedParameter;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class DynamicInjectionPointTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DynamicInjectionPointTest.class).build();
    }

    @Inject
    Bar bar;

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "aab")
    public void testInjectionPointGetBean() {
        assertEquals(bar.getFoo().getInjectionPoint().getBean(), getUniqueBean(Bar.class));
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "baa")
    public void testInjectionPointGetType() {
        assertEquals(bar.getFoo().getInjectionPoint().getType(), Foo.class);
        assertEquals(bar.getTypeNiceFoo().getInjectionPoint().getType(), NiceFoo.class);
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "bca")
    public void testInjectionPointGetQualifiers() {

        Set<Annotation> fooQualifiers = bar.getFoo().getInjectionPoint().getQualifiers();
        Set<Annotation> niceFooQualifiers = bar.getQualifierNiceFoo().getInjectionPoint().getQualifiers();

        annotationSetMatches(fooQualifiers, Any.Literal.INSTANCE, Default.Literal.INSTANCE);
        annotationSetMatches(niceFooQualifiers, Any.Literal.INSTANCE, new Nice.Literal());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTION_POINT, id = "caa"), @SpecAssertion(section = INJECTION_POINT, id = "cba"),
            @SpecAssertion(section = INJECTION_POINT, id = "cca") })
    public void testInjectionPointGetMember() {

        Member fieldMember = bar.getFoo().getInjectionPoint().getMember();
        assertNotNull(fieldMember);
        assertTrue(fieldMember instanceof Field);
        Field field = (Field) fieldMember;
        assertEquals(field.getName(), "fooInstance");
        assertEquals(field.getType(), Instance.class);
        assertEquals(field.getDeclaringClass(), Bar.class);

        Member methodMember = bar.getInitializerFoo().getInjectionPoint().getMember();
        assertNotNull(methodMember);
        assertTrue(methodMember instanceof Method);
        Method method = (Method) methodMember;
        assertEquals(method.getName(), "setInitializerInjectionFooInstance");
        assertEquals(method.getParameterTypes().length, 1);
        assertEquals(method.getDeclaringClass(), Bar.class);

        Member constructorMember = bar.getConstructorInjectionFoo().getInjectionPoint().getMember();
        assertNotNull(constructorMember);
        assertTrue(constructorMember instanceof Constructor);
        Constructor<?> constructor = (Constructor<?>) constructorMember;
        // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6294399
        assertTrue("org.jboss.cdi.tck.tests.lookup.injectionpoint.dynamic.Bar".equals(constructor.getName())
                || "Bar".equals(constructor.getName()));
        assertNotNull(constructor.getAnnotation(Inject.class));
        assertEquals(constructor.getDeclaringClass(), Bar.class);
    }

    @SuppressWarnings("rawtypes")
    @Test
    @SpecAssertion(section=INJECTION_POINT, id="dab")
    public void testInjectionPointGetAnnotated() {

        Annotated fooFieldAnnotated = bar.getFoo().getInjectionPoint().getAnnotated();
        assertTrue(fooFieldAnnotated instanceof AnnotatedField);
        assertEquals(((AnnotatedField) fooFieldAnnotated).getJavaMember().getName(), "fooInstance");
        assertTrue(fooFieldAnnotated.isAnnotationPresent(Any.class));

        Annotated fooInitializerAnnnotated = bar.getInitializerFoo().getInjectionPoint().getAnnotated();
        assertTrue(fooInitializerAnnnotated instanceof AnnotatedParameter);
        assertEquals(((AnnotatedParameter) fooInitializerAnnnotated).getPosition(), 0);

        Annotated fooConstructorAnnnotated = bar.getConstructorInjectionFoo().getInjectionPoint().getAnnotated();
        assertTrue(fooConstructorAnnnotated instanceof AnnotatedParameter);
        assertEquals(((AnnotatedParameter) fooConstructorAnnnotated).getPosition(), 0);
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "dbb")
    public void testInjectionPointIsDelegate() {
        assertFalse(bar.getFoo().getInjectionPoint().isDelegate());
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "dcb")
    public void testInjectionPointIsTransient() {
        assertTrue(bar.getTransientFoo().getInjectionPoint().isTransient());
        assertFalse(bar.getFoo().getInjectionPoint().isTransient());
    }

}
