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
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

@Dependent
public class HorseStable {
    private static boolean dependentContextActive = false;
    private static Integer instanceThatObservedEventHashcode = null;
    private static Integer foxUsedForObservedEventHashcode = null;
    private static boolean destroyed = false;

    @Inject
    public HorseStable(BeanManager beanManager) {
        dependentContextActive = beanManager.getContext(Dependent.class).isActive();
    }

    public void horseEntered(@Observes HorseInStableEvent horseEvent, Fox fox) {
        instanceThatObservedEventHashcode = this.hashCode();
        foxUsedForObservedEventHashcode = fox.hashCode();
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
        instanceThatObservedEventHashcode = null;
        foxUsedForObservedEventHashcode = null;
        destroyed = false;
    }

    public static Integer getInstanceThatObservedEventHashcode() {
        return instanceThatObservedEventHashcode;
    }

    public static Integer getFoxUsedForObservedEventHashcode() {
        return foxUsedForObservedEventHashcode;
    }

    public static boolean isDestroyed() {
        return destroyed;
    }
}
