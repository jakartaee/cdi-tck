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
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

@Dependent
public class HorseStable {
    private static boolean dependentContextActive = false;
    private static HorseStable instanceThatObservedEvent = null;
    private static boolean destroyed = false;

    @Inject
    public HorseStable(BeanManager beanManager) {
        dependentContextActive = beanManager.getContext(Dependent.class).isActive();
    }

    public void horseEntered(@Observes HorseInStableEvent horseEvent, Fox fox) {
        instanceThatObservedEvent = this;
    }

    @PreDestroy
    public void destroy() {
        destroyed = true;
    }

    public static boolean isDependentContextActive() {
        return dependentContextActive;
    }

    public static void reset() {
        HorseStable.dependentContextActive = false;
        instanceThatObservedEvent = null;
        destroyed = false;
    }

    public static HorseStable getInstanceThatObservedEvent() {
        return instanceThatObservedEvent;
    }

    public static boolean isDestroyed() {
        return destroyed;
    }
}
