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
package org.jboss.cdi.tck.tests.event.observer.resolve;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;

@NotEnabled
@Dependent
class DisabledObserver {
    public void observeSecret(@Observes @Secret String secretString) {
        if ("fail if disabled observer invoked".equals(secretString)) {
            assert false : "This observer should not be invoked since it resides on a bean with a policy that is not enabled.";
        }
    }

    public void observeGhost(@Observes Ghost ghost) {
        assert false : "This observer should not be invoked since it resides on a bean with a policy that is not enabled.";
    }
}
