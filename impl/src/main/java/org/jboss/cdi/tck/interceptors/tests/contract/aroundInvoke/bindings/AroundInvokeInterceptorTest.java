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
package org.jboss.cdi.tck.interceptors.tests.contract.aroundInvoke.bindings;

import static org.jboss.cdi.tck.interceptors.InterceptorsSections.BUSINESS_METHOD_INTERCEPTOR_METHODS;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.DEF_OF_INTERCEPTOR_CLASSES_AND_INTERCEPTOR_METHODS;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INT_ORDERING_RULES;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Matus Abaffy
 */
@SpecVersion(spec = "interceptors", version = "1.2")
public class AroundInvokeInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AroundInvokeInterceptorTest.class).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = DEF_OF_INTERCEPTOR_CLASSES_AND_INTERCEPTOR_METHODS, id = "ac")
    @SpecAssertion(section = DEF_OF_INTERCEPTOR_CLASSES_AND_INTERCEPTOR_METHODS, id = "aa")
    @SpecAssertion(section = DEF_OF_INTERCEPTOR_CLASSES_AND_INTERCEPTOR_METHODS, id = "ad")
    @SpecAssertion(section = BUSINESS_METHOD_INTERCEPTOR_METHODS, id = "a")
    @SpecAssertion(section = BUSINESS_METHOD_INTERCEPTOR_METHODS, id = "b")
    @SpecAssertion(section = BUSINESS_METHOD_INTERCEPTOR_METHODS, id = "ca")
    @SpecAssertion(section = INT_ORDERING_RULES, id = "h")
    public void testBusinessMethodIntercepted(Foo foo) throws Exception {
        ActionSequence.reset();
        foo.ping();

        ActionSequence.assertSequenceDataContainsAll(SuperInterceptor1.class, MiddleInterceptor1.class, Interceptor1.class,
                SuperInterceptor2.class, Interceptor2.class, SuperFoo.class, MiddleFoo.class, Foo.class);

        ActionSequence.assertSequenceDataEquals(SuperInterceptor1.class, MiddleInterceptor1.class, Interceptor1.class,
                SuperInterceptor2.class, Interceptor2.class, SuperFoo.class, MiddleFoo.class, Foo.class);
    }
}
