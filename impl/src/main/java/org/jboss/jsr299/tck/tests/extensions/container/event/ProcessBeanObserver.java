/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.jsr299.tck.tests.extensions.container.event;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessManagedBean;
import javax.enterprise.inject.spi.ProcessProducerField;
import javax.enterprise.inject.spi.ProcessProducerMethod;
import javax.enterprise.inject.spi.ProcessSessionBean;

public class ProcessBeanObserver implements Extension {
    private static ProcessManagedBean<Farm> processManagedBeanEvent = null;
    private static ProcessSessionBean<Sheep> processStatelessSessionBeanEvent = null;
    private static ProcessSessionBean<Cow> processStatefulSessionBeanEvent = null;
    private static ProcessProducerField<Milk, Farm> processProducerFieldEvent = null;
    private static ProcessProducerMethod<Cheese, Farm> processProducerMethodEvent = null;

    public void cleanup(@Observes BeforeShutdown shutdown) {
        processManagedBeanEvent = null;
        processStatefulSessionBeanEvent = null;
        processStatelessSessionBeanEvent = null;
        processProducerFieldEvent = null;
        processProducerMethodEvent = null;
    }

    public void observeProcessManagedBean(@Observes ProcessManagedBean<Farm> event) {
        processManagedBeanEvent = event;
    }

    public void observeProcessStatelessSessionBean(@Observes ProcessSessionBean<Sheep> event) {
        processStatelessSessionBeanEvent = event;
    }

    public void observeProcessStatefulSessionBean(@Observes ProcessSessionBean<Cow> event) {
        processStatefulSessionBeanEvent = event;
    }

    public void observeProcessProduceField(@Observes ProcessProducerField<Milk, Farm> event) {
        processProducerFieldEvent = event;
    }

    public void observeProcessProduceMethod(@Observes ProcessProducerMethod<Cheese, Farm> event) {
        processProducerMethodEvent = event;
    }

    public static ProcessProducerField<Milk, Farm> getProcessProducerFieldEvent() {
        return processProducerFieldEvent;
    }

    public static ProcessManagedBean<Farm> getProcessManagedBeanEvent() {
        return processManagedBeanEvent;
    }

    public static ProcessSessionBean<Sheep> getProcessStatelessSessionBeanEvent() {
        return processStatelessSessionBeanEvent;
    }

    public static ProcessSessionBean<Cow> getProcessStatefulSessionBeanEvent() {
        return processStatefulSessionBeanEvent;
    }

    public static ProcessProducerMethod<Cheese, Farm> getProcessProducerMethodEvent() {
        return processProducerMethodEvent;
    }
}
