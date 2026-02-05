/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.build.compatible.extensions.registration;

import java.util.List;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;

@Dependent
public class MyServiceFoo implements MyService, MyGenericService<String> {
    @Override
    public String hello() {
        return "foo";
    }

    void init(@Observes Startup event) {
    }

    void observeListOfString(@Observes List<String> list) {
    }

    // this observer should _not_ be counted
    void observeIterableOfString(@Observes Iterable<String> list) {
    }

    // this observer should _not_ be counted either
    void observeListOfInteger(@Observes List<Integer> list) {
    }
}
