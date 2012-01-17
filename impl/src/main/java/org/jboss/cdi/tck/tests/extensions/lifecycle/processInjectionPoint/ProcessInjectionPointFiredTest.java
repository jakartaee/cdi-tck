/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cdi.tck.tests.extensions.lifecycle.processInjectionPoint;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
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
import org.jboss.cdi.tck.util.ParameterizedTypeImpl;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class ProcessInjectionPointFiredTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProcessInjectionPointFiredTest.class)
                .withClasses(HierarchyDiscovery.class, ParameterizedTypeImpl.class).withExtension(VerifyingExtension.class)
                .build();
    }

    @Inject
    private VerifyingExtension extension;

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.7", id = "a"), @SpecAssertion(section = "11.5.7", id = "ba") })
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
    @SpecAssertions({ @SpecAssertion(section = "11.5.7", id = "a"), @SpecAssertion(section = "11.5.7", id = "ba") })
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
    @SpecAssertions({ @SpecAssertion(section = "11.5.7", id = "a"), @SpecAssertion(section = "11.5.7", id = "ba") })
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
    @SpecAssertions({ @SpecAssertion(section = "11.5.7", id = "a"), @SpecAssertion(section = "11.5.7", id = "ba") })
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
    @SpecAssertions({ @SpecAssertion(section = "11.5.7", id = "a"), @SpecAssertion(section = "11.5.7", id = "ba") })
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
