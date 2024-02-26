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
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.Specializes;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

@Specializes
@Dependent
public class PreferredSpiderProducer extends SpiderProducer {
    @Inject
    private Web web;
    private static Web injectedWeb;
    private static Tarantula tarantulaCreated;
    private static Tarantula tarantulaDestroyed;
    private static boolean destroyArgumentsSet;

    @Override
    @Produces
    @Pet
    @Specializes
    public Tarantula produceTarantula() {
        Tarantula tarantula = new Tarantula("Pete");
        tarantulaCreated = tarantula;
        resetTarantulaDestroyed();
        injectedWeb = web;
        return tarantula;
    }

    @Override
    @Produces
    @Null
    public Spider getNullSpider() {
        return null;
    }

    public void destroyTarantula(@Disposes @Pet Tarantula spider, BeanManager beanManager) {
        tarantulaDestroyed = spider;
        injectedWeb = web;
        if (beanManager != null) {
            destroyArgumentsSet = true;
        }
    }

    public static boolean isTarantulaCreated() {
        return tarantulaCreated != null;
    }

    public static boolean isTarantulaDestroyed() {
        return tarantulaDestroyed != null;
    }

    public static Tarantula getTarantulaDestroyed() {
        return tarantulaDestroyed;
    }

    public static void resetTarantulaCreated() {
        PreferredSpiderProducer.tarantulaCreated = null;
    }

    public static void resetTarantulaDestroyed() {
        PreferredSpiderProducer.tarantulaDestroyed = null;
        PreferredSpiderProducer.destroyArgumentsSet = false;
    }

    public static boolean isDestroyArgumentsSet() {
        return destroyArgumentsSet;
    }

    public static Tarantula getTarantulaCreated() {
        return tarantulaCreated;
    }

    public static Web getInjectedWeb() {
        return injectedWeb;
    }

    public static void resetInjections() {
        injectedWeb = null;
    }

    public static void reset() {
        resetTarantulaCreated();
        resetTarantulaDestroyed();
        resetInjections();
    }

}
