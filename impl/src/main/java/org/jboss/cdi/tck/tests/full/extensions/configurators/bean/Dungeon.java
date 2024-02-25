/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.configurators.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Simple class checking that all 'manually' created beans can be injected
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@ApplicationScoped
public class Dungeon {

    @Inject
    @Undead
    private Skeleton skeleton;

    @Inject
    @Undead
    @Dangerous
    private Zombie zombie;

    @Inject
    @Undead
    private Ghost ghost;

    @Inject
    @Undead
    private Vampire vampire;

    public boolean hasAllMonters() {
        return skeleton != null && zombie != null && ghost != null && vampire != null;
    }

    public Skeleton getSkeleton() {
        return skeleton;
    }

    public Zombie getZombie() {
        return zombie;
    }

    public Ghost getGhost() {
        return ghost;
    }

    public Vampire getVampire() {
        return vampire;
    }
}
