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
package org.jboss.cdi.tck.tests.implementation.producer.method.definition;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

public class SpiderProducer {

    private static Spider[] ALL_SPIDERS = { new Tarantula(), new LadybirdSpider(), new DaddyLongLegs() };

    @Produces
    @Tame
    public Tarantula produceTameTarantula() {
        return new DefangedTarantula(0);
    }

    @Produces
    @Deadliest
    public Tarantula producesDeadliestTarantula(@Tame Tarantula tameTarantula, Tarantula tarantula) {
        return tameTarantula.getDeathsCaused() >= tarantula.getDeathsCaused() ? tameTarantula : tarantula;
    }

    @Produces
    public Spider getNullSpider() {
        return null;
    }

    @Produces
    public FunnelWeaver<Spider> getFunnelWeaverSpider() {
        return new FunnelWeaver<Spider>("Weaver");
    }

    @Produces
    public Animal makeASpider() {
        return new WolfSpider();
    }

    @Number
    @Produces
    public int getWolfSpiderSize() {
        return 4;
    }

    @Produces
    public Spider[] getSpiders() {
        return ALL_SPIDERS;
    }

    @Produces
    @Named
    @RequestScoped
    @Tame
    public DaddyLongLegs produceDaddyLongLegs() {
        return new DaddyLongLegs();
    }

    @Produces
    @Named
    @Tame
    public LadybirdSpider getLadybirdSpider() {
        return new LadybirdSpider();
    }

    @Produces
    @Named("blackWidow")
    @Tame
    public BlackWidow produceBlackWidow() {
        return new BlackWidow();
    }

    @Produces
    @AnimalStereotype
    @Tame
    public WolfSpider produceWolfSpider() {
        return new WolfSpider();
    }

    @Produces
    public Bite getBite() {
        return new Bite() {
        };
    }

}
