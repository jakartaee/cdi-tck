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
package org.jboss.jsr299.tck.tests.context.dependent;

import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

public class SpiderProducer {
    @Inject
    private BeanManager beanManager;

    private static boolean dependentContextActive = false;
    private static boolean destroyed = false;
    private static SpiderProducer instanceUsedForDisposal = null;

    @Produces
    @Pet
    public Tarantula produceTarantula(Tarantula spider, DomesticationKit domesticationKit) {
        dependentContextActive = beanManager.getContext(Dependent.class).isActive();
        return spider;
    }

    public void disposeTarantula(@Disposes @Pet Tarantula tarantula, Fox fox) {
        dependentContextActive = beanManager.getContext(Dependent.class).isActive();
        instanceUsedForDisposal = this;
    }

    public static boolean isDependentContextActive() {
        return dependentContextActive;
    }

    public static SpiderProducer getInstanceUsedForDisposal() {
        return instanceUsedForDisposal;
    }

    public static boolean isDestroyed() {
        return destroyed;
    }

    public static void reset() {
        destroyed = false;
        dependentContextActive = false;
        instanceUsedForDisposal = null;
    }

    @PreDestroy
    public void destroy() {
        destroyed = true;
    }
}
