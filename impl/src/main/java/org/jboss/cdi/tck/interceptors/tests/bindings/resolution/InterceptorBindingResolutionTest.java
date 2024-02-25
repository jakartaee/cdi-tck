/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.interceptors.tests.bindings.resolution;

import static org.jboss.cdi.tck.interceptors.InterceptorsSections.BINDING_INT_TO_COMPONENT;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INT_BINDING_TYPES_WITH_ADDITIONAL_INT_BINDINGS;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INT_RESOLUTION;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INT_WITH_MULTIPLE_BINDINGS;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.OTHER_SOURCES_OF_INT_BINDINGS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InterceptionType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Interceptor resolution test.
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "interceptors", version = "1.2")
public class InterceptorBindingResolutionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InterceptorBindingResolutionTest.class)
                .build();
    }

    @SuppressWarnings("serial")
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = BINDING_INT_TO_COMPONENT, id = "e")
    @SpecAssertion(section = INT_RESOLUTION, id = "ca")
    @SpecAssertion(section = INT_RESOLUTION, id = "da")
    @SpecAssertion(section = INT_RESOLUTION, id = "db")
    @SpecAssertion(section = INT_RESOLUTION, id = "dc")
    @SpecAssertion(section = OTHER_SOURCES_OF_INT_BINDINGS, id = "a")
    @SpecAssertion(section = INT_BINDING_TYPES_WITH_ADDITIONAL_INT_BINDINGS, id = "a")
    @SpecAssertion(section = INT_BINDING_TYPES_WITH_ADDITIONAL_INT_BINDINGS, id = "b")
    public void testBusinessMethodInterceptorBindings(MessageService messageService, MonitorService monitorService) {

        // Test interceptor is resolved (note non-binding member of BallBinding)
        assertEquals(
                getCurrentManager().resolveInterceptors(InterceptionType.AROUND_INVOKE,
                        new MessageBinding.Literal(),
                        new LoggedBinding.Literal(),
                        new TransactionalBinding.Literal(),
                        new PingBinding.Literal(),
                        new PongBinding.Literal(),
                        new BallBindingLiteral(true, true)).size(),
                1);

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

    @SuppressWarnings("serial")
    @Test
    @SpecAssertion(section = INT_RESOLUTION, id = "b")
    @SpecAssertion(section = INT_WITH_MULTIPLE_BINDINGS, id = "a")
    public void testLifecycleInterceptorBindings() {

        // Test interceptor is resolved (note non-binding member of BallBinding)
        assertEquals(
                getCurrentManager().resolveInterceptors(InterceptionType.POST_CONSTRUCT,
                        new MessageBinding.Literal(),
                        new LoggedBinding.Literal(),
                        new TransactionalBinding.Literal(),
                        new BasketBindingLiteral(true, true)).size(),
                1);
        assertEquals(
                getCurrentManager().resolveInterceptors(InterceptionType.PRE_DESTROY,
                        new MessageBinding.Literal(),
                        new LoggedBinding.Literal(),
                        new TransactionalBinding.Literal(),
                        new BasketBindingLiteral(true, true)).size(),
                1);

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

    @SuppressWarnings("serial")
    @Test
    @SpecAssertion(section = INT_RESOLUTION, id = "b")
    @SpecAssertion(section = INT_RESOLUTION, id = "cc")
    public void testConstructorInterceptorBindings() {

        // Test interceptor is resolved
        assertEquals(
                getCurrentManager().resolveInterceptors(InterceptionType.AROUND_CONSTRUCT,
                        new MachineBinding.Literal(),
                        new LoggedBinding.Literal(),
                        new TransactionalBinding.Literal(),
                        new ConstructorBinding.Literal(),
                        new CreativeBinding.Literal()).size(),
                1);

        // Test the set of interceptor bindings
        ComplicatedAroundConstructInterceptor.reset();

        Bean<MachineService> bean = getUniqueBean(MachineService.class);
        CreationalContext<MachineService> ctx = getCurrentManager().createCreationalContext(bean);
        bean.create(ctx);

        assertTrue(ComplicatedAroundConstructInterceptor.aroundConstructCalled);
    }
}
