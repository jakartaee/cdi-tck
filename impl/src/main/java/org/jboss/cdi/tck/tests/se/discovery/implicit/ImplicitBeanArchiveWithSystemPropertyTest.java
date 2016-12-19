/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.se.discovery.implicit;

import static org.jboss.cdi.tck.TestGroups.SE;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE_SE;
import static org.jboss.cdi.tck.cdi.Sections.SE_BOOTSTRAP;

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import org.jboss.arquillian.container.se.api.ClassPath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = SE)
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class ImplicitBeanArchiveWithSystemPropertyTest extends Arquillian {

    @Deployment
    public static Archive<?> deployment() {
        final JavaArchive implicitArchive = ShrinkWrap.create(JavaArchive.class)
                .addClasses(ImplicitBeanArchiveWithSystemPropertyTest.class, Bar.class);
        return ClassPath.builder().add(implicitArchive).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE_SE, id = "a"), @SpecAssertion(section = SE_BOOTSTRAP, id = "dk") })
    public void testImplicitArchiveDiscovered() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.enterprise.inject.scan.implicit", true);
        try (SeContainer seContainer = SeContainerInitializer.newInstance().setProperties(properties).initialize()) {
            Bar bar = seContainer.select(Bar.class).get();
            Assert.assertNotNull(bar);
            bar.ping();
        }
    }

}
