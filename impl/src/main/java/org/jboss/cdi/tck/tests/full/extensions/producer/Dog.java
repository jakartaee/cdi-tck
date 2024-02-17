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
package org.jboss.cdi.tck.tests.full.extensions.producer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;

public class Dog {
    private static boolean constructorCalled = false;
    private static boolean postConstructCalled;
    private static boolean preDestroyCalled;
    private static DogBed dogBed;

    public static final String DEFAULT_COLOR = "Brown";
    private String color = DEFAULT_COLOR;

    @Inject
    private DogBone dogBone;

    public Dog() {
        constructorCalled = true;
    }

    public Dog(String color) {
        this.color = color;
    }

    @Inject
    public void init(DogBed dogBed) {
        Dog.dogBed = dogBed;
    }

    @PostConstruct
    public void postConstruct() {
        postConstructCalled = true;
    }

    @PreDestroy
    public void preDestroy() {
        preDestroyCalled = true;
    }

    public static boolean isConstructorCalled() {
        return constructorCalled;
    }

    public static void setConstructorCalled(boolean constructorCalled) {
        Dog.constructorCalled = constructorCalled;
    }

    public static DogBed getDogBed() {
        return dogBed;
    }

    public static void setDogBed(DogBed dogBed) {
        Dog.dogBed = dogBed;
    }

    public String getColor() {
        return color;
    }

    public static boolean isPostConstructCalled() {
        return postConstructCalled;
    }

    public static void setPostConstructCalled(boolean postConstructCalled) {
        Dog.postConstructCalled = postConstructCalled;
    }

    public static boolean isPreDestroyCalled() {
        return preDestroyCalled;
    }

    public static void setPreDestroyCalled(boolean preDestroyCalled) {
        Dog.preDestroyCalled = preDestroyCalled;
    }

    public DogBone getDogBone() {
        return dogBone;
    }

    public void setDogBone(DogBone dogBone) {
        this.dogBone = dogBone;
    }
}
