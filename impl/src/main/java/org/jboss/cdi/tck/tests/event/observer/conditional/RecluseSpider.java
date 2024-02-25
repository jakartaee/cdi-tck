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

/**
 * Simple bean that conditionally listens to events.
 *
 */
@RequestScoped
public class RecluseSpider {
    private static boolean notified = false;
    private static boolean instanceNotified = false;
    private Web web;

    public void observe(@Observes(notifyObserver = Reception.IF_EXISTS) ConditionalEvent someEvent) {
        notified = true;
        instanceNotified = true;
        if (web != null) {
            web.addRing();
        }
    }

    public boolean isInstanceNotified() {
        return instanceNotified;
    }

    public static boolean isNotified() {
        return notified;
    }

    public void setWeb(Web web) {
        this.web = web;
    }

    public Web getWeb() {
        return this.web;
    }
}
