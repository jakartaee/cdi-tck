/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.invokers.lookup.dependent.async.paramtype;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.InvokerFactory;
import jakarta.enterprise.inject.build.compatible.spi.InvokerValidation;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.inject.build.compatible.spi.Validation;
import jakarta.enterprise.invoke.AsyncHandler;
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
public class AsyncHandlerParamTypeTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AsyncHandlerParamTypeTest.class)
                .withClasses(InvokerHolder.class, InvokerHolderCreator.class, InvokerHolderExtensionBase.class)
                .withServiceProvider(AsyncHandler.ParameterType.class, MyAsyncTypeHandler.class)
                .withBuildCompatibleExtension(TestExtension.class)
                .build();
    }

    public static class TestExtension extends InvokerHolderExtensionBase implements BuildCompatibleExtension {
        @Registration(types = MyBean.class)
        public void registration(BeanInfo bean, InvokerFactory invokers) {
            registerInvokers(bean, invokers, Set.of("helloSync", "helloAsync", "helloThrow"), builder -> {
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
            invokers.ensureAsyncHandlerExists(MyAsyncType.class);
        }
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "cf")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "ha")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "i")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "j")
    public void testSync(InvokerHolder invokers) throws Exception {
        MyDependentBean.reset();

        Invoker<MyBean, MyAsyncType<String>> hello = invokers.get("helloSync");
        MyAsyncType<String> result = MyAsyncType.createSuspended();

        assertEquals(MyDependentBean.destroyedCounter.get(), 0);

        hello.invoke(null, new Object[] { null, result });

        assertEquals(MyDependentBean.destroyedCounter.get(), 1);
        assertTrue(result.isComplete());
        assertEquals(result.getIfComplete(), "hello");
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "cf")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "hb")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "i")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "j")
    public void testAsync(InvokerHolder invokers) throws Exception {
        MyDependentBean.reset();

        Invoker<MyBean, MyAsyncType<String>> hello = invokers.get("helloAsync");
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
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "cf")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "hc")
    @SpecAssertion(section = Sections.INVOKER_ASYNCHRONOUS_METHODS, id = "k")
    public void testSyncThrow(InvokerHolder invokers) {
        MyDependentBean.reset();

        Invoker<MyBean, MyAsyncType<String>> helloThrow = invokers.get("helloThrow");
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
