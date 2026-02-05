/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.build.compatible.extensions.registration;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Messages;
import jakarta.enterprise.inject.build.compatible.spi.ObserverInfo;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.build.compatible.spi.Types;
import jakarta.enterprise.inject.build.compatible.spi.Validation;
import jakarta.enterprise.util.TypeLiteral;

public class RegistrationExtension implements BuildCompatibleExtension {
    private final AtomicInteger beanCounter = new AtomicInteger();
    private final AtomicInteger genericBeanCounter = new AtomicInteger();
    private final AtomicInteger beanMyQualifierCounter = new AtomicInteger();
    private final AtomicInteger observerCounter = new AtomicInteger();
    private final AtomicInteger genericObserverCounter = new AtomicInteger();
    private final AtomicInteger interceptorCounter = new AtomicInteger();

    @Registration(types = MyService.class)
    public void beans(BeanInfo bean) {
        beanCounter.incrementAndGet();

        if (bean.qualifiers().stream().anyMatch(it -> it.name().equals(MyQualifier.class.getName()))) {
            beanMyQualifierCounter.incrementAndGet();
        }
    }

    @Registration(types = MyGenericServiceOfString.class)
    public void genericBeans1(BeanInfo bean) {
        genericBeanCounter.incrementAndGet();
    }

    @Registration(types = MyGenericServiceOfT.class)
    public void genericBeans2(BeanInfo bean) {
        genericBeanCounter.incrementAndGet();
    }

    static class MyGenericServiceOfString extends TypeLiteral<MyGenericService<String>> {
    }

    static class MyGenericServiceOfT<T> extends TypeLiteral<MyGenericService<T>> {
    }

    @Registration(types = Object.class)
    public void observers(ObserverInfo observer, Types types) {
        if (observer.declaringClass().superInterfaces().contains(types.of(MyService.class))) {
            observerCounter.incrementAndGet();
        }
    }

    @Registration(types = CollectionOfString.class)
    public void genericObservers1(ObserverInfo observer) {
        genericObserverCounter.incrementAndGet();
    }

    @Registration(types = ListOfString.class)
    public void genericObservers2(ObserverInfo observer) {
        genericObserverCounter.incrementAndGet();
    }

    static class CollectionOfString extends TypeLiteral<Collection<String>> {
    }

    static class ListOfString extends TypeLiteral<List<String>> {
    }

    @Registration(types = MyInterceptor.class)
    public void interceptors(BeanInfo interceptor, Messages msg) {
        if (!interceptor.isInterceptor()) {
            msg.error("Interceptor expected", interceptor);
        }
        interceptorCounter.incrementAndGet();
    }

    @Validation
    public void test(Messages msg) {
        if (beanCounter.get() != 2) {
            msg.error("Should see 2 beans of type MyService");
        }

        if (genericBeanCounter.get() != 3) {
            msg.error("Should see 3 beans of type MyGenericService");
        }

        if (beanMyQualifierCounter.get() != 1) {
            msg.error("Should see 1 bean of type MyService with qualifier MyQualifier");
        }

        if (observerCounter.get() != 1) {
            msg.error("Should see 1 observer declared in class that implements MyService");
        }

        if (genericObserverCounter.get() != 2) {
            msg.error("Should see 1 observer whose event type is assignable to Collection<String> and List<String>");
        }

        if (interceptorCounter.get() != 1) {
            msg.error("Should see 1 interceptor of type MyInterceptor");
        }
    }
}
