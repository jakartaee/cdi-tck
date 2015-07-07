/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015 Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.observer.priority;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessBeanAttributes;
import javax.interceptor.Interceptor;

import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.weld.experimental.Priority;

/**
 * @author Mark Paluch
 * @author Tomas Remes
 */
public class PrioritizedExtensionEvents implements Extension {

    public static final String A = "A";
    public static final String B = "B";
    public static final String C = "C";

    void afterDeploymentValidation(@Observes @Priority(Interceptor.Priority.LIBRARY_BEFORE - 150) AfterDeploymentValidation afterDeploymentValidation) {
        ActionSequence.addAction(AfterDeploymentValidation.class.getSimpleName());
    }

    void processBeanEarly(@Observes @Priority(Interceptor.Priority.LIBRARY_BEFORE) ProcessBean<TestBean> processBean) {
        ActionSequence.addAction(A);
    }

    void processAnnotatedType(@Observes @Priority(Interceptor.Priority.LIBRARY_BEFORE + 100) ProcessAnnotatedType<TestBean> processAnnotatedType) {
        ActionSequence.addAction(ProcessAnnotatedType.class.getSimpleName());

    }

    void processBeanSomewhereInTheMiddle(@Observes ProcessBean<TestBean> processBean) {
        ActionSequence.addAction(B);
    }

    void processBeanLate(@Observes @Priority(Interceptor.Priority.LIBRARY_AFTER) ProcessBean<TestBean> processBean) {
        ActionSequence.addAction(C);
    }

    void processBeanAttributes(@Observes @Priority(Interceptor.Priority.LIBRARY_AFTER + 100) ProcessBeanAttributes<TestBean> processBeanAttributes) {
        ActionSequence.addAction(ProcessBeanAttributes.class.getSimpleName());

    }

    void beforeBeanDiscovery(@Observes @Priority(Interceptor.Priority.LIBRARY_AFTER + 150) BeforeBeanDiscovery beforeBeanDiscovery) {
        ActionSequence.addAction(BeforeBeanDiscovery.class.getSimpleName());
    }

}
