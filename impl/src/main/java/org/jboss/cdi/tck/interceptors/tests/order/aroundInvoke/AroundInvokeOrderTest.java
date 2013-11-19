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
package org.jboss.cdi.tck.interceptors.tests.order.aroundInvoke;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "int", version = "1.2")
public class AroundInvokeOrderTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AroundInvokeOrderTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.2.1", id = "aa"), @SpecAssertion(section = "5.2.1", id = "ab"), @SpecAssertion(section = "5.2.1", id = "ba"),
            @SpecAssertion(section = "4", id = "a"), @SpecAssertion(section = "4", id = "d"), @SpecAssertion(section = "5.5", id = "b"),
            @SpecAssertion(section = "5.5", id = "c"), @SpecAssertion(section = "5.2.2", id = "a"), @SpecAssertion(section = "5.5", id = "e"),
            @SpecAssertion(section = "2.3", id = "ha"), @SpecAssertion(section = "5.1", id = "a") })
    public void testInvocationOrder() {

        // Expected order: Interceptor1, Interceptor2, Interceptor3, Interceptor4, Interceptor5, RailVehicle.intercept2,
        // Tram.intercept3
        assertEquals(getContextualReference(Tram.class).getId(), 8);

        // Overriden interceptor methods are not invoked
        assertFalse(OverridenInterceptor.isOverridenMethodCalled());
    }
}
