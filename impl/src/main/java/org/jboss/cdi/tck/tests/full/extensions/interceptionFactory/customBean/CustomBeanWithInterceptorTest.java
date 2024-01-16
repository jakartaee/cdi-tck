/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.interceptionFactory.customBean;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_INTERCEPTION_FACTORY;
import static org.jboss.cdi.tck.cdi.Sections.INTERCEPTION_FACTORY;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class CustomBeanWithInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(CustomBeanWithInterceptorTest.class)
                .withExtension(AfterBeanDiscoveryObserver.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_INTERCEPTION_FACTORY, id = "a"), @SpecAssertion(section = INTERCEPTION_FACTORY, id = "f") })
    public void customBeanIntercepted() {
        Account customAccount = getContextualReference(Account.class, Custom.CustomLiteral.INSTANCE);
        assertNotNull(customAccount);
        int remainingBalance = customAccount.withdraw(100);
        assertEquals(remainingBalance, 895);
    }
}
