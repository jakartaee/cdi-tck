/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.configurators.beanConfigurator;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Instance;


/** Producer and Consumer logic resides here
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class MonsterController {

    
    public static boolean zombieKilled = false;
    public static boolean skeletonKilled = false;
    public static boolean zombieProducerCalled = false;
    public static boolean skeletonProducerCalled = false;
    public static boolean ghostInstanceObtained = false; 
    public static boolean vampireInstanceCreated = false;
    
    private static Ghost ghostInstance = new Ghost(false);
    
    // skeleton Supplier user as producer
    public static Supplier<Skeleton> skeletonSupplier = () -> {
        skeletonProducerCalled = true;
        return new Skeleton(100);
    };

    // zombie Fuction used as producer
    public static Function<Instance<Object>, Zombie> zombieProducingFunction = (Instance<Object> t) -> {
        zombieProducerCalled = true;
        return new Zombie(t.select(Boolean.class).get());
    };
    
    public static BiConsumer<Zombie, CreationalContext<Zombie>> zombieConsumer = new BiConsumer<Zombie, CreationalContext<Zombie>>() {

        @Override
        public void accept(Zombie t, CreationalContext<Zombie> u) {
            zombieKilled = true;
        }
    };
    
    public static Consumer<Skeleton> skeletonConsumer = new Consumer<Skeleton>() {

        @Override
        public void accept(Skeleton t) {
            skeletonKilled = true;
        }
    };
    
    public static Ghost getGhostInstance() {
        ghostInstanceObtained = true;
        return ghostInstance;
    }
}
