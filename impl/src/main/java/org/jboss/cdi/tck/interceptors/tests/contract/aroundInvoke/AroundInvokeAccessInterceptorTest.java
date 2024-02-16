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
package org.jboss.cdi.tck.interceptors.tests.contract.aroundInvoke;

import static org.jboss.cdi.tck.interceptors.InterceptorsSections.BUSINESS_METHOD_INTERCEPTOR_METHODS;
import static org.testng.Assert.assertEquals;


import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "interceptors", version = "1.2")
public class AroundInvokeAccessInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AroundInvokeAccessInterceptorTest.class).build();
    }

    @Test
    @SpecAssertion(section = BUSINESS_METHOD_INTERCEPTOR_METHODS, id = "cb")
    public void testPrivateAroundInvokeInterceptor() {
        assertEquals(getContextualReference(SimpleBean.class).zero(), 1);
        assertEquals(getContextualReference(Bean3.class).zero(), 1);
    }

    @Test
    @SpecAssertion(section = BUSINESS_METHOD_INTERCEPTOR_METHODS, id = "cc")
    public void testProtectedAroundInvokeInterceptor() {
        assertEquals(getContextualReference(SimpleBean.class).one(), 2);
        assertEquals(getContextualReference(Bean1.class).zero(), 1);
    }

    @Test
    @SpecAssertion(section = BUSINESS_METHOD_INTERCEPTOR_METHODS, id = "cd")
    public void testPackagePrivateAroundInvokeInterceptor() {
        assertEquals(getContextualReference(SimpleBean.class).two(), 3);
        assertEquals(getContextualReference(Bean2.class).zero(), 1);
    }

}
