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
package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

class SpiderProducer {
    private static boolean tameSpiderDestroyed = false;
    private static boolean deadliestTarantulaDestroyed = false;
    private static boolean deadliestSandSpiderDestroyed = false;

    @Produces
    @Tame
    public Tarantula produceTameTarantula() {
        return new DefangedTarantula(0);
    }

    @Produces
    @Deadliest
    public SandSpider produceDeadliestSandSpider() {
        return new SandSpider();
    }

    @Produces
    @Deadliest
    public WebSpider produceDeadliestWebSpider() {
        return new WebSpider();
    }

    @Produces
    @Deadliest
    public Tarantula producesDeadliestTarantula(@Tame Tarantula tameTarantula, Tarantula tarantula) {
        return tameTarantula.getDeathsCaused() >= tarantula.getDeathsCaused() ? tameTarantula : tarantula;
    }

    public void destroyTameTarantula(@Disposes @Tame Tarantula spider) {
        SpiderProducer.tameSpiderDestroyed = true;
    }

    public void destroyTameSandSpider(@Disposes @Deadliest SandSpider spider) {
        SpiderProducer.deadliestSandSpiderDestroyed = true;
    }

    public static void destroyDeadliestSpider(@Disposes @Deadliest Tarantula spider, Tarantula anotherSpider) {
        assert spider != anotherSpider;
        SpiderProducer.deadliestTarantulaDestroyed = true;
    }

    public static boolean isTameSpiderDestroyed() {
        return tameSpiderDestroyed;
    }

    public static boolean isDeadliestTarantulaDestroyed() {
        return deadliestTarantulaDestroyed;
    }

    public static boolean isDeadliestSandSpiderDestroyed() {
        return deadliestSandSpiderDestroyed;
    }
}
