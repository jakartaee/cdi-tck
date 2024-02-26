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
package org.jboss.cdi.tck.interceptors.tests.order.lifecycleCallback;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;

@LakeCargoShipClassBinding
@Dependent
class LakeCargoShip extends CargoShip {
    // Every interceptor sets this property to a certain value.
    // The following interceptor verifies the correct order of the chain by
    // inspecting this value.
    private static int sequence = 0;

    static int getSequence() {
        return sequence;
    }

    static void setSequence(int value) {
        sequence = value;
    }

    @PostConstruct
    void postConstruct3() {
        assert LakeCargoShip.getSequence() == 6;
        LakeCargoShip.setSequence(7);
    }
}
