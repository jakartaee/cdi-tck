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
package org.jboss.cdi.tck.interceptors.tests.order.aroundInvoke;

import static org.jboss.cdi.tck.interceptors.InterceptorsSections.ASSOCIATING_INT_USING_INTERCEPTORS_ANNOTATION;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.ENABLING_INTERCEPTORS;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INT_ORDERING_RULES;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INVOCATIONCONTEXT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "interceptors", version = "1.2")
public class AroundInvokeOrderTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AroundInvokeOrderTest.class).build();
    }

    @Test
    @SpecAssertion(section = ASSOCIATING_INT_USING_INTERCEPTORS_ANNOTATION, id = "a")
    @SpecAssertion(section = ASSOCIATING_INT_USING_INTERCEPTORS_ANNOTATION, id = "b")
    @SpecAssertion(section = INVOCATIONCONTEXT, id = "ha")
    @SpecAssertion(section = ENABLING_INTERCEPTORS, id = "a")
    @SpecAssertion(section = INT_ORDERING_RULES, id = "c")
    @SpecAssertion(section = INT_ORDERING_RULES, id = "d")
    @SpecAssertion(section = INT_ORDERING_RULES, id = "e")
    @SpecAssertion(section = INT_ORDERING_RULES, id = "f")
    @SpecAssertion(section = INT_ORDERING_RULES, id = "i")
    @SpecAssertion(section = INT_ORDERING_RULES, id = "j")
    public void testInvocationOrder() {

        // Expected order: Interceptor1, Interceptor2, Interceptor3, Interceptor4, Interceptor5, Vehicle.intercept,
        // RailVehicle.intercept2, Tram.intercept3
        assertEquals(getContextualReference(Tram.class).getId(), 8);

        // Overriden interceptor methods are not invoked
        assertFalse(OverridenInterceptor.isOverridenMethodCalled());
    }
}
