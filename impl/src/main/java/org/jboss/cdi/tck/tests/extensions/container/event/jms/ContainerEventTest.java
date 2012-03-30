/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.tests.extensions.container.event.jms;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.JMS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test verifies that ProcessInjectionTarget event is fired for message driven bean.
 * 
 * Note that basic JMS configuration is required for this test.
 * 
 * @author Martin Kouba
 */
@Test(groups = { JAVAEE_FULL, JMS })
@SpecVersion(spec = "cdi", version = "20091101")
public class ContainerEventTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ContainerEventTest.class)
                .withExtension(ProcessInjectionTargetObserver.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.8", id = "aaba"), @SpecAssertion(section = "11.5.8", id = "abba") })
    public void testProcessInjectionTargetEventFiredForMessageDrivenBean() {
        ProcessInjectionTarget<QueueMessageDrivenBean> event = ProcessInjectionTargetObserver.getMdbEvent();
        assertNotNull(event);
        AnnotatedType<QueueMessageDrivenBean> annotatedType = event.getAnnotatedType();
        assertEquals(annotatedType.getBaseType(), QueueMessageDrivenBean.class);
        // Methods initialize() and onMessage()
        assertEquals(annotatedType.getMethods().size(), 2);
        for (AnnotatedMethod<? super QueueMessageDrivenBean> method : annotatedType.getMethods()) {
            assertTrue(method.isAnnotationPresent(Inject.class) || method.isAnnotationPresent(Override.class));
        }
        // Fields sheep and initializerCalled
        assertEquals(annotatedType.getFields().size(), 2);
        for (AnnotatedField<? super QueueMessageDrivenBean> field : annotatedType.getFields()) {
            assertTrue(field.isAnnotationPresent(Inject.class) || field.isAnnotationPresent(SuppressWarnings.class));
        }
    }
}
