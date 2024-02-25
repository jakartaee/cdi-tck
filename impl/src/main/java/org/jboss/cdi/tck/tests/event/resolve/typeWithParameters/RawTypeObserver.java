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
package org.jboss.cdi.tck.tests.event.resolve.typeWithParameters;

import java.util.Random;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;

@Dependent
public class RawTypeObserver {

    public static boolean OBSERVED = false;

    @SuppressWarnings("rawtypes") // raw type used intentionally
    public void observe(@Observes Box box) {
        OBSERVED = true;
    }

    protected static class BoxWithObjectTypeParameters extends Box<Object, Object, Object> {
    }

    protected static class BoxWithDifferentTypeParameters extends Box<Number, String, Random> {
    }
}
