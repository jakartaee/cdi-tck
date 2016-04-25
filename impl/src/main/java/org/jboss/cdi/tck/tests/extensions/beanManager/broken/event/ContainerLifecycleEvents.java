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
package org.jboss.cdi.tck.tests.extensions.beanManager.broken.event;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.Context;
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
import javax.enterprise.inject.spi.ProcessObserverMethod;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.inject.spi.ProcessProducerField;
import javax.enterprise.inject.spi.ProcessProducerMethod;
import javax.enterprise.inject.spi.ProcessSessionBean;
import javax.enterprise.inject.spi.Producer;
import javax.enterprise.inject.spi.SessionBeanType;
import javax.enterprise.inject.spi.builder.AnnotatedTypeConfigurator;
import javax.enterprise.inject.spi.builder.BeanAttributesConfigurator;
import javax.enterprise.inject.spi.builder.BeanConfigurator;
import javax.enterprise.inject.spi.builder.InjectionPointConfigurator;
import javax.enterprise.inject.spi.builder.ObserverMethodConfigurator;

public class ContainerLifecycleEvents {

    private ContainerLifecycleEvents() {
    }

    public static final Collection<Object> CONTAINER_LIFECYCLE_EVENTS;

    public static final BeforeBeanDiscovery BEFORE_BEAN_DISCOVERY = new BeforeBeanDiscovery() {
        @Override
        public <T> AnnotatedTypeConfigurator<T> addAnnotatedType(String s, Class<T> aClass) {
            return null;
        }

        @Override
        public void addStereotype(Class<? extends Annotation> stereotype, Annotation... stereotypeDef) {
        }

        @Override
        public void addScope(Class<? extends Annotation> scopeType, boolean normal, boolean passivating) {
        }

        @Override
        public void addQualifier(Class<? extends Annotation> qualifier) {
        }

        @Override
        public void addInterceptorBinding(Class<? extends Annotation> bindingType, Annotation... bindingTypeDef) {
        }

        @Override
        public void addAnnotatedType(AnnotatedType<?> type) {
        }

        @Override
        public void addQualifier(AnnotatedType<? extends Annotation> qualifier) {
        }

        @Override
        public void addInterceptorBinding(AnnotatedType<? extends Annotation> bindingType) {
        }

        @Override
        public void addAnnotatedType(AnnotatedType<?> type, String id) {
        }

    };

    public static final AfterBeanDiscovery AFTER_BEAN_DISCOVERY = new AfterBeanDiscovery() {
        @Override
        public void addObserverMethod(ObserverMethod<?> observerMethod) {
        }

        @Override
        public ObserverMethodConfigurator<?> addObserverMethod() {
            return null;
        }

        @Override
        public void addDefinitionError(Throwable t) {
        }

        @Override
        public void addContext(Context context) {
        }

        @Override
        public void addBean(Bean<?> bean) {
        }

        @Override
        public BeanConfigurator<?> addBean() {
            return null;
        }

        @Override
        public <T> AnnotatedType<T> getAnnotatedType(Class<T> type, String id) {
            return null;
        }

        @Override
        public <T> Iterable<AnnotatedType<T>> getAnnotatedTypes(Class<T> type) {
            return null;
        }
    };

    public static final AfterDeploymentValidation AFTER_DEPLOYMENT_VALIDATION = new AfterDeploymentValidation() {
        @Override
        public void addDeploymentProblem(Throwable t) {
        }
    };

    public static final BeforeShutdown BEFORE_SHUTDOWN = new BeforeShutdown() {
    };

    public static final ProcessAnnotatedType<Integer> PROCESS_ANNOTATED_TYPE = new ProcessAnnotatedType<Integer>() {
        @Override
        public AnnotatedTypeConfigurator<Integer> configureAnnotatedType() {
            return null;
        }

        @Override
        public AnnotatedType<Integer> getAnnotatedType() {
            return null;
        }

        @Override
        public void setAnnotatedType(AnnotatedType<Integer> type) {
        }

        @Override
        public void veto() {
        }
    };

    public static final ProcessInjectionPoint<String, Number> PROCESS_INJECTION_POINT = new ProcessInjectionPoint<String, Number>() {
        @Override
        public InjectionPointConfigurator configureInjectionPoint() {
            return null;
        }

        @Override
        public InjectionPoint getInjectionPoint() {
            return null;
        }

        @Override
        public void setInjectionPoint(InjectionPoint injectionPoint) {
        }

        @Override
        public void addDefinitionError(Throwable t) {
        }
    };

    public static final ProcessInjectionTarget<Double> PROCESS_INJECTION_TARGET = new ProcessInjectionTarget<Double>() {
        @Override
        public AnnotatedType<Double> getAnnotatedType() {
            return null;
        }

        @Override
        public InjectionTarget<Double> getInjectionTarget() {
            return null;
        }

        @Override
        public void setInjectionTarget(InjectionTarget<Double> injectionTarget) {
        }

        @Override
        public void addDefinitionError(Throwable t) {
        }
    };

