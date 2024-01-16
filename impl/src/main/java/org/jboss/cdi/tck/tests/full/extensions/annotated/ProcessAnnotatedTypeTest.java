/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.full.extensions.annotated;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_ANNOTATED_TYPE;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_DISCOVERY_STEPS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for the extensions provided by the ProcessAnnotatedType events.
 * 
 * @author David Allen
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class ProcessAnnotatedTypeTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProcessAnnotatedTypeTest.class)
                .withExtension(ProcessAnnotatedTypeObserver.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL)).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "aa"), @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "ab"),
            @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "c"), @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "j") })
    public void testProcessAnnotatedTypeEventsSent() {
        // Randomly test some of the classes and interfaces that should have
        // been discovered and sent via the event
        assertTrue(ProcessAnnotatedTypeObserver.getAnnotatedclasses().contains(AbstractC.class));
        assertTrue(ProcessAnnotatedTypeObserver.getAnnotatedclasses().contains(ClassD.class));
        assertTrue(ProcessAnnotatedTypeObserver.getAnnotatedclasses().contains(Dog.class));
        assertTrue(ProcessAnnotatedTypeObserver.getAnnotatedclasses().contains(InterfaceA.class));
    }

    @Test
    @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "ac")
    public void testProcessAnnotatedTypeFiredForEnum() {
        assertTrue(ProcessAnnotatedTypeObserver.getAnnotatedclasses().contains(Type.class));
    }

    @Test
    @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "ba")
    public void testGetAnnotatedType() {
        AnnotatedType<Dog> annotatedType = ProcessAnnotatedTypeObserver.getDogAnnotatedType();
        assertEquals(annotatedType.getBaseType(), Dog.class);
        Set<AnnotatedMethod<? super Dog>> annotatedMethods = annotatedType.getMethods();
        assertEquals(annotatedMethods.size(), 3);
        for (AnnotatedMethod<? super Dog> annotatedMethod : annotatedMethods) {
            Set<String> validMethodNames = new HashSet<String>(Arrays.asList("bite", "live", "drinkMilk"));
            if (!validMethodNames.contains(annotatedMethod.getJavaMember().getName())) {
                fail("Invalid method name found" + annotatedMethod.getJavaMember().getName());
            }
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "bb"), @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "ca") })
    public void testSetAnnotatedType() {
        assertTrue(TestAnnotatedType.isGetConstructorsUsed());
        assertTrue(TestAnnotatedType.isGetFieldsUsed());
        assertTrue(TestAnnotatedType.isGetMethodsUsed());
    }

    @Test
    @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "bc")
    public void testVeto() {
        assertTrue(getCurrentManager().getBeans(VetoedBean.class).isEmpty());
    }
}
