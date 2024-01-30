/*
 * Copyright 2023, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.alternative.selection.priority;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

// produces standard (non-alternative) beans that should be replaced by alternatives
@ApplicationScoped
public class RegularBeanProducer {

    @Produces
    @ProducedByMethod
    Alpha producer1() {
        return new Alpha(ProducerExplicitPriorityTest.DEFAULT);
    }

    @Produces
    @ProducedByMethod
    Beta producer2() {
        return new Beta(ProducerExplicitPriorityTest.DEFAULT);
    }

    @Produces
    @ProducedByField
    Alpha producer3 = new Alpha(ProducerExplicitPriorityTest.DEFAULT);

    @Produces
    @ProducedByField
    Beta producer4 = new Beta(ProducerExplicitPriorityTest.DEFAULT);

    @Produces
    @ProducedByMethod
    Gamma producer5() {
        return new Gamma(ProducerExplicitPriorityTest.DEFAULT);
    }

    @Produces
    @ProducedByField
    Gamma producer6 = new Gamma(ProducerExplicitPriorityTest.DEFAULT);

    @Produces
    @ProducedByMethod
    Delta producer7() {
        return new Delta(ProducerExplicitPriorityTest.DEFAULT);
    }

    @Produces
    @ProducedByField
    Delta producer8 = new Delta(ProducerExplicitPriorityTest.DEFAULT);
}
