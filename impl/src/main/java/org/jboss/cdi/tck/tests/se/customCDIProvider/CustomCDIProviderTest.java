/*
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.se.customCDIProvider;

import static org.jboss.cdi.tck.TestGroups.SE;

import jakarta.enterprise.inject.spi.CDI;

import org.jboss.arquillian.container.se.api.ClassPath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = SE)
@SpecVersion(spec = "cdi", version = "2.0")
public class CustomCDIProviderTest extends Arquillian {

    @Deployment
    public static Archive<?> deployment() {
        final JavaArchive testArchive = ShrinkWrap.create(JavaArchive.class).addClasses(CustomCDIProviderTest.class, CustomCDIProvider.class)
                .addAsResource(EmptyAsset.INSTANCE,
                        "META-INF/beans.xml");
        return ClassPath.builder().add(testArchive).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = Sections.PROVIDER, id = "da"), @SpecAssertion(section = Sections.PROVIDER, id = "c") })
    public void testCustomCDIProvider() {
        CDI.setCDIProvider(new CustomCDIProvider());
        CDI<Object> current = CDI.current();
        Assert.assertNotNull(current);
        Assert.assertTrue(CustomCDIProvider.initializedCalled);
    }

}
