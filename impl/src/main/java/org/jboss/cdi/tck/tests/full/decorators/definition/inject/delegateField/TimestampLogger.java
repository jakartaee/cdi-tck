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
package org.jboss.cdi.tck.tests.full.decorators.definition.inject.delegateField;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

/**
 * @author pmuir
 *
 */
@Decorator
public class TimestampLogger implements Logger {

    public static final String PREFIX = TimestampLogger.class.getSimpleName();

    @Inject
    @Delegate
    private Logger instanceField;

    private static Logger field;

    public void log(String message) {
        field = instanceField;
        field.log(PREFIX + message);
    }

    /**
     * @return the field
     */
    public static Logger getField() {
        return field;
    }

    /**
     */
    public static void reset() {
        field = null;
    }

}
