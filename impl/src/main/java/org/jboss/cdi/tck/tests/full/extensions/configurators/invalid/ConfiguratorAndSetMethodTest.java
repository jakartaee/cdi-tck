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
package org.jboss.cdi.tck.tests.full.extensions.configurators.invalid;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.testng.Assert.assertTrue;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests the invalid use case of calling set method and configurator in the same listener.
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class ConfiguratorAndSetMethodTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ConfiguratorAndSetMethodTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withExtension(ConfigureAndSetExtension.class).build();
    }

    @Inject
    ConfigureAndSetExtension extension;

    @Test
    @SpecAssertion(section = Sections.PROCESS_ANNOTATED_TYPE, id = "k")
    public void testPAT() {
        assertTrue(extension.PAT_ISE_CAUGHT);
        assertTrue(extension.PAT_REVERSE_ISE_CAUGHT);
    }

    @Test
    @SpecAssertion(section = Sections.PROCESS_BEAN_ATTRIBUTES, id = "g")
    public void testPBA() {
        assertTrue(extension.PBA_ISE_CAUGHT);
        assertTrue(extension.PBA_REVERSE_ISE_CAUGHT);
    }

    @Test
    @SpecAssertion(section = Sections.PROCESS_INJECTION_POINT, id = "g")
    public void testPIP() {
        assertTrue(extension.PIP_ISE_CAUGHT);
        assertTrue(extension.PIP_REVERSE_ISE_CAUGHT);
    }

    @Test
    @SpecAssertion(section = Sections.PROCESS_OBSERVER_METHOD, id = "e")
    public void testPOM() {
        assertTrue(extension.POM_ISE_CAUGHT);
        assertTrue(extension.POM_REVERSE_ISE_CAUGHT);
    }
}
