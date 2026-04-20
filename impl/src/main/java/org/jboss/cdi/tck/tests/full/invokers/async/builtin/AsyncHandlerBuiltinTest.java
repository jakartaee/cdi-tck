/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.full.invokers.async.builtin;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterDeploymentValidation;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessManagedBean;
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
public class AsyncHandlerBuiltinTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AsyncHandlerBuiltinTest.class)
                .withExtension(TestExtension.class)
                .build();
    }

    public static class TestExtension implements Extension {
        private final Map<String, Invoker<?, ?>> invokers = new HashMap<>();

        public void registration(@Observes ProcessManagedBean<MyBean> pmb) {
            Set<String> methods = Set.of("helloCS", "helloCF", "helloFP");
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
            adv.ensureAsyncHandlerExists(CompletionStage.class);
            adv.ensureAsyncHandlerExists(CompletableFuture.class);
            adv.ensureAsyncHandlerExists(Flow.Publisher.class);
        }

        @SuppressWarnings("unchecked")
        public <T, R> Invoker<T, R> getInvoker(String name) {
            return (Invoker<T, R>) invokers.get(name);
        }
    }

    @Test
    @SpecAssertion(section = Sections.USING_INVOKER_BUILDER_FULL, id = "a")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "ja")
    public void testCompletionStage() throws Exception {
        MyDependentBean.reset();

        Invoker<MyBean, CompletionStage<String>> hello = getCurrentManager()
                .getExtension(TestExtension.class).getInvoker("helloCS");
        CompletableFuture<String> future = new CompletableFuture<>();

        assertEquals(MyDependentBean.destroyedCounter.get(), 0);

        CompletionStage<String> result = hello.invoke(null, new Object[] { null, future });

        assertEquals(MyDependentBean.destroyedCounter.get(), 0);
        assertFalse(result.toCompletableFuture().isDone());

        future.complete("hello");

        assertEquals(MyDependentBean.destroyedCounter.get(), 1);
        assertTrue(result.toCompletableFuture().isDone());
        assertEquals(result.toCompletableFuture().getNow(null), "hello");
    }

    @Test
    @SpecAssertion(section = Sections.USING_INVOKER_BUILDER_FULL, id = "a")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "jb")
    public void testCompletableFuture() throws Exception {
        MyDependentBean.reset();

        Invoker<MyBean, CompletableFuture<String>> hello = getCurrentManager()
                .getExtension(TestExtension.class).getInvoker("helloCF");
        CompletableFuture<String> future = new CompletableFuture<>();

        assertEquals(MyDependentBean.destroyedCounter.get(), 0);

        CompletableFuture<String> result = hello.invoke(null, new Object[] { null, future });

        assertEquals(MyDependentBean.destroyedCounter.get(), 0);
        assertFalse(result.isDone());

        future.complete("hello");

        assertEquals(MyDependentBean.destroyedCounter.get(), 1);
        assertTrue(result.isDone());
        assertEquals(result.getNow(null), "hello");
    }

    @Test
    @SpecAssertion(section = Sections.USING_INVOKER_BUILDER_FULL, id = "a")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "jc")
    public void testFlowPublisher() throws Exception {
        MyDependentBean.reset();

        Invoker<MyBean, Flow.Publisher<String>> hello = getCurrentManager()
                .getExtension(TestExtension.class).getInvoker("helloFP");
        CompletableFuture<String> future = new CompletableFuture<>();

        assertEquals(MyDependentBean.destroyedCounter.get(), 0);

        Flow.Publisher<String> result = hello.invoke(null, new Object[] { null, future });

        AtomicReference<String> value = new AtomicReference<>();
        AtomicReference<Throwable> error = new AtomicReference<>();
        AtomicBoolean done = new AtomicBoolean(false);

        result.subscribe(new Flow.Subscriber<>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                subscription.request(1);
            }

            @Override
            public void onNext(String item) {
                value.set(item);
            }

            @Override
            public void onError(Throwable throwable) {
                error.set(throwable);
            }

            @Override
            public void onComplete() {
                done.set(true);
            }
        });

        assertEquals(MyDependentBean.destroyedCounter.get(), 0);
        assertNull(value.get());
        assertNull(error.get());
        assertFalse(done.get());

        future.complete("hello");

        assertEquals(MyDependentBean.destroyedCounter.get(), 1);
        assertEquals(value.get(), "hello");
        assertNull(error.get());
        assertTrue(done.get());
    }
}
