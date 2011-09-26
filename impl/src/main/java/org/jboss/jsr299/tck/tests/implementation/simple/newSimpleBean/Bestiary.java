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
package org.jboss.jsr299.tck.tests.implementation.simple.newSimpleBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;

@RequestScoped
public class Bestiary {

    private Set<String> possibleNames;
    private Set<String> knightsWhichKilledTheDragons;

    @Produces
    Collection<Dragon> getDragons(@New ArrayList<Dragon> dragons) {
        return dragons;
    }

    public void observeBirth(@Observes Griffin griffin, @New TreeSet<String> possibleNames) {
        this.possibleNames = possibleNames;
    }

    public void destroyDragons(@Disposes Collection<Dragon> dragons, @New LinkedHashSet<String> knights) {
        this.knightsWhichKilledTheDragons = knights;
    }

    public Set<String> getPossibleNames() {
        return possibleNames;
    }

    public Set<String> getKnightsWhichKilledTheDragons() {
        return knightsWhichKilledTheDragons;
    }

}
