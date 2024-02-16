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
package org.jboss.cdi.tck.tests.event.eventTypes;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;

@RequestScoped
class Listener {
    List<Object> objectsFired = new ArrayList<Object>();

    public void registerNumberFired(@Observes @Extra Integer i) {
        objectsFired.add(i);
    }

    public void registerSongFired(@Observes @Any Song s) {
        objectsFired.add(s);
    }

    public void registerBroadcastFired(@Observes @Any Broadcast b) {
        objectsFired.add(b);
    }

    public void registerArrayOfSongs(@Observes Song[] songs) {
        objectsFired.add(songs);
    }

    public void registerArrayOfNumbers(@Observes Integer[] integers) {
        objectsFired.add(integers);
    }

    public void registerArrayOfNumberPrimitives(@Observes int[] integers) {
        objectsFired.add(integers);
    }

    public List<Object> getObjectsFired() {
        return objectsFired;
    }

    public void reset() {
        objectsFired.clear();
    }

}
