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
package org.jboss.cdi.tck.tests.event.broken.observer.beanNotManaged;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;

/**
 * This class is not a bean since it does not have a default constructor nor does it have a constructor annotated with {@link
 * jakarta.inject.Inject}. As such, an observer method is not detected on this class.
 *
 * @author David Allen
 */
public class NonManagedBean {
    public NonManagedBean(String name) {
    }

    public void observe(@Observes @Any String event) {
    }
}
