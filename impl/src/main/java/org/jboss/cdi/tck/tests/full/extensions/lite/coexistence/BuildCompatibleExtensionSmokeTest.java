/*
 * Copyright 2022, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.lite.coexistence;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.TestGroups;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.ExtensionLifecycleTest;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.SimpleExtension;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Verifies invocation of BCE and PE and the ability to override BCE via provided PE.
 */
@Test(groups = TestGroups.CDI_FULL)
@SpecVersion(spec = "cdi", version = "4.0")
public class BuildCompatibleExtensionSmokeTest extends AbstractTest {

    @Deployment
    public static WebArchive getDeployment() {
        return new WebArchiveBuilder().withTestClassPackage(BuildCompatibleExtensionSmokeTest.class)
                .withExtensions(OverridingPortableExtension.class, StandardPortableExtension.class)
                .withBuildCompatibleExtensions(StandardBuildCompatibleExtension.class, OverridenBuildCompatibleExtension.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ANNOTATED))
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.INIT_EVENTS, id = "g")
    public void testExtensionsCanCoexist() {
        // assert the deployment is fine, DummyBean should be resolvable
        Assert.assertTrue(getCurrentManager().createInstance().select(DummyBean.class).isResolvable());

        // assert that standard BCE was invoked correctly
        Assert.assertEquals(5, StandardBuildCompatibleExtension.TIMES_INVOKED);

        // assert that overriden BCE was not invoked
        Assert.assertEquals(0, OverridenBuildCompatibleExtension.TIMES_INVOKED);

        // assert that overriding portable extension was invoked
        Assert.assertEquals(3, OverridingPortableExtension.TIMES_INVOKED);

        // assert that standard portable extension was invoked
        Assert.assertTrue(StandardPortableExtension.INVOKED);
    }
}
