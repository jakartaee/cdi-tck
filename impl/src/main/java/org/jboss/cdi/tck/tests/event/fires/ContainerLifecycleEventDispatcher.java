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
package org.jboss.cdi.tck.tests.event.fires;

import static org.jboss.cdi.tck.tests.full.extensions.beanManager.broken.event.ContainerLifecycleEvents.AFTER_BEAN_DISCOVERY;
import static org.jboss.cdi.tck.tests.full.extensions.beanManager.broken.event.ContainerLifecycleEvents.AFTER_DEPLOYMENT_VALIDATION;
import static org.jboss.cdi.tck.tests.full.extensions.beanManager.broken.event.ContainerLifecycleEvents.BEFORE_BEAN_DISCOVERY;
import static org.jboss.cdi.tck.tests.full.extensions.beanManager.broken.event.ContainerLifecycleEvents.BEFORE_SHUTDOWN;
import static org.jboss.cdi.tck.tests.full.extensions.beanManager.broken.event.ContainerLifecycleEvents.PROCESS_ANNOTATED_TYPE;
import static org.jboss.cdi.tck.tests.full.extensions.beanManager.broken.event.ContainerLifecycleEvents.PROCESS_BEAN;
import static org.jboss.cdi.tck.tests.full.extensions.beanManager.broken.event.ContainerLifecycleEvents.PROCESS_BEAN_ATTRIBUTES;
import static org.jboss.cdi.tck.tests.full.extensions.beanManager.broken.event.ContainerLifecycleEvents.PROCESS_INJECTION_POINT;
import static org.jboss.cdi.tck.tests.full.extensions.beanManager.broken.event.ContainerLifecycleEvents.PROCESS_INJECTION_TARGET;
import static org.jboss.cdi.tck.tests.full.extensions.beanManager.broken.event.ContainerLifecycleEvents.PROCESS_OBSERVER_METHOD;
import static org.jboss.cdi.tck.tests.full.extensions.beanManager.broken.event.ContainerLifecycleEvents.PROCESS_PRODUCER;
import static org.jboss.cdi.tck.tests.full.extensions.beanManager.broken.event.ContainerLifecycleEvents.PROCESS_PRODUCER_FIELD;
import static org.jboss.cdi.tck.tests.full.extensions.beanManager.broken.event.ContainerLifecycleEvents.PROCESS_PRODUCER_METHOD;
import static org.jboss.cdi.tck.tests.full.extensions.beanManager.broken.event.ContainerLifecycleEvents.PROCESS_SESSION_BEAN;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.AfterDeploymentValidation;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.BeforeShutdown;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.ProcessBean;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;
import jakarta.enterprise.inject.spi.ProcessInjectionPoint;
import jakarta.enterprise.inject.spi.ProcessInjectionTarget;
import jakarta.enterprise.inject.spi.ProcessObserverMethod;
import jakarta.enterprise.inject.spi.ProcessProducer;
import jakarta.enterprise.inject.spi.ProcessProducerField;
import jakarta.enterprise.inject.spi.ProcessProducerMethod;
import jakarta.enterprise.inject.spi.ProcessSessionBean;
import jakarta.inject.Inject;

@RequestScoped
public class ContainerLifecycleEventDispatcher {

    @Inject
    Event<Object> event;

    public void fireContainerLifecycleEvents() {
        tryFire(AfterBeanDiscovery.class, AFTER_BEAN_DISCOVERY);
        tryFire(AfterDeploymentValidation.class, AFTER_DEPLOYMENT_VALIDATION);
        tryFire(BeforeShutdown.class, BEFORE_SHUTDOWN);
        tryFire(ProcessAnnotatedType.class, PROCESS_ANNOTATED_TYPE);
        tryFire(ProcessInjectionPoint.class, PROCESS_INJECTION_POINT);
        tryFire(ProcessInjectionTarget.class, PROCESS_INJECTION_TARGET);
        tryFire(ProcessProducer.class, PROCESS_PRODUCER);
        tryFire(ProcessBeanAttributes.class, PROCESS_BEAN_ATTRIBUTES);
        tryFire(ProcessBean.class, PROCESS_BEAN);
        tryFire(ProcessObserverMethod.class, PROCESS_OBSERVER_METHOD);
        tryFire(ProcessSessionBean.class, PROCESS_SESSION_BEAN);
        tryFire(ProcessProducerField.class, PROCESS_PRODUCER_FIELD);
        tryFire(ProcessProducerMethod.class, PROCESS_PRODUCER_METHOD);
        tryFire(BeforeBeanDiscovery.class, BEFORE_BEAN_DISCOVERY);
    }

    private <T> void tryFire(Class<T> clazz, T payload) {
        try {
            event.select(clazz).fire(payload);
            throw new IllegalStateException("Expected exception (IllegalArgumentException) not thrown");
        } catch (IllegalArgumentException expected) {
            expected.getCause();
        }
    }
}
