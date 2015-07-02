/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.observer.priority;

import javax.enterprise.event.Observes;
import javax.interceptor.Interceptor;

import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.weld.experimental.Priority;

public class SunriseObservers {

    public static class AsianObserver {

        public void observeSunset(@Observes @Priority(Interceptor.Priority.APPLICATION + 499) Sunrise sunrise) {
            ActionSequence.addAction(getClass().getName());
        }
    }

    public static class GermanObserver {

        public void observeSunset(@Observes Sunrise sunrise) {
            ActionSequence.addAction(getClass().getName());
        }
    }

    public static class ItalianObserver {

        public void observeSunset(@Observes Sunrise sunrise) {
            ActionSequence.addAction(getClass().getName());
        }
    }

}
