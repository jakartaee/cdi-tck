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
package org.jboss.cdi.tck.tests.se.container.contextualReference;

import static org.jboss.cdi.tck.TestGroups.SE;
import static org.jboss.cdi.tck.cdi.Sections.CONTEXTUAL_REFERENCE_VALIDITY;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import org.jboss.arquillian.container.se.api.ClassPath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@Test(groups = SE)
@SpecVersion(spec = "cdi", version = "2.0-PFD")
public class InvalidContextualReferenceTest extends Arquillian {
    
    @Deployment
    public static Archive<?> deployment() {
        final JavaArchive testArchive = ShrinkWrap.create(JavaArchive.class)
                .addPackage(InvalidContextualReferenceTest.class.getPackage())
                .addAsResource(EmptyAsset.INSTANCE,
                        "META-INF/beans.xml");
        return ClassPath.builder().add(testArchive).build();
    }
    
    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertions(@SpecAssertion(section = CONTEXTUAL_REFERENCE_VALIDITY, id = "b"))
    public void testReferenceUsedAfterContainerShutdown() {
        SeContainerInitializer seContainerInitializer = SeContainerInitializer.newInstance();
        FooBean beanReferenceOutsideContainer = null;
        try (SeContainer seContainer = seContainerInitializer.disableDiscovery().addBeanClasses(FooBean.class).initialize()) {
            // obtain bean, use it and store
            FooBean validBeanReference = seContainer.select(FooBean.class).get();
            validBeanReference.ping();
            beanReferenceOutsideContainer = validBeanReference;
        }
        
        // now use stored reference after container shutdown
        // this should throw IllegalStateException
        beanReferenceOutsideContainer.ping();
    }
}
