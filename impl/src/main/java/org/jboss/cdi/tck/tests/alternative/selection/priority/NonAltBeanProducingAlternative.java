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
public class NonAltBeanProducingAlternative {

    @Alternative
    @Priority(10)
    @Produces
    @ProducedByMethod
    Alpha producer1() {
        return new Alpha(ProducerExplicitPriorityTest.ALT);
    }

    @Alternative
    @Priority(10)
    @Produces
    @ProducedByMethod
    Beta producer2() {
        return new Beta(ProducerExplicitPriorityTest.ALT);
    }

    @Alternative
    @Priority(10)
    @Produces
    @ProducedByField
    Alpha producer3 = new Alpha(ProducerExplicitPriorityTest.ALT);

    @Alternative
    @Priority(10)
    @Produces
    @ProducedByField
    Beta producer4 = new Beta(ProducerExplicitPriorityTest.ALT);

    @Produces
    @ProducedByMethod
    @Alternative
    @Priority(10)
    Gamma producer5() {
        return new Gamma(ProducerExplicitPriorityTest.ALT);
    }

    @Produces
    @ProducedByField
    @Alternative
    @Priority(10)
    Gamma producer6 = new Gamma(ProducerExplicitPriorityTest.ALT);

    @Produces
    @ProducedByMethod
    @Alternative
    @Priority(10)
    Delta producer7() {
        return new Delta(ProducerExplicitPriorityTest.ALT);
    }

    @Produces
    @ProducedByField
    @Alternative
    @Priority(10)
    Delta producer8 = new Delta(ProducerExplicitPriorityTest.ALT);

}
