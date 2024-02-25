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
package org.jboss.cdi.tck.tests.event.observer.conditional;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Reception;

@RequestScoped
public class Tarantula {

    private static boolean notified = false;

    public void observe(@Observes(notifyObserver = Reception.IF_EXISTS) TarantulaEvent someEvent) {
        notified = true;
    }

    public void ping() {
    }

    public static boolean isNotified() {
        return notified;
    }

    public static void reset() {
        notified = false;
    }

}
