/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.tests.se.context.custom;

import static org.jboss.cdi.tck.TestGroups.SE;
import static org.testng.Assert.assertEquals;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.enterprise.inject.spi.Extension;

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
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@Test(groups = SE)
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class CustomRequestContextSETest extends Arquillian {

    @Deployment
    public static Archive<?> deployment() {
        final JavaArchive testArchive = ShrinkWrap.create(JavaArchive.class)
                .addPackage(CustomRequestContextSETest.class.getPackage())
                .addAsServiceProvider(Extension.class, AfterBeanDiscoveryObserver.class)
                .addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
        return ClassPath.builder().add(testArchive).build();
    }

    //define custom request context which is always active
    @Test
    @SpecAssertions({ @SpecAssertion(section = Sections.BUILTIN_CONTEXTS, id = "a"), @SpecAssertion(section = Sections.BUILTIN_CONTEXTS, id = "b")})
    public void defineCustomRequestContext() {
        try (SeContainer seContainer = SeContainerInitializer.newInstance().initialize()) {
            RequestScopeCounter counter = seContainer.select(RequestScopeCounter.class).get();
            assertEquals(counter.increment(), 11);
        }
    }
}
