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
package org.jboss.cdi.tck.tests.interceptors.definition.multipleBindings;

import static org.jboss.cdi.tck.TestGroups.INTERCEPTORS_SPEC;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class MultipleInterceptorBindingsTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(MultipleInterceptorBindingsTest.class).withBeansXml("beans.xml")
                .build();
    }

    @Test(groups = INTERCEPTORS_SPEC)
    // @SpecAssertions({ @SpecAssertion(section = INTERCEPTOR_WITH_MULTIPLE_BINDINGS, id = "a"), @SpecAssertion(section = INTERCEPTOR_RESOLUTION, id = "ca"),
    //        @SpecAssertion(section = DECLARING_INTERCEPTOR, id = "ab"), @SpecAssertion(section = BINDING_INTERCEPTOR_TO_BEAN, id = "ba") })
    public void testInterceptorAppliedToBeanWithAllBindings() {
        MissileInterceptor.intercepted = false;

        Missile missile = getContextualReference(FastAndDeadlyMissile.class);
        missile.fire();

        assert MissileInterceptor.intercepted;
    }

    @Test(groups = INTERCEPTORS_SPEC)
    // @SpecAssertion(section = INTERCEPTOR_WITH_MULTIPLE_BINDINGS, id = "b")
    public void testInterceptorNotAppliedToBeanWithSomeBindings() {
        MissileInterceptor.intercepted = false;

        Missile missile = getContextualReference(SlowMissile.class);
        missile.fire();

        assert !MissileInterceptor.intercepted;
    }

    @Test(groups = INTERCEPTORS_SPEC)
    // @SpecAssertion(section = BINDING_INTERCEPTOR_TO_BEAN, id = "bb")
    public void testMultipleInterceptorsOnMethod() {
        LockInterceptor.intercepted = false;

        GuidedMissile bullet = getContextualReference(GuidedMissile.class);

        bullet.fire();
        assert !LockInterceptor.intercepted;

        bullet.lockAndFire();
        assert LockInterceptor.intercepted;
    }

}
