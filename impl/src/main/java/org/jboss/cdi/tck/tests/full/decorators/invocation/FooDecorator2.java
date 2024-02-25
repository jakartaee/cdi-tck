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
package org.jboss.cdi.tck.tests.full.decorators.invocation;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

/**
 * @author pmuir
 *
 */
@Decorator
public class FooDecorator2 implements Foo {

    public static final String SUFFIX = "fooDecorator2";

    private static String message;

    /**
     * @return the message
     */
    public static String getMessage() {
        return message;
    }

    /**
     */
    public static void reset() {
        FooDecorator2.message = null;
    }

    @Inject
    @Delegate
    Foo foo;

    public void log(String message) {
        FooDecorator2.message = message;
        foo.log(message + SUFFIX);
    }

}
