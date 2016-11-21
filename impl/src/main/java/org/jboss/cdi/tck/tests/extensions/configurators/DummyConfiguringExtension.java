/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.configurators;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBeanAttributes;
import javax.enterprise.inject.spi.ProcessInjectionPoint;
import javax.enterprise.inject.spi.ProcessObserverMethod;
import javax.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator;
import javax.enterprise.inject.spi.configurator.BeanAttributesConfigurator;
import javax.enterprise.inject.spi.configurator.InjectionPointConfigurator;
import javax.enterprise.inject.spi.configurator.ObserverMethodConfigurator;

public class DummyConfiguringExtension implements Extension {

    private AtomicBoolean sameATConfiguratorReturned = new AtomicBoolean(false);
    private AtomicBoolean sameIPConfiguratorReturned = new AtomicBoolean(false);
    private AtomicBoolean sameBAConfiguratorReturned = new AtomicBoolean(false);
    private AtomicBoolean sameOMConfiguratorReturned = new AtomicBoolean(false);

    void observesFooPAT(@Observes ProcessAnnotatedType<Foo> event) {
        AnnotatedTypeConfigurator<Foo> annotatedTypeConfigurator = event.configureAnnotatedType();
        annotatedTypeConfigurator.remove(RequestScoped.class);
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

    void observesFooPOM(@Observes ProcessObserverMethod<Bar, Foo> event){
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
