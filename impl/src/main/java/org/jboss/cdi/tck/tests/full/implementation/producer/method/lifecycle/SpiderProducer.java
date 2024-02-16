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
package org.jboss.cdi.tck.tests.full.implementation.producer.method.lifecycle;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;

@Dependent
public class SpiderProducer {
    private static Tarantula tarantulaCreated;

    @Produces
    @Pet
    public Tarantula produceTarantula() {
        Tarantula tarantula = new Tarantula("Pete");
        tarantulaCreated = tarantula;
        return tarantula;
    }

    @Produces
    @Null
    public Spider getNullSpider() {
        return null;
    }

    public static boolean isTarantulaCreated() {
        return tarantulaCreated != null;
    }

    public static void resetTarantulaCreated() {
        SpiderProducer.tarantulaCreated = null;
    }


    public static Tarantula getTarantulaCreated() {
        return tarantulaCreated;
    }

    public static void reset() {
        resetTarantulaCreated();
    }
}
