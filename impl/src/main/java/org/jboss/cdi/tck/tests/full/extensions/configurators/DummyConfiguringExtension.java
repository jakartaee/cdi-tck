/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.configurators;

import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;
import jakarta.enterprise.inject.spi.ProcessInjectionPoint;
import jakarta.enterprise.inject.spi.ProcessObserverMethod;
import jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator;
import jakarta.enterprise.inject.spi.configurator.BeanAttributesConfigurator;
import jakarta.enterprise.inject.spi.configurator.InjectionPointConfigurator;
import jakarta.enterprise.inject.spi.configurator.ObserverMethodConfigurator;

public class DummyConfiguringExtension implements Extension {

    private AtomicBoolean sameATConfiguratorReturned = new AtomicBoolean(false);
    private AtomicBoolean sameIPConfiguratorReturned = new AtomicBoolean(false);
    private AtomicBoolean sameBAConfiguratorReturned = new AtomicBoolean(false);
    private AtomicBoolean sameOMConfiguratorReturned = new AtomicBoolean(false);

    void observesFooPAT(@Observes ProcessAnnotatedType<Foo> event) {
        AnnotatedTypeConfigurator<Foo> annotatedTypeConfigurator = event.configureAnnotatedType();
        annotatedTypeConfigurator.remove(p -> p.annotationType().equals(RequestScoped.class));
        sameATConfiguratorReturned.set(annotatedTypeConfigurator.equals(event.configureAnnotatedType()));
    }

    void observesFooPIP(@Observes ProcessInjectionPoint<Foo, Bar> event) {
        InjectionPointConfigurator injectionPointConfigurator = event.configureInjectionPoint();
        injectionPointConfigurator.transientField(true);
        sameIPConfiguratorReturned.set(injectionPointConfigurator.equals(event.configureInjectionPoint()));
    }

    void observesFooPBA(@Observes ProcessBeanAttributes<Foo> event) {
        BeanAttributesConfigurator<Foo> beanAttributesConfigurator = event.configureBeanAttributes();
        beanAttributesConfigurator.name(Foo.class.getSimpleName());
        sameBAConfiguratorReturned.set(beanAttributesConfigurator.equals(event.configureBeanAttributes()));
    }

    void observesFooPOM(@Observes ProcessObserverMethod<Bar, Foo> event) {
        ObserverMethodConfigurator<Bar> observerMethodConfigurator = event.configureObserverMethod();
        observerMethodConfigurator.priority(1000);
        sameOMConfiguratorReturned.set(observerMethodConfigurator.equals(event.configureObserverMethod()));
    }

    public AtomicBoolean isSameATConfiguratorReturned() {
        return sameATConfiguratorReturned;
    }

    public AtomicBoolean isSameIPConfiguratorReturned() {
        return sameIPConfiguratorReturned;
    }

    public AtomicBoolean isSameBAConfiguratorReturned() {
        return sameBAConfiguratorReturned;
    }

    public AtomicBoolean isSameOMConfiguratorReturned() {
        return sameOMConfiguratorReturned;
    }

}
