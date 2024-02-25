/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.event;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeanManager;

/**
 * @author Martin Kouba
 *
 */
@Dependent
public class TerrierObserver {

    public static boolean eventObserved = false;
    public static boolean parametersInjected = false;

    public void observeDog(@Observes BullTerrier event, BeanManager beanManager, @Tame Volume volume,
            OrangeCheekedWaxbill orangeCheekedWaxbill) {
        eventObserved = true;
        parametersInjected = (beanManager != null && volume != null && orangeCheekedWaxbill != null);
    }

    public static void reset() {
        eventObserved = false;
        parametersInjected = false;
    }

}
