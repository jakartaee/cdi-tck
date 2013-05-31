/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.metadata.injectionpoint;

import static org.jboss.cdi.tck.cdi.Sections.EVENT_METADATA;
import static org.jboss.cdi.tck.cdi.Sections.INJECTION_POINT;
import static org.jboss.cdi.tck.util.Assert.assertAnnotationSetMatches;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Note that injection point is not available for {@link BeanManager#fireEvent()}.
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class EventMetadataInjectionPointTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EventMetadataInjectionPointTest.class).build();
    }

    @Inject
    Notifier notifier;

    @Inject
    InfoObserver infoObserver;

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTION_POINT, id = "aa"), @SpecAssertion(section = EVENT_METADATA, id = "b") })
    public void testGetBean() {

        Bean<?> lastBean = null;

        notifier.fireInfoEvent();
        lastBean = infoObserver.getLastBean();
        assertNotNull(lastBean);
        assertEquals(lastBean.getBeanClass(), Notifier.class);

        notifier.fireInitializerInfoEvent();
        lastBean = infoObserver.getLastBean();
        assertNotNull(lastBean);
        assertEquals(lastBean.getBeanClass(), Notifier.class);

        notifier.fireConstructorInfoEvent();
        lastBean = infoObserver.getLastBean();
        assertNotNull(lastBean);
        assertEquals(lastBean.getBeanClass(), Notifier.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTION_POINT, id = "dca"), @SpecAssertion(section = EVENT_METADATA, id = "b") })
    public void testIsTransient() {

        notifier.fireInfoEvent();
        assertFalse(infoObserver.isLastIsTransient());

        notifier.fireTransientInfoEvent();
        assertTrue(infoObserver.isLastIsTransient());
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTION_POINT, id = "ba"), @SpecAssertion(section = EVENT_METADATA, id = "b") })
    public void testGetType() {

        Type lastType = null;
        Type eventInfoLiteralType = new TypeLiteral<Event<Info>>() {
        }.getType();

        notifier.fireInfoEvent();
        lastType = infoObserver.getLastType();
        assertNotNull(lastType);
        assertEquals(lastType, eventInfoLiteralType);

        notifier.fireInitializerInfoEvent();
        lastType = infoObserver.getLastType();
        assertNotNull(lastType);
        assertEquals(lastType, eventInfoLiteralType);

        notifier.fireConstructorInfoEvent();
        lastType = infoObserver.getLastType();
        assertNotNull(lastType);
        assertEquals(lastType, eventInfoLiteralType);
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTION_POINT, id = "bc"), @SpecAssertion(section = EVENT_METADATA, id = "b") })
    public void testGetQualifiers() {

        Set<Annotation> lastQualifiers = null;

        notifier.fireInfoEvent();
        lastQualifiers = infoObserver.getLastQualifiers();
        assertNotNull(lastQualifiers);
        assertAnnotationSetMatches(lastQualifiers, Default.class);

        notifier.fireConstructorInfoEvent();
        lastQualifiers = infoObserver.getLastQualifiers();
        assertNotNull(lastQualifiers);
        assertAnnotationSetMatches(lastQualifiers, Nice.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTION_POINT, id = "ca"),
            @SpecAssertion(section = INJECTION_POINT, id = "cc"), @SpecAssertion(section = INJECTION_POINT, id = "cb"),
            @SpecAssertion(section = EVENT_METADATA, id = "b") })
    public void testGetMember() {

        Member lastMember = null;

        notifier.fireInfoEvent();
        lastMember = infoObserver.getLastMember();
        assertNotNull(lastMember);
        assertTrue(lastMember instanceof Field);
        Field field = (Field) lastMember;
        assertEquals(field.getName(), "infoEvent");
        assertEquals(field.getType(), Event.class);
        assertEquals(field.getDeclaringClass(), Notifier.class);

        notifier.fireInitializerInfoEvent();
        lastMember = infoObserver.getLastMember();
        assertNotNull(lastMember);
        assertTrue(lastMember instanceof Method);
        Method method = (Method) lastMember;
        assertEquals(method.getName(), "setInitializerInjectionInfoEvent");
        assertEquals(method.getParameterTypes().length, 1);
        assertEquals(method.getDeclaringClass(), Notifier.class);

        notifier.fireConstructorInfoEvent();
        lastMember = infoObserver.getLastMember();
        assertNotNull(lastMember);
        assertTrue(lastMember instanceof Constructor);
        Constructor<?> constructor = (Constructor<?>) lastMember;
        assertEquals(constructor.getParameterTypes().length, 1);
        assertEquals(constructor.getDeclaringClass(), Notifier.class);
    }

    @SuppressWarnings("rawtypes")
    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTION_POINT, id = "daa"), @SpecAssertion(section = EVENT_METADATA, id = "b") })
    public void testGetAnnotatedType() {

        Annotated lastAnnotated = null;

        notifier.fireInfoEvent();
        lastAnnotated = infoObserver.getLastAnnotated();
        assertTrue(lastAnnotated instanceof AnnotatedField);
        assertEquals(((AnnotatedField) lastAnnotated).getJavaMember().getName(), "infoEvent");
        assertTrue(lastAnnotated.isAnnotationPresent(Inject.class));

        notifier.fireInitializerInfoEvent();
        lastAnnotated = infoObserver.getLastAnnotated();
        assertTrue(lastAnnotated instanceof AnnotatedParameter);
        assertEquals(((AnnotatedParameter) lastAnnotated).getPosition(), 0);

        notifier.fireConstructorInfoEvent();
        lastAnnotated = infoObserver.getLastAnnotated();
        assertTrue(lastAnnotated instanceof AnnotatedParameter);
        assertEquals(((AnnotatedParameter) lastAnnotated).getPosition(), 0);
        assertTrue(lastAnnotated.isAnnotationPresent(Nice.class));
    }

}
