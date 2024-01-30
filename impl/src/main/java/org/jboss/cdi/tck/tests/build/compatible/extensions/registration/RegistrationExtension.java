/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.build.compatible.extensions.registration;

import jakarta.enterprise.inject.build.compatible.spi.BeanInfo;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Messages;
import jakarta.enterprise.inject.build.compatible.spi.ObserverInfo;
import jakarta.enterprise.inject.build.compatible.spi.Registration;
import jakarta.enterprise.inject.build.compatible.spi.Types;
import jakarta.enterprise.inject.build.compatible.spi.Validation;

import java.util.concurrent.atomic.AtomicInteger;

public class RegistrationExtension implements BuildCompatibleExtension {
    private final AtomicInteger beanCounter = new AtomicInteger();
    private final AtomicInteger beanMyQualifierCounter = new AtomicInteger();
    private final AtomicInteger observerCounter = new AtomicInteger();
    private final AtomicInteger interceptorCounter = new AtomicInteger();

    @Registration(types = MyService.class)
    public void beans(BeanInfo bean) {
        beanCounter.incrementAndGet();

        if (bean.qualifiers().stream().anyMatch(it -> it.name().equals(MyQualifier.class.getName()))) {
            beanMyQualifierCounter.incrementAndGet();
        }
    }

    @Registration(types = Object.class)
    public void observers(ObserverInfo observer, Types types) {
        if (observer.declaringClass().superInterfaces().contains(types.of(MyService.class))) {
            observerCounter.incrementAndGet();
        }
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

        if (beanMyQualifierCounter.get() != 1) {
            msg.error("Should see 1 bean of type MyService with qualifier MyQualifier");
        }

        if (observerCounter.get() != 1) {
            msg.error("Should see 1 observer declared in class that implements MyService");
        }

        if (interceptorCounter.get() != 1) {
            msg.error("Should see 1 interceptor of type MyInterceptor");
        }
    }
}
