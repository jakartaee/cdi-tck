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
package org.jboss.cdi.tck.interceptors.tests.contract.businessMethods;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "int", version = "1.2")
public class AroundInvokeInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AroundInvokeInterceptorTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.5", id = "a"), @SpecAssertion(section = "2.5", id = "cb") })
    public void testPrivateAroundInvokeInterceptor() {
        assertEquals(getContextualReference(SimpleBean.class).zero(), 1);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.5", id = "a"), @SpecAssertion(section = "2.5", id = "cc") })
    public void testProtectedAroundInvokeInterceptor() {
        assertEquals(getContextualReference(SimpleBean.class).one(), 2);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.5", id = "a"), @SpecAssertion(section = "2.5", id = "cd") })
    public void testPackagePrivateAroundInvokeInterceptor() {
        assertEquals(getContextualReference(SimpleBean.class).two(), 3);
    }
}
