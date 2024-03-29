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
package org.jboss.cdi.tck.tests.full.decorators.builtin.event.complex;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import org.jboss.cdi.tck.util.ActionSequence;

@ApplicationScoped
public class Observers {

    private final ActionSequence sequence = new ActionSequence(Observers.class.getName());

    @Ordered(20)
    void observe1(@Observes @Bar @Baz Payload event) {
        sequence.add("third");
    }

    @Ordered(-10)
    void observe2(@Observes @Bar @Baz Payload event) {
        sequence.add("first");
    }

    void observe3(@Observes @Bar @Baz Payload event) {
        sequence.add("second");
    }

    public ActionSequence getSequence() {
        return sequence;
    }
}
