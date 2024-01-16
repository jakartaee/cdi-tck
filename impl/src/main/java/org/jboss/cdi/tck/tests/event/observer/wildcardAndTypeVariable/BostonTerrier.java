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
package org.jboss.cdi.tck.tests.event.observer.wildcardAndTypeVariable;

import java.util.List;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;

@Dependent
public class BostonTerrier {

    public static boolean observedTypeVariable;

    public static boolean observedWildcard;

    public static boolean observedArrayTypeVariable;

    public static boolean observedArrayWildCard;

    public <T extends Behavior> void observesEventWithTypeParameter(@Observes T behavior) {
        observedTypeVariable = true;
    }

    public void observesEventTypeWithWildcard(@Observes List<?> someArray) {
        observedWildcard = true;
    }

    public <T extends Behavior> void observesArrayTypeVariable(@Observes T[] terriers) {
        observedArrayTypeVariable = true;
    }

    public void observesArrayWildcard(@Observes List<?>[] terriers) {
        observedArrayWildCard = true;
    }

    public static void reset() {
        observedTypeVariable = false;
        observedWildcard = false;
        observedArrayTypeVariable = false;
        observedArrayWildCard = false;
    }
}
