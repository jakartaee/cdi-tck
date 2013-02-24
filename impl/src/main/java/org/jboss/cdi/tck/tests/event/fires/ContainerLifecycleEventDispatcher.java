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

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.event.Event;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMember;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanAttributes;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessBeanAttributes;
import javax.enterprise.inject.spi.ProcessInjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.inject.spi.ProcessModule;
import javax.enterprise.inject.spi.ProcessObserverMethod;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.inject.spi.ProcessProducerField;
import javax.enterprise.inject.spi.ProcessProducerMethod;
import javax.enterprise.inject.spi.ProcessSessionBean;
import javax.enterprise.inject.spi.Producer;
import javax.enterprise.inject.spi.SessionBeanType;
import javax.inject.Inject;
import static org.jboss.cdi.tck.tests.extensions.beanManager.broken.event.ContainerLifecycleEvents.*;

@RequestScoped
public class ContainerLifecycleEventDispatcher {

    @Inject
    Event<Object> event;

    public void fireContainerLifecycleEvents() {
        tryFire(AfterBeanDiscovery.class, AFTER_BEAN_DISCOVERY);
        tryFire(AfterDeploymentValidation.class, AFTER_DEPLOYMENT_VALIDATION);
        tryFire(BeforeShutdown.class, BEFORE_SHUTDOWN);
        tryFire(ProcessModule.class, PROCESS_MODULE);
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
