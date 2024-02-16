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
package org.jboss.cdi.tck.interceptors.tests.contract.interceptorLifeCycle;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;

@WarriorBinding
@Dependent
public class Warrior {

    @Inject
    Weapon weapon1;

    Weapon weapon2;

    @Inject
    public Warrior(Weapon weapon2) {
        this.weapon2 = weapon2;
    }

    @AttackBinding
    @MethodBinding
    public void attack1() {
        weapon1.use();
    }

    @AttackBinding
    @MethodBinding
    public void attack2() {
        weapon2.use();
    }

    public Weapon getWeapon2() {
        return weapon2;
    }

    public Weapon getWeapon1() {
        return weapon1;
    }
}
