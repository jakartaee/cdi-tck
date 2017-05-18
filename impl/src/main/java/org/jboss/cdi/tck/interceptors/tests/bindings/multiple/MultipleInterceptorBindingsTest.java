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
package org.jboss.cdi.tck.interceptors.tests.bindings.multiple;

import static org.jboss.cdi.tck.interceptors.InterceptorsSections.BINDING_INT_TO_COMPONENT;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.DECLARING_INTERCEPTOR_BINDINGS_OF_AN_INTERCEPTOR;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "interceptors", version = "1.2")
public class MultipleInterceptorBindingsTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(MultipleInterceptorBindingsTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).getOrCreateInterceptors()
                                .clazz(MissileInterceptor.class.getName(), LockInterceptor.class.getName()).up()).build();
    }

    @Test
    @SpecAssertion(section = BINDING_INT_TO_COMPONENT, id = "aa")
    @SpecAssertion(section = BINDING_INT_TO_COMPONENT, id = "ba")
    @SpecAssertion(section = DECLARING_INTERCEPTOR_BINDINGS_OF_AN_INTERCEPTOR, id = "a")
    @SpecAssertion(section = DECLARING_INTERCEPTOR_BINDINGS_OF_AN_INTERCEPTOR, id = "b")
    @SpecAssertion(section = DECLARING_INTERCEPTOR_BINDINGS_OF_AN_INTERCEPTOR, id = "c")
    public void testInterceptorAppliedToBeanWithAllBindings() {
        MissileInterceptor.intercepted = false;
        Missile missile = getContextualReference(FastAndDeadlyMissile.class);
        missile.fire();
        assertTrue(MissileInterceptor.intercepted);
    }

    @Test
    @SpecAssertion(section = BINDING_INT_TO_COMPONENT, id = "aa")
    @SpecAssertion(section = BINDING_INT_TO_COMPONENT, id = "ba")
    @SpecAssertion(section = DECLARING_INTERCEPTOR_BINDINGS_OF_AN_INTERCEPTOR, id = "a")
    @SpecAssertion(section = DECLARING_INTERCEPTOR_BINDINGS_OF_AN_INTERCEPTOR, id = "b")
    @SpecAssertion(section = DECLARING_INTERCEPTOR_BINDINGS_OF_AN_INTERCEPTOR, id = "c")
    public void testInterceptorNotAppliedToBeanWithSomeBindings() {
        MissileInterceptor.intercepted = false;
        Missile missile = getContextualReference(SlowMissile.class);
        missile.fire();
        assertFalse(MissileInterceptor.intercepted);
    }

    @Test
    @SpecAssertion(section = BINDING_INT_TO_COMPONENT, id = "ab")
    @SpecAssertion(section = BINDING_INT_TO_COMPONENT, id = "bb")
    public void testMultipleInterceptorsOnMethod() {
        LockInterceptor.intercepted = false;
        GuidedMissile bullet = getContextualReference(GuidedMissile.class);
        bullet.fire();
        assertFalse(LockInterceptor.intercepted);
        bullet.lockAndFire();
        assertTrue(LockInterceptor.intercepted);
    }

}
