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
package org.jboss.cdi.tck.tests.event.resolve.typeWithParameters;

import java.util.List;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;

@Dependent
public class FooObserver extends AbstractObserver {

    public static final String SEQUENCE = "fooString";

    public static final String SEQUENCE_NESTED = "fooStringList";

    public void observeTypeParameterActualType(@Observes Foo<String> foo) {
        addAction("fooString", foo);
    }

    public void observeTypeParameterActualTypeNested(@Observes Foo<List<String>> foo) {
        addAction("fooStringList", foo);
    }

}
