/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

@ApplicationScoped
public class ApplicationHorseStable {
    @Inject
    private BeanManager beanManager;
    private static boolean dependentContextActive = false;

    public void horseEntered(@Observes HorseInStableEvent horseEvent) {
        dependentContextActive = beanManager.getContext(Dependent.class).isActive();
    }

    public static boolean isDependentContextActive() {
        return ApplicationHorseStable.dependentContextActive;
    }

    public static void setDependentContextActive(boolean dependentContextActive) {
        ApplicationHorseStable.dependentContextActive = dependentContextActive;
    }
}
