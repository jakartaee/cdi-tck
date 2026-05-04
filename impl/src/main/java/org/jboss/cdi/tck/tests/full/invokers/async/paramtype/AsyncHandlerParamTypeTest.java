/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.full.invokers.async.paramtype;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterDeploymentValidation;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessManagedBean;
import jakarta.enterprise.invoke.AsyncHandler;
import jakarta.enterprise.invoke.Invoker;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
@Test(groups = CDI_FULL)
public class AsyncHandlerParamTypeTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AsyncHandlerParamTypeTest.class)
                .withServiceProvider(AsyncHandler.ParameterType.class, MyAsyncTypeHandler.class)
                .withExtension(TestExtension.class)
                .build();
    }

    public static class TestExtension implements Extension {
        private final Map<String, Invoker<?, ?>> invokers = new HashMap<>();

        public void registration(@Observes ProcessManagedBean<MyBean> pmb) {
            Set<String> methods = Set.of("hello", "helloThrow");
            for (AnnotatedMethod<? super MyBean> method : pmb.getAnnotatedBeanClass().getMethods()) {
                if (methods.contains(method.getJavaMember().getName())) {
                    Invoker<MyBean, ?> invoker = pmb.createInvoker(method)
                            .withInstanceLookup()
                            .withArgumentLookup(0)
                            .build();
                    invokers.put(method.getJavaMember().getName(), invoker);
                }
            }
        }

        public void validation(@Observes AfterDeploymentValidation adv) {
            adv.ensureAsyncHandlerExists(MyAsyncType.class);
        }

        @SuppressWarnings("unchecked")
        public <T, R> Invoker<T, R> getInvoker(String name) {
            return (Invoker<T, R>) invokers.get(name);
        }
    }

    @Test
    @SpecAssertion(section = Sections.USING_INVOKER_BUILDER_FULL, id = "a")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "b")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "g")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "h")
    public void test() throws Exception {
        MyDependentBean.reset();

        Invoker<MyBean, MyAsyncType<String>> hello = getCurrentManager()
                .getExtension(TestExtension.class).getInvoker("hello");
        CompletableFuture<String> future = new CompletableFuture<>();
        MyAsyncType<String> result = MyAsyncType.createSuspended();

        assertEquals(MyDependentBean.destroyedCounter.get(), 0);

        hello.invoke(null, new Object[] { null, future, result });

        assertEquals(MyDependentBean.destroyedCounter.get(), 0);
        assertFalse(result.isComplete());

        future.complete("hello");

        assertEquals(MyDependentBean.destroyedCounter.get(), 1);
        assertTrue(result.isComplete());
        assertEquals(result.getIfComplete(), "hello");
    }

    // this test verifies both that dependent instances created for the invocation are destroyed
    // in case of a synchronous exception and that a "secondary completion" is ignored
    @Test
    @SpecAssertion(section = Sections.USING_INVOKER_BUILDER_FULL, id = "a")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "b")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "i")
    public void testSyncThrow() {
        MyDependentBean.reset();

        Invoker<MyBean, MyAsyncType<String>> helloThrow = getCurrentManager()
                .getExtension(TestExtension.class).getInvoker("helloThrow");
        CompletableFuture<String> future = new CompletableFuture<>();
        MyAsyncType<String> result = MyAsyncType.createSuspended();

        assertEquals(MyDependentBean.destroyedCounter.get(), 0);

        assertThrows(IllegalArgumentException.class, () -> {
            helloThrow.invoke(null, new Object[] { null, future, result });
        });

        assertEquals(MyDependentBean.destroyedCounter.get(), 1);

        // calls the completion callback, which must be a noop
        future.complete("hello");

        assertEquals(MyDependentBean.destroyedCounter.get(), 1);
    }
}
