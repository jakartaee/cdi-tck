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
package org.jboss.cdi.tck.tests.se.container.customClassloader;

import static org.jboss.cdi.tck.TestGroups.SE;
import static org.jboss.cdi.tck.cdi.Sections.SE_BOOTSTRAP;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.enterprise.inject.spi.Extension;

import org.jboss.arquillian.container.se.api.ClassPath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * This test was originally part of Weld testsuite
 */
@Test(groups = SE)
@SpecVersion(spec = "cdi", version = "2.0-PFD")
public class CustomClassLoaderSETest extends Arquillian {

    @Deployment
    public static Archive<?> deployment() throws IOException {
        final JavaArchive bda1 = ShrinkWrap.create(JavaArchive.class)
                .addPackage(CustomClassLoaderSETest.class.getPackage())
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml").addAsServiceProvider(Extension.class, AlphaExtension.class, BravoExtension.class)
                .addAsServiceProvider(MyExtension.class, AlphaExtension.class);
        return ClassPath.builder().add(bda1).build();
    }

    @Test
    @SpecAssertion(section = SE_BOOTSTRAP, id = "dn")
    public void testCustomClassLoader() throws IOException {

        ClassLoader classLoader = new URLClassLoader(new URL[] {}, Alpha.class.getClassLoader()) {
            @Override
            public Enumeration<URL> getResources(String name) throws IOException {
                if ("META-INF/services/javax.enterprise.inject.spi.Extension".equals(name)) {
                    // Load only AlphaExtension
                    return super.getResources("META-INF/services/" + MyExtension.class.getName());
                }
                return super.getResources(name);
            }
        };

        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        try (SeContainer container = seContainerInitializer
                .setClassLoader(classLoader)
                .initialize()) {
            container.select(Alpha.class, ProcessedByExtension.ProcessedByExtensionLiteral.INSTANCE).get().ping();
            Assert.assertTrue(container.select(Bravo.class, ProcessedByExtension.ProcessedByExtensionLiteral.INSTANCE).isUnsatisfied());
        }

    }
}
