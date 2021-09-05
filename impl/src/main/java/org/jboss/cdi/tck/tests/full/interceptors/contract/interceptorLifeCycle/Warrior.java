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
package org.jboss.cdi.tck.tests.full.interceptors.contract.interceptorLifeCycle;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;

@WarriorBinding
@Interceptors(WarriorPDInterceptor.class)
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
    @Interceptors(MethodInterceptor.class)
    public void attack1() {
        weapon1.use();
    }

    @AttackBinding
    @Interceptors(MethodInterceptor.class)
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
