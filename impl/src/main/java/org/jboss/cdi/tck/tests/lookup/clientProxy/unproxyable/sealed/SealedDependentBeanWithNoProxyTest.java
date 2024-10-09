/*
 * Copyright 2024, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.lookup.clientProxy.unproxyable.sealed;

import static org.jboss.cdi.tck.cdi.Sections.UNPROXYABLE;
import static org.testng.Assert.assertEquals;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.testng.annotations.Test;

public class SealedDependentBeanWithNoProxyTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(SealedDependentBeanWithNoProxyTest.class)
                .withClasses(MyDependent.class, MyDependentSubclass.class, InjectingBean3.class).build();
    }

    @Inject
    MyDependent bean;

    @Test
    @SpecAssertion(section = UNPROXYABLE, id = "bdb")
    public void testSealedDependentBeanWithNoProxyWorks() {
        // dependent bean with no proxyability requirement should work
        assertEquals(MyDependent.class.getSimpleName(), bean.ping());

    }
}
