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
package org.jboss.cdi.tck.interceptors.tests.contract.interceptorLifeCycle.aroundConstruct;

import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INTERCEPTOR_LIFECYCLE;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.Instance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "interceptors", version = "1.2")
public class AroundConstructLifeCycleTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AroundConstructLifeCycleTest.class).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = INTERCEPTOR_LIFECYCLE, id = "da")
    public void testAroundConstructInvokedAfterDependencyInjectionOnInterceptorClasses(Instance<Foo> instance) {
        FooCommonInterceptor.reset();
        instance.get();
        // assertions are made in FooCommonInterceptor
        assertTrue(FooCommonInterceptor.commonAroundConstructCalled);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = INTERCEPTOR_LIFECYCLE, id = "db")
    @SpecAssertion(section = INTERCEPTOR_LIFECYCLE, id = "dc")
    public void testInstanceNotCreatedUnlessInvocationContextProceedCalled(Instance<Baz> instance) {
        Baz2Interceptor.setProceed(false);
        assertFalse(Baz.postConstructedCalled, "Instance created even though InvocationContext.proceed() was not called.");

        Baz2Interceptor.setProceed(true);
        Baz baz = instance.get();
        assertNotNull(baz, "Instance not created even though InvocationContext.proceed() was called.");
        assertTrue(baz.accessed);
        assertTrue(baz.injectionPerformedCorrectly());
    }
}
