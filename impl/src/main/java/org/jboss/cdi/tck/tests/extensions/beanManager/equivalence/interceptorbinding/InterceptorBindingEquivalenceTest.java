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

package org.jboss.cdi.tck.tests.extensions.beanManager.equivalence.interceptorbinding;

import static org.jboss.cdi.tck.cdi.Sections.BM_DETERMINING_HASH;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.inject.spi.Interceptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class InterceptorBindingEquivalenceTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InterceptorBindingEquivalenceTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).getOrCreateInterceptors()
                                .clazz(MissileInterceptor.class.getName()).up()).build();
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_DETERMINING_HASH, id = "b") })
    public void testAreInterceptorBindingsEquivalent() {

        Annotation literal1 = new MissileInterceptorBinding() {
        };
        Annotation literal2 = new MissileInterceptorBinding() {

            @Override
            public int numberOfTargets() {
                return 7;
            }

            @Override
            public Level level() {
                return Level.B;
            }
        };
        Annotation containerProvided = getContainerProvidedInterceptorBinding(literal1);
        assertTrue(getCurrentManager().areInterceptorBindingsEquivalent(literal1, containerProvided));
        assertFalse(getCurrentManager().areInterceptorBindingsEquivalent(literal1, literal2));
        assertFalse(getCurrentManager().areInterceptorBindingsEquivalent(containerProvided, literal2));
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_DETERMINING_HASH, id = "d") })
    public void testGetInterceptorBindingHashCode() {

        Annotation literal1 = new MissileInterceptorBinding() {
        };
        Annotation literal2 = new MissileInterceptorBinding() {

            @Override
            public String position() {
                return "hill";
            }
        };
        Annotation containerProvided = getContainerProvidedInterceptorBinding(literal1);
        assertEquals(getCurrentManager().getInterceptorBindingHashCode(containerProvided), getCurrentManager()
                .getInterceptorBindingHashCode(literal1));
        assertNotEquals(getCurrentManager().getInterceptorBindingHashCode(literal1), getCurrentManager()
                .getInterceptorBindingHashCode(literal2));
        assertNotEquals(getCurrentManager().getInterceptorBindingHashCode(containerProvided), getCurrentManager()
                .getInterceptorBindingHashCode(literal2));
    }

    private Annotation getContainerProvidedInterceptorBinding(Annotation literal) {
        List<Interceptor<?>> interceptors = getCurrentManager().resolveInterceptors(InterceptionType.AROUND_INVOKE, literal);
        assertFalse(interceptors.isEmpty());
        return interceptors.get(0).getInterceptorBindings().iterator().next();
    }
}
