/*
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
package org.jboss.cdi.tck.tests.context.dependent;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

@Dependent
public class SpiderProducer {
    @Inject
    private BeanManager beanManager;

    private static boolean dependentContextActive = false;
    private static boolean destroyed = false;
    private static Integer instanceUsedForDisposalHashcode = null;
    private static Integer instanceUsedForProducerHashcode = null;
    private static Integer foxUsedForDisposalHashcode = null;

    @Produces
    @Pet
    public Tarantula produceTarantula(Tarantula spider, DomesticationKit domesticationKit) {
        dependentContextActive = beanManager.getContext(Dependent.class).isActive();
        instanceUsedForProducerHashcode = this.hashCode();
        return spider;
    }

    public void disposeTarantula(@Disposes @Pet Tarantula tarantula, Fox fox) {
        dependentContextActive = beanManager.getContext(Dependent.class).isActive();
        instanceUsedForDisposalHashcode = this.hashCode();
        foxUsedForDisposalHashcode = fox.hashCode();
    }

    public static boolean isDependentContextActive() {
        return dependentContextActive;
    }

    public static Integer getInstanceUsedForDisposalHashcode() {
        return instanceUsedForDisposalHashcode;
    }

    public static Integer getInstanceUsedForProducerHashcode() {
        return instanceUsedForProducerHashcode;
    }

    public static Integer getFoxUsedForDisposalHashcode() {
        return foxUsedForDisposalHashcode;
    }

    public static boolean isDestroyed() {
        return destroyed;
    }

    public static void reset() {
        destroyed = false;
        dependentContextActive = false;
        instanceUsedForDisposalHashcode = null;
        instanceUsedForProducerHashcode = null;
        foxUsedForDisposalHashcode = null;
    }

    @PreDestroy
    public void destroy() {
        destroyed = true;
    }
}
