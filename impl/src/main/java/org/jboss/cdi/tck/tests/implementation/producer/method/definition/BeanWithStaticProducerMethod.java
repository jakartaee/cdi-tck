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
package org.jboss.cdi.tck.tests.implementation.producer.method.definition;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

@Dependent
public class BeanWithStaticProducerMethod {
    static boolean stringDestroyed;
    static boolean yummyDestroyed;
    static boolean numberDestroyed;

    @Produces
    @Yummy
    public static String yummy() {
        yummyDestroyed = false;
        return "yummy";
    }

    @Produces
    @Number
    public String number() {
        numberDestroyed = false;
        return "number";
    }

    @Produces
    @Tame
    public static String getString() {
        stringDestroyed = false;
        return "Pete";
    }

    public static void destroyString(@Disposes @Tame String someString) {
        stringDestroyed = true;
    }

    public void destroyYummy(@Disposes @Yummy String someString) {
        yummyDestroyed = true;
    }

    public static void destroyNumber(@Disposes @Number String someString) {
        numberDestroyed = true;
    }
}
