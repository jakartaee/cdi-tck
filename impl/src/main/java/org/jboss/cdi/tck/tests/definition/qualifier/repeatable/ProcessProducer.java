/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.definition.qualifier.repeatable;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

public class ProcessProducer {

    @Inject
    Event<Process> event;

    @Produces
    @Start("A")
    public Process createProcessA() {
        Process process = new Process();
        event.select(new Start.StartLiteral("A")).fire(process);
        return process;
    }

    @Produces
    @Start("B")
    public Process createProcessB() {
        Process process = new Process();
        event.select(new Start.StartLiteral("B")).fire(process);
        return process;
    }

    @Produces
    @Start("B")
    @Start("C")
    public Process createProcessAB() {
        Process process = new Process();
        event.select(new Start.StartLiteral("B"), new Start.StartLiteral("C")).fire(process);
        return process;
    }
}
