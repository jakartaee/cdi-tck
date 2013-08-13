/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.interceptors.tests.bindings.resolution.ejb;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.Timer;
import org.jboss.cdi.tck.util.Timer.StopCondition;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Interceptor resolution test.
 *
 * @author Martin Kouba
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "int", version = "1.2")
public class EnterpriseInterceptorBindingResolutionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(EnterpriseInterceptorBindingResolutionTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors()
                                .clazz(ComplicatedInterceptor.class.getName(), ComplicatedLifecycleInterceptor.class.getName())
                                .up()).build();
    }

    @SuppressWarnings("serial")
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "3.3", id = "e"), @SpecAssertion(section = "3.4", id = "ca"),
            @SpecAssertion(section = "3.4", id = "da"), @SpecAssertion(section = "3.4", id = "db"),
            @SpecAssertion(section = "3.4", id = "dc") })
    public void testBusinessMethodInterceptorBindings(MessageService messageService, MonitorService monitorService) {

        // Test interceptor is resolved (note non-binding member of BallBinding)
        assertEquals(
                getCurrentManager().resolveInterceptors(InterceptionType.AROUND_INVOKE,
                        new AnnotationLiteral<MessageBinding>() {
                        }, new AnnotationLiteral<LoggedBinding>() {
                        }, new AnnotationLiteral<TransactionalBinding>() {
                        }, new AnnotationLiteral<PingBinding>() {
                        }, new AnnotationLiteral<PongBinding>() {
                        }, new BallBindingLiteral(true, true)).size(), 1);

        // Test the set of interceptor bindings
        assertNotNull(messageService);
        ComplicatedInterceptor.reset();
        messageService.ping();
        assertTrue(ComplicatedInterceptor.intercepted);

        assertNotNull(monitorService);
        ComplicatedInterceptor.reset();
        monitorService.ping();
        assertFalse(ComplicatedInterceptor.intercepted);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "3.4", id = "cb")
    public void testTimeoutMethodInterceptorBindings(MessageService messageService) throws Exception {

        assertNotNull(messageService);
        ComplicatedInterceptor.reset();
        messageService.start();
        // Timeout is async
        StopCondition condition = new StopCondition() {
            public boolean isSatisfied() {
                return ComplicatedInterceptor.intercepted;
            }
        };
        Timer timer = new Timer().addStopCondition(condition).start();
        assertTrue(timer.isStopConditionsSatisfiedBeforeTimeout() || condition.isSatisfied());
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.4", id = "b") })
    public void testLifecycleInterceptorBindings() throws Exception {

        // Test interceptor is resolved (note non-binding member of BallBinding)
        assertEquals(
                getCurrentManager().resolveInterceptors(InterceptionType.POST_CONSTRUCT,
                        new AnnotationLiteral<MessageBinding>() {
                        }, new AnnotationLiteral<LoggedBinding>() {
                        }, new AnnotationLiteral<TransactionalBinding>() {
                        }, new BasketBindingLiteral(true, true)).size(), 1);
        assertEquals(
                getCurrentManager().resolveInterceptors(InterceptionType.PRE_DESTROY, new AnnotationLiteral<MessageBinding>() {
                }, new AnnotationLiteral<LoggedBinding>() {
                }, new AnnotationLiteral<TransactionalBinding>() {
                }, new BasketBindingLiteral(true, true)).size(), 1);

        // Test the set of interceptor bindings
        ComplicatedLifecycleInterceptor.reset();

        Bean<RemoteMessageService> bean = getUniqueBean(RemoteMessageService.class);
        CreationalContext<RemoteMessageService> ctx = getCurrentManager().createCreationalContext(bean);
        RemoteMessageService remoteMessageService = bean.create(ctx);
        remoteMessageService.ping();
        bean.destroy(remoteMessageService, ctx);

        assertTrue(ComplicatedLifecycleInterceptor.postConstructCalled);
        assertTrue(ComplicatedLifecycleInterceptor.preDestroyCalled);
    }

}
