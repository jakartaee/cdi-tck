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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processInjectionPoint;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY_STEPS;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_INJECTION_POINT;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_INJECTION_POINT_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.HierarchyDiscovery;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p/>
 * This test was originally part of Weld test suite.
 * <p/>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0-PFD")
public class ProcessInjectionPointFiredTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProcessInjectionPointFiredTest.class)
                .withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml")
                .withExtension(VerifyingExtension.class).build();
    }

    @Inject
    private VerifyingExtension extension;

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "a"), @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "ba"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "e"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "ja") })
    public void testFieldInjectionPoint() {
        InjectionPoint ip = extension.getAlpha();
        assertNotNull(ip);
        assertTrue(annotationSetMatches(ip.getQualifiers(), Foo.class));
        assertNotNull(ip.getBean());
        assertEquals(extension.getInjectingBean(), ip.getBean());
        verifyType(ip, Alpha.class, String.class);
        verifyAnnotated(ip);
        verifyMember(ip, InjectingBean.class);
        assertFalse(ip.isDelegate());
        assertTrue(ip.isTransient());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "a"), @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "ba"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "e"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "ja") })
    public void testConstructorInjectionPoint() {
        InjectionPoint ip = extension.getBravo();
        assertNotNull(ip);
        assertTrue(annotationSetMatches(ip.getQualifiers(), Bar.class));
        assertNotNull(ip.getBean());
        assertEquals(extension.getInjectingBean(), ip.getBean());
        verifyType(ip, Bravo.class, String.class);
        verifyAnnotated(ip);
        verifyMember(ip, InjectingBean.class);
        assertFalse(ip.isDelegate());
        assertFalse(ip.isTransient());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "a"), @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "ba"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "e") })
    public void testInitializerInjectionPoint() {
        InjectionPoint ip = extension.getCharlie();
        assertNotNull(ip);
        assertTrue(annotationSetMatches(ip.getQualifiers(), Default.class));
        assertNotNull(ip.getBean());
        assertEquals(extension.getInjectingBean(), ip.getBean());
        verifyType(ip, Charlie.class);
        verifyAnnotated(ip);
        verifyMember(ip, InjectingBean.class);
        assertFalse(ip.isDelegate());
        assertFalse(ip.isTransient());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "a"), @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "ba") })
    public void testProducerMethodInjectionPoint1() {
        InjectionPoint ip = extension.getProducerAlpha();
        assertNotNull(ip);
        assertTrue(annotationSetMatches(ip.getQualifiers(), Foo.class));
        assertNotNull(ip.getBean());
        assertEquals(extension.getProducingBean(), ip.getBean());
        verifyType(ip, Alpha.class, Integer.class);
        verifyAnnotated(ip);
        verifyMember(ip, InjectingBean.class);
        assertFalse(ip.isDelegate());
        assertFalse(ip.isTransient());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "a"), @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "ba") })
    public void testProducerMethodInjectionPoint2() {
        InjectionPoint ip = extension.getProducerBravo();
        assertNotNull(ip);
        assertTrue(annotationSetMatches(ip.getQualifiers(), Bar.class));
        assertNotNull(ip.getBean());
        assertEquals(extension.getProducingBean(), ip.getBean());
        verifyType(ip, Bravo.class, Integer.class);
        verifyAnnotated(ip);
        verifyMember(ip, InjectingBean.class);
        assertFalse(ip.isDelegate());
        assertFalse(ip.isTransient());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_POINT_EE, id = "a") })
    public void testJavaEEComponentInjectionPoint() {
        InjectionPoint servletIp = extension.getServletCharlie();
        assertNotNull(servletIp);
        verifyType(servletIp, Charlie.class);
        InjectionPoint filterIp = extension.getFilterCharlie();
        assertNotNull(filterIp);
        verifyType(filterIp, Charlie.class);
        InjectionPoint listenerIp = extension.getListenerCharlie();
        assertNotNull(listenerIp);
        verifyType(listenerIp, Charlie.class);
    }

    @Test
    @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "ka")
    public void testObserverMethodInjectionPoint() {
        InjectionPoint charlieIp = extension.getObserverCharliIp();
        assertNotNull(charlieIp);
        verifyType(charlieIp, Charlie.class);
        InjectionPoint deltaIp = extension.getObserverDeltaIp();
        assertNotNull(deltaIp);
        verifyType(deltaIp, Delta.class);
    }

    @Test
    @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "je")
    public void testDisposerMethodInjectionPoint() {
        InjectionPoint deltaIp = extension.getDisposerDeltaIp();
        assertNotNull(deltaIp);
        verifyType(deltaIp, Delta.class);
    }

    private static void verifyType(InjectionPoint ip, Class<?> rawType, Class<?>... typeParameters) {
        assertEquals(getRawType(ip.getType()), rawType);
        if (typeParameters.length > 0) {
            assertTrue(ip.getType() instanceof ParameterizedType);
            assertTrue(Arrays.equals(typeParameters, getActualTypeArguments(ip.getType())));
        }
    }

    private static void verifyAnnotated(InjectionPoint ip) {
        assertNotNull(ip.getAnnotated());
        assertTrue(ip.getAnnotated().isAnnotationPresent(PlainAnnotation.class));
    }

    private static void verifyMember(InjectionPoint ip, Class<?> declaringClass) {
        assertNotNull(ip.getMember());
        assertEquals(declaringClass, ip.getMember().getDeclaringClass());
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<T>) type;
        } else if (type instanceof ParameterizedType) {
            if (((ParameterizedType) type).getRawType() instanceof Class<?>) {
                return (Class<T>) ((ParameterizedType) type).getRawType();
            }
        }
        return null;
    }

    private static Type[] getActualTypeArguments(Type type) {
        Type resolvedType = new HierarchyDiscovery(type).getResolvedType();
        if (resolvedType instanceof ParameterizedType) {
            return ((ParameterizedType) resolvedType).getActualTypeArguments();
        } else {
            return new Type[] {};
        }
    }
}
