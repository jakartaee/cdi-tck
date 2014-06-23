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
package org.jboss.cdi.tck.interceptors.tests.contract.aroundInvoke;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.SECURITY;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "int", version = "1.2")
public class AroundInvokeAccessInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AroundInvokeAccessInterceptorTest.class).build();
    }

    @Test
    @SpecAssertion(section = "2.5", id = "cb")
    public void testPrivateAroundInvokeInterceptor() {
        assertEquals(getContextualReference(SimpleBean.class).zero(), 1);
        assertEquals(getContextualReference(Bean3.class).zero(), 1);
    }

    @Test
    @SpecAssertion(section = "2.5", id = "cc")
    public void testProtectedAroundInvokeInterceptor() {
        assertEquals(getContextualReference(SimpleBean.class).one(), 2);
        assertEquals(getContextualReference(Bean1.class).zero(), 1);
    }

    @Test
    @SpecAssertion(section = "2.5", id = "cd")
    public void testPackagePrivateAroundInvokeInterceptor() {
        assertEquals(getContextualReference(SimpleBean.class).two(), 3);
        assertEquals(getContextualReference(Bean2.class).zero(), 1);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = {JAVAEE_FULL, SECURITY})
    @SpecAssertions({ @SpecAssertion(section = "2.5", id = "e"), @SpecAssertion(section = "2.5", id = "fb") })
    public void testSecurityContext(Student student) throws Exception {
        student.printArticle();
        assertTrue(PrinterSecurityInterceptor.securityContextOK);
        assertTrue(Toner.calledFromInterceptor);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = JAVAEE_FULL)
    @SpecAssertion(section = "2.5", id = "fa")
    public void testTransactionContext(Foo foo, UserTransaction ut) throws Exception {
        ut.begin();

        foo.invoke();
        // checks are done in FooInterceptor and BazInterceptor
        assertTrue(FooInterceptor.called);
        assertTrue(BazInterceptor.called);

        ut.commit();
    }
}
