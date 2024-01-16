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
package org.jboss.cdi.tck.tests.full.extensions.configurators;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_ANNOTATED_TYPE;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_BEAN_ATTRIBUTES;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_INJECTION_POINT;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_OBSERVER_METHOD;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.DisposesLiteral;
import org.jboss.cdi.tck.literals.ProducesLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class BasicConfiguratorsTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BasicConfiguratorsTest.class)
                .withClasses(ProducesLiteral.class, DisposesLiteral.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withExtensions(DummyConfiguringExtension.class).build();
    }

    @Test
    @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "bbb")
    public void configuratorReturnsAlwaysSameAnnotatedTypeConfigurator() {
        assertTrue(getDummyConfiguringExtension().isSameATConfiguratorReturned().get());
    }

    @Test
    @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "bbb")
    public void configuratorReturnsAlwaysSameInjectionPointConfigurator() {
        assertTrue(getDummyConfiguringExtension().isSameIPConfiguratorReturned().get());
    }

    @Test
    @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "bcb")
    public void configuratorReturnsAlwaysSameBeanAttributesConfigurator() {
        assertTrue(getDummyConfiguringExtension().isSameBAConfiguratorReturned().get());
    }

    @Test
    @SpecAssertion(section = PROCESS_OBSERVER_METHOD, id = "dac")
    public void configuratorReturnsAlwaysSameObserverMethodConfigurator() {
        assertTrue(getDummyConfiguringExtension().isSameOMConfiguratorReturned().get());
    }

    private DummyConfiguringExtension getDummyConfiguringExtension() {
        return getCurrentManager().getExtension(DummyConfiguringExtension.class);
    }
}
