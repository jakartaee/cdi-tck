/*
 * Copyright 2023, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.alternative.selection.priority;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
@Alternative
@Priority(1)
public class AltBeanProducingPrioritizedNonAlternative {

    @Priority(20) // should override class-level priority value and hence end up having the highest priority
    @Produces
    @ProducedByMethod
    Delta producer1() {
        return new Delta(ProducerExplicitPriorityTest.ALT2);
    }

    @Priority(20) // should override class-level priority value and hence end up having the highest priority
    @Produces
    @ProducedByField
    Delta producer2 = new Delta(ProducerExplicitPriorityTest.ALT2);
}
