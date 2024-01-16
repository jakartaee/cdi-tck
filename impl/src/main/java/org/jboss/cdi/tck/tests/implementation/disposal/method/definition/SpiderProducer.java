/*
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
package org.jboss.cdi.tck.tests.implementation.disposal.method.definition;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

@Dependent
public class SpiderProducer {

    private static boolean tameSpiderDestroyed = false;
    private static boolean deadliestTarantulaDestroyed = false;
    private static boolean deadliestSandSpiderDestroyed = false;
    private static int widowsDestroyed = 0;
    private static boolean scaryBlackWidowDestroyed = false;
    private static boolean tameBlackWidowDestroyed = false;

    @Produces
    @Scary
    @SuppressWarnings("unused")
    private final Calisoga scaryCalisoga = new Calisoga("scary");
    @Produces
    @Tame
    @SuppressWarnings("unused")
    @RequestScoped
    private final Calisoga tameCalisoga = new Calisoga("tame");


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

    @Produces
    @Tame
    public Widow produceTameWidow() {
        return new Widow("steatoda");
    }

    @Produces
    @Deadliest
    public Widow produceDeadliestWidow() {
        return new Widow("black");
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

    public void destroyScaryCalisoga(@Disposes @Scary Calisoga calisoga) {
        assert calisoga != null;
        assert "scary".equals(calisoga.getName());
        scaryBlackWidowDestroyed = true;
    }

    public void destroyTameCalisoga(@Disposes @Tame Calisoga calisoga) {
        assert calisoga != null;
        assert "tame".equals(calisoga.getName());
        tameBlackWidowDestroyed = true;
    }

    public void destroyWidow(@Disposes @Any Widow widow) {
        widowsDestroyed++;
    }

    public static boolean isTameSpiderDestroyed() {
        return tameSpiderDestroyed;
    }

    public static boolean isDeadliestTarantulaDestroyed() {
        return deadliestTarantulaDestroyed;
    }

    public static int getWidowsDestroyed() {
        return widowsDestroyed;
    }

    public static boolean isScaryBlackWidowDestroyed() {
        return scaryBlackWidowDestroyed;
    }

    public static boolean isTameBlackWidowDestroyed() {
        return tameBlackWidowDestroyed;
    }

    public static boolean isDeadliestSandSpiderDestroyed() {
        return deadliestSandSpiderDestroyed;
    }

    public static void reset() {
        tameSpiderDestroyed = false;
        deadliestTarantulaDestroyed = false;
        widowsDestroyed = 0;
        scaryBlackWidowDestroyed = false;
        tameBlackWidowDestroyed = false;
    }
}
