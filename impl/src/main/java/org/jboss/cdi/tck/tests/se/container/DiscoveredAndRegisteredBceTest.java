/*
 * Copyright 2025, Contributors to the Eclipse Foundation
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

package org.jboss.cdi.tck.tests.se.container;

import static org.jboss.cdi.tck.TestGroups.SE;
import static org.jboss.cdi.tck.cdi.Sections.SE_BOOTSTRAP;
import static org.jboss.cdi.tck.cdi.Sections.SE_CONTAINER_INITIALIZER;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

import org.jboss.arquillian.container.se.api.ClassPath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@Test(groups = SE)
@SpecVersion(spec = "cdi", version = "5.0")
public class DiscoveredAndRegisteredBceTest {

    @Deployment
    public static Archive<?> deployment() {
        final JavaArchive testArchive = ShrinkWrap.create(JavaArchive.class)
                .addPackage(DiscoveredAndRegisteredBceTest.class.getPackage())
                // make sure the extension can be discovered
                .addAsServiceProvider(BuildCompatibleExtension.class, TestBuildCompatibleExtension.class)
                .addAsResource(EmptyAsset.INSTANCE,
                        "META-INF/beans.xml");
        return ClassPath.builder().add(testArchive).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SE_BOOTSTRAP, id = "deb"),
            @SpecAssertion(section = SE_CONTAINER_INITIALIZER, id = "b") })
    public void testAddingAlreadyDiscoveredBCE() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery()
                .addBeanClasses(Foo.class)
                // make sure the extension is also registered
                .addBuildCompatibleExtensions(TestBuildCompatibleExtension.class)
                .initialize()) {
            // despite being added manually and also discovered, it should only register a single bean hence resolvable
            Instance<String> beanInstance = seContainer.select(String.class);
            assertTrue(beanInstance.isResolvable());
        }
    }
}
