/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.invokers.lookup.dependent.async.builtin;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.InvokerFactory;
import jakarta.enterprise.inject.build.compatible.spi.InvokerValidation;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.inject.build.compatible.spi.Validation;
import jakarta.enterprise.invoke.Invoker;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.invokers.InvokerHolder;
import org.jboss.cdi.tck.tests.invokers.InvokerHolderCreator;
import org.jboss.cdi.tck.tests.invokers.InvokerHolderExtensionBase;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class AsyncHandlerBuiltinTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AsyncHandlerBuiltinTest.class)
                .withClasses(InvokerHolder.class, InvokerHolderCreator.class, InvokerHolderExtensionBase.class)
                .withBuildCompatibleExtension(TestExtension.class)
                .build();
    }

    public static class TestExtension extends InvokerHolderExtensionBase implements BuildCompatibleExtension {
        @Registration(types = MyBean.class)
        public void registration(BeanInfo bean, InvokerFactory invokers) {
            registerInvokers(bean, invokers, Set.of("helloCS", "helloCF", "helloFP"), builder -> {
                builder.withInstanceLookup();
                builder.withArgumentLookup(0);
            });
        }

        @Synthesis
        public void synthesis(SyntheticComponents syn) {
            synthesizeInvokerHolder(syn);
        }

        @Validation
        public void validation(InvokerValidation invokers) {
            invokers.ensureAsyncHandlerExists(CompletionStage.class);
            invokers.ensureAsyncHandlerExists(CompletableFuture.class);
            invokers.ensureAsyncHandlerExists(Flow.Publisher.class);
        }
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "ja")
    public void testCompletionStage(InvokerHolder invokers) throws Exception {
        MyDependentBean.reset();

        Invoker<MyBean, CompletionStage<String>> hello = invokers.get("helloCS");
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

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "jb")
    public void testCompletableFuture(InvokerHolder invokers) throws Exception {
        MyDependentBean.reset();

        Invoker<MyBean, CompletableFuture<String>> hello = invokers.get("helloCF");
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

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "jc")
    public void testFlowPublisher(InvokerHolder invokers) throws Exception {
        MyDependentBean.reset();

        Invoker<MyBean, Flow.Publisher<String>> hello = invokers.get("helloFP");
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
