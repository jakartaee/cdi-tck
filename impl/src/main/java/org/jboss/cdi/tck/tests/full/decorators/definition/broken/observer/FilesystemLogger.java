/*
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.decorators.definition.broken.observer;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

@Decorator
public class FilesystemLogger implements Logger {

    @Inject
    @Any
    @Delegate
    Logger logger;

    public void log() {
        logger.log();
    }

    public void observeFoo(@Observes FooPayload fooPayload) {
    }

}
