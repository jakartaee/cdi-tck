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
package org.jboss.cdi.tck.tests.full.extensions.producer;

import jakarta.inject.Inject;

public class Cat implements Speakable{
    private static boolean constructorCalled;
    private static boolean initializerCalled;

    @Inject
    protected CatFoodDish foodDish;

    private Bird bird;
    
    public Cat() {
    }

    @Inject
    public Cat(LitterBox litterBox) {
        assert litterBox != null;
        constructorCalled = true;
    }

    @Inject
    public void setBird(Bird bird) {
        assert bird != null;
        initializerCalled = true;
    }

    public static boolean isConstructorCalled() {
        return constructorCalled;
    }

    public static boolean isInitializerCalled() {
        return initializerCalled;
    }

    public static void reset() {
        Cat.constructorCalled = false;
        Cat.initializerCalled = false;
    }

    public void ping() {

    }
    
    @CatSpectator
    @CatHolder
    public int foo() {
        return 0;
    }

    @Override
    public String saySomething() {
        return "Meow";
    }
}
