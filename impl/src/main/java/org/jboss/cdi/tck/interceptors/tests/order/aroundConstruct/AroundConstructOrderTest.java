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
package org.jboss.cdi.tck.interceptors.tests.order.aroundConstruct;

import static org.jboss.cdi.tck.interceptors.InterceptorsSections.ENABLING_INTERCEPTORS;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INT_ORDERING_RULES;
import static org.jboss.cdi.tck.util.ActionSequence.assertSequenceDataEquals;

import jakarta.enterprise.inject.Instance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "interceptors", version = "1.2")
public class AroundConstructOrderTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AroundConstructOrderTest.class).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = ENABLING_INTERCEPTORS, id = "a")
    @SpecAssertion(section = INT_ORDERING_RULES, id = "c")
    @SpecAssertion(section = INT_ORDERING_RULES, id = "d")
    @SpecAssertion(section = INT_ORDERING_RULES, id = "e")
    @SpecAssertion(section = INT_ORDERING_RULES, id = "f")
    public void testInvocationOrder(Instance<Foo> instance) {
        ActionSequence.reset();
        instance.get();
        assertSequenceDataEquals(SuperInterceptor1.class, MiddleInterceptor1.class, Interceptor1.class, Interceptor2.class,
                Interceptor3.class, Interceptor4.class);
    }
}
