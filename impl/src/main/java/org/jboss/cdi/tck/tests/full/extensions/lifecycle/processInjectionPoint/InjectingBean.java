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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle.processInjectionPoint;

import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

public class InjectingBean {

    @Inject
    @Foo
    @PlainAnnotation
    @SuppressWarnings("unused")
    private transient Alpha<String> alpha;

    @Inject
    public InjectingBean(@Bar @PlainAnnotation Bravo<String> bravo) {
    }

    @Inject
    public void init(@PlainAnnotation Charlie charlie) {
    }

    @Produces
    public ProducedBean produce(@PlainAnnotation @Foo Alpha<Integer> alpha, @PlainAnnotation @Bar Bravo<Integer> bravo) {
        return null;
    }

    public void destroy(@Disposes ProducedBean producedBean, Delta delta) {

    }
}