    public static final ProcessProducer<Number, String> PROCESS_PRODUCER = new ProcessProducer<Number, String>() {
        @Override
        public AnnotatedMember<Number> getAnnotatedMember() {
            return null;
        }

        @Override
        public Producer<String> getProducer() {
            return null;
        }

        @Override
        public void setProducer(Producer<String> producer) {
        }

        @Override
        public void addDefinitionError(Throwable t) {
        }
    };

    public static final ProcessBeanAttributes<Exception> PROCESS_BEAN_ATTRIBUTES = new ProcessBeanAttributes<Exception>() {
        @Override
        public Annotated getAnnotated() {
            return null;
        }

        @Override
        public BeanAttributes<Exception> getBeanAttributes() {
            return null;
        }

        @Override
        public void setBeanAttributes(BeanAttributes<Exception> beanAttributes) {
        }

        @Override
        public BeanAttributesConfigurator<Exception> configureBeanAttributes() {
            return null;
        }

        @Override
        public void addDefinitionError(Throwable t) {
        }

        @Override
        public void veto() {
        }
    };

    public static final ProcessBean<Set<Void>> PROCESS_BEAN = new ProcessBean<Set<Void>>() {

        @Override
        public Annotated getAnnotated() {
            return null;
        }

        @Override
        public Bean<Set<Void>> getBean() {
            return null;
        }

        @Override
        public void addDefinitionError(Throwable t) {
        }
    };

    public static final ProcessObserverMethod<String, String> PROCESS_OBSERVER_METHOD = new ProcessObserverMethod<String, String>() {
        @Override
        public ObserverMethodConfigurator<String> configureObserverMethod() {
            return null;
        }

        @Override
        public AnnotatedMethod<String> getAnnotatedMethod() {
            return null;
        }

        @Override
        public ObserverMethod<String> getObserverMethod() {
            return null;
        }

        @Override
        public void addDefinitionError(Throwable t) {
        }

        //@Override
        public void setObserverMethod(ObserverMethod<String> observerMethod) {

        }

        //@Override
        public void veto() {

        }
    };

    public static final ProcessSessionBean<Object> PROCESS_SESSION_BEAN = new ProcessSessionBean<Object>() {

        @Override
        public AnnotatedType<Object> getAnnotatedBeanClass() {
            return null;
        }

        @Override
        public Annotated getAnnotated() {
            return null;
        }

        @Override
        public Bean<Object> getBean() {
            return null;
        }

        @Override
        public void addDefinitionError(Throwable t) {
        }

        @Override
        public String getEjbName() {
            return null;
        }

        @Override
        public SessionBeanType getSessionBeanType() {
            return null;
        }
    };

    public static final ProcessProducerField<Object, Object> PROCESS_PRODUCER_FIELD = new ProcessProducerField<Object, Object>() {

        @Override
        public Annotated getAnnotated() {
            return null;
        }

        @Override
        public Bean<Object> getBean() {
            return null;
        }

        @Override
        public void addDefinitionError(Throwable t) {
        }

        @Override
        public AnnotatedField<Object> getAnnotatedProducerField() {
            return null;
        }

        @Override
        public AnnotatedParameter<Object> getAnnotatedDisposedParameter() {
            return null;
        }
    };

    public static final ProcessProducerMethod<Object, Object> PROCESS_PRODUCER_METHOD = new ProcessProducerMethod<Object, Object>() {

        @Override
        public Annotated getAnnotated() {
            return null;
        }

        @Override
        public Bean<Object> getBean() {
            return null;
        }

        @Override
        public void addDefinitionError(Throwable t) {
        }

        @Override
        public AnnotatedMethod<Object> getAnnotatedProducerMethod() {
            return null;
        }

        @Override
        public AnnotatedParameter<Object> getAnnotatedDisposedParameter() {
            return null;
        }
    };

    static {
        List<Object> values = new LinkedList<Object>();
        values.add(AFTER_BEAN_DISCOVERY);
        values.add(AFTER_DEPLOYMENT_VALIDATION);
        values.add(BEFORE_SHUTDOWN);
        values.add(PROCESS_ANNOTATED_TYPE);
        values.add(PROCESS_INJECTION_POINT);
        values.add(PROCESS_INJECTION_TARGET);
        values.add(PROCESS_PRODUCER);
        values.add(PROCESS_BEAN_ATTRIBUTES);
        values.add(PROCESS_BEAN);
        values.add(PROCESS_OBSERVER_METHOD);
        values.add(PROCESS_SESSION_BEAN);
        values.add(PROCESS_PRODUCER_FIELD);
        values.add(PROCESS_PRODUCER_METHOD);
        values.add(BEFORE_BEAN_DISCOVERY);
        CONTAINER_LIFECYCLE_EVENTS = Collections.unmodifiableList(values);

    }
}
