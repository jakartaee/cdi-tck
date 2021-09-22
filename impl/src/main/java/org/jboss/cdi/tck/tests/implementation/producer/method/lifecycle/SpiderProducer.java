/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.cdi.tck.tests.implementation.producer.method.lifecycle;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

@Dependent
public class SpiderProducer {

    @Inject
    private Web web;

    private static Tarantula tarantulaCreated;
    private static Web injectedWeb;
    private static boolean destroyArgumentsSet;
    private static Tarantula tarantulaDestroyed;

    @Produces
    @Pet
    public Tarantula produceTarantula() {
        Tarantula tarantula = new Tarantula("Pete");
        tarantulaCreated = tarantula;
        injectedWeb = web;
        tarantulaCreated = tarantula;
        resetTarantulaDestroyed();
        return tarantula;
    }

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

    public static void resetTarantulaCreated() {
        SpiderProducer.tarantulaCreated = null;
    }


    public static Tarantula getTarantulaCreated() {
        return tarantulaCreated;
    }

    public static Tarantula getTarantulaDestroyed() {
        return tarantulaDestroyed;
    }

    public static void reset() {
        resetTarantulaCreated();
    }

    public static void resetInjections() {
        injectedWeb = null;
    }

    public static Web getInjectedWeb() {
        return injectedWeb;
    }

    public static boolean isDestroyArgumentsSet() {
        return destroyArgumentsSet;
    }

    public static void resetTarantulaDestroyed() {
        tarantulaDestroyed = null;
        destroyArgumentsSet = false;
    }

    public static boolean isTarantulaDestroyed() {
        return tarantulaDestroyed != null;
    }

}
