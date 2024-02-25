/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.alternative.veto;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE;

import jakarta.enterprise.context.RequestScoped;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class VetoedAlternativeTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(VetoedAlternativeTest.class)
                .withExtension(VetoingExtension.class)
                .withBeansXml(
                        new BeansXml(BeanDiscoveryMode.ALL)
                                .alternatives(MockPaymentProcessorImpl.class)
                                .stereotype(AlternativeConsumerStereotype.class))
                .build();
    }

    @Test(groups = CDI_FULL)
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "e")
    public void mockAlternativeIsVetoed() {
        Assert.assertEquals(getUniqueBean(PaymentProcessor.class).getName(), "paymentProcessorImpl");
    }

    @Test(groups = CDI_FULL)
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "e")
    public void alternativeStereotypeIsVetoed() {
        Assert.assertEquals(getUniqueBean(Consumer.class).getScope(), RequestScoped.class);
    }

}
