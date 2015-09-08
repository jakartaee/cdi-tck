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
package org.jboss.cdi.tck.tests.event.observer.conditional;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import javax.enterprise.event.Reception;

@ApplicationScoped
public class AsyncConditionalObserver {

    private static AtomicBoolean notified = new AtomicBoolean(false);
    private static boolean syncNotified = false;

    public static AtomicBoolean IsNotified() {
        return notified;
    }

    public static boolean isSyncNotified() {
        return syncNotified;
    }

    public void observeSync(@Observes(notifyObserver = Reception.IF_EXISTS) AsyncConditionalEvent event) {
        syncNotified = true;
    }

    public void observeAsync(@ObservesAsync(notifyObserver = Reception.IF_EXISTS) AsyncConditionalEvent event) {
        notified.set(true);
    }

    public static void reset() {
        notified.set(false);
        syncNotified = false;
    }

    public void ping() {
    }

}
