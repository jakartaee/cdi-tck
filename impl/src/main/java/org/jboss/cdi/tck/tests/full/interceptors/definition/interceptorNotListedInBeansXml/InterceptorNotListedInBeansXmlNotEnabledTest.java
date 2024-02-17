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
package org.jboss.cdi.tck.tests.full.interceptors.definition.interceptorNotListedInBeansXml;

import static org.jboss.cdi.tck.cdi.Sections.ENABLED_INTERCEPTORS;
import static org.testng.Assert.assertFalse;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.TestGroups;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = TestGroups.CDI_FULL)
public class InterceptorNotListedInBeansXmlNotEnabledTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InterceptorNotListedInBeansXmlNotEnabledTest.class)
                .withBeansXml("beans.xml").build();
    }

    @Test
    @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "i")
    public void testInterceptorNotListedInBeansXmlNotInvoked() {
        TransactionInterceptor.invoked = false;

        AccountHolder accountHolder = getContextualReference(AccountHolder.class);
        accountHolder.transfer(0);

        assertFalse(TransactionInterceptor.invoked);
    }
}
