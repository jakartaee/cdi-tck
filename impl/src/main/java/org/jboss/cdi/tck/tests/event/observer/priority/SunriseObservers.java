/*
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.observer.priority;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import jakarta.interceptor.Interceptor;

import org.jboss.cdi.tck.util.ActionSequence;

public class SunriseObservers {

    @Dependent
    public static class AsianObserver {

        public void observeSunset(@Observes @Priority(Interceptor.Priority.APPLICATION + 499) Sunrise sunrise) {
            ActionSequence.addAction(getClass().getName());
        }
    }

    @Dependent
    public static class GermanObserver {

        public void observeSunset(@Observes Sunrise sunrise) {
            ActionSequence.addAction(getClass().getName());
        }
    }

    @Dependent
    public static class ItalianObserver {

        public void observeSunset(@Observes Sunrise sunrise) {
            ActionSequence.addAction(getClass().getName());
        }
    }

}
