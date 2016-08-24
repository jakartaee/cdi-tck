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
package org.jboss.cdi.tck.tests.se.container.implicitBA;

import static org.jboss.cdi.tck.TestGroups.SE;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE_SE;
import static org.jboss.cdi.tck.cdi.Sections.SE_BOOTSTRAP;

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
public class ImplicitBeanArchiveSETest extends Arquillian {

    private static final String IMPLICIT_SCAN_KEY = "javax.enterprise.inject.scan.implicit";

    @Deployment
    public static Archive<?> deployment() {
        final JavaArchive implicitArchive = ShrinkWrap.create(JavaArchive.class)
                .addClasses(ImplicitBeanArchiveSETest.class, Bar.class);
        return ClassPath.builder().add(implicitArchive).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE_SE, id = "b"), @SpecAssertion(section = SE_BOOTSTRAP, id = "dj") })
    public void testImplicitArchiveDiscovered() {
        try (SeContainer seContainer = SeContainerInitializer.newInstance().addProperty(IMPLICIT_SCAN_KEY, true).initialize()) {
            Bar bar = seContainer.select(Bar.class).get();
            Assert.assertNotNull(bar);
            bar.ping();
        }
    }

}
