/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016 Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.se.container;

import java.util.concurrent.atomic.AtomicBoolean;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.AfterDeploymentValidation;
import jakarta.enterprise.inject.spi.AfterTypeDiscovery;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.ProcessBean;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;
import jakarta.enterprise.inject.spi.ProcessInjectionTarget;

public class TestExtension implements Extension {

    private AtomicBoolean bbdNotified = new AtomicBoolean();
    private AtomicBoolean atdNotified = new AtomicBoolean();
    private AtomicBoolean abdNotified = new AtomicBoolean();
    private AtomicBoolean advNotified = new AtomicBoolean();
    private AtomicBoolean patNotified = new AtomicBoolean();
    private AtomicBoolean pitNotified = new AtomicBoolean();
    private AtomicBoolean pbNotified = new AtomicBoolean();
    private AtomicBoolean pbaNotified = new AtomicBoolean();


    void observeBeforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager beanManager) {
        bbdNotified.set(true);
    }

    void observeAfterTypeDiscovery(@Observes AfterTypeDiscovery event, BeanManager beanManager) {
        atdNotified.set(true);
    }

    void observeAfterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager beanManager) {
        abdNotified.set(true);
    }

    void observeAfterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager beanManager) {
        advNotified.set(true);
    }

    void observerProcessAnnotatedType(@Observes ProcessAnnotatedType<Foo> event, BeanManager beanManager) {
        patNotified.set(true);
    }

    void observerProcessInjectionTarget(@Observes ProcessInjectionTarget<Foo> event, BeanManager beanManager) {
        pitNotified.set(true);
    }

    void observerProcessBeanAttributes(@Observes ProcessBeanAttributes<Foo> event, BeanManager beanManager) {
        pbaNotified.set(true);
    }

    void observerProcessBean(@Observes ProcessBean<Foo> event, BeanManager beanManager) {
        pbNotified.set(true);
    }

    public AtomicBoolean getBeforeBeanDiscoveryNotified() {
        return bbdNotified;
    }

    public AtomicBoolean getAfterBeanDiscoveryNotified() {
        return abdNotified;
    }

    public AtomicBoolean getAfterDeploymentValidationNotified() {
        return advNotified;
    }

    public AtomicBoolean getProcessAnnotatedTypeNotified() {
        return patNotified;
    }

    public AtomicBoolean getAfterTypeDiscoveryNotified() {
        return atdNotified;
    }

    public AtomicBoolean getProcessInjectionTargetNotified() {
        return pitNotified;
    }

    public AtomicBoolean getProcessBeanAttributesNotified() {
        return pbaNotified;
    }

    public AtomicBoolean getProcessBeanNotified() {
        return pbNotified;
    }
}
