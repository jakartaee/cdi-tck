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

package org.jboss.cdi.tck.tests.interceptors.definition.inheritance.resolution;

import static org.jboss.cdi.tck.TestGroups.INTERCEPTORS_SPEC;
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
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Interceptor resolution test.
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class InterceptorBindingResolutionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InterceptorBindingResolutionTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors()
                                .clazz(ComplicatedInterceptor.class.getName(), ComplicatedLifecycleInterceptor.class.getName())
                                .up()).build();
    }

    /**
     * Interceptor bindings include the interceptor bindings declared or inherited by the bean at the class level, including,
     * recursively, interceptor bindings declared as meta-annotations of other interceptor bindings and stereotypes, together
     * with all interceptor bindings declared at the method level, including, recursively, interceptor bindings declared as
     * meta-annotations of other interceptor bindings.
     *
     * @param messageService
     * @param monitorService
     */
    @SuppressWarnings("serial")
    @Test(groups = INTERCEPTORS_SPEC, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    // @SpecAssertion(section = INTERCEPTOR_RESOLUTION, id = "ba")
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

    /**
     * For a lifecycle callback method, the interceptor bindings include the interceptor bindings declared or inherited by the
     * bean at the class level, including, recursively, interceptor bindings declared as meta-annotations of other interceptor
     * bindings and stereotypes.
     */
    @SuppressWarnings("serial")
    @Test(groups = INTERCEPTORS_SPEC)
    // @SpecAssertion(section = INTERCEPTOR_RESOLUTION, id = "a")
    public void testLifecycleInterceptorBindings() {

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
