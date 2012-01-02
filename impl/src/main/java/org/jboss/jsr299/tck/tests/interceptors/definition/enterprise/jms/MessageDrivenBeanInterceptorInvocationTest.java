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
package org.jboss.jsr299.tck.tests.interceptors.definition.enterprise.jms;

import static org.jboss.jsr299.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.jsr299.tck.TestGroups.JMS;
import static org.testng.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test that invocations of message listener methods of message-driven beans during message delivery are business method
 * invocations.
 * 
 * Note that basic JMS configuration is required for this test.
 * 
 * @author Martin Kouba
 */
@Test(groups = { JAVAEE_FULL, JMS })
@SpecVersion(spec = "cdi", version = "20091101")
public class MessageDrivenBeanInterceptorInvocationTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(MessageDrivenBeanInterceptorInvocationTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors()
                                .clazz(MissileInterceptor.class.getName()).up()).build();
    }

    @Inject
    SimpleMessageProducer producer;

    @Test
    public void testMessageDrivenBeanMethodIntercepted() throws Exception {

        MissileInterceptor.reset();

        producer.sendQueueMessage();

        // Wait for async processing
        Thread.sleep(300l);

        assertTrue(MessageDrivenMissile.messageAccepted);
        assertTrue(MissileInterceptor.methodIntercepted);
        assertTrue(MissileInterceptor.lifecycleCallbackIntercepted);
    }

}
