/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.deployment.packaging.installedLibrary;

import static org.jboss.cdi.tck.TestGroups.INSTALLED_LIB;
import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.extlib.Strict;
import org.jboss.cdi.tck.extlib.StrictLiteral;
import org.jboss.cdi.tck.extlib.Translator;
import org.jboss.cdi.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.se.manifest.ManifestDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test installed library bean archive referenced by an EAR.
 * 
 * TODO I'm not completely sure about test configuration (manifests contents).
 * 
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class InstalledLibraryEarTest extends AbstractTest {

    @Deployment
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder().withTestClass(InstalledLibraryEarTest.class)
                .build();

        enterpriseArchive.setManifest(new StringAsset(Descriptors.create(ManifestDescriptor.class)
                .attribute("Extension-List", "CDITCKExtLib")
                .attribute("CDITCKExtLib-Extension-Name", "org.jboss.cdi.tck.extlib")
                // .attribute("CDITCKExtLib-Specification-Version", "1.0")
                // .attribute("CDITCKExtLib-Implementation-Version", "1.0")
                // .attribute("CDITCKExtLib-Implementation-Vendor-Id", "org.jboss")
                .exportAsString()));

        return enterpriseArchive;
    }

    @Test(groups = { JAVAEE_FULL, INSTALLED_LIB }, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "12.1", id = "bbf") })
    public void testInjection(@Strict Translator translator) {
        assertNotNull(translator);
        assertEquals(translator.echo("hello"), "hello");
    }

    @Test(groups = { JAVAEE_FULL, INSTALLED_LIB })
    @SpecAssertions({ @SpecAssertion(section = "12.1", id = "bbf") })
    public void testResolution() {
        // No assertion needed
        getUniqueBean(Translator.class, StrictLiteral.INSTANCE);
    }
}
