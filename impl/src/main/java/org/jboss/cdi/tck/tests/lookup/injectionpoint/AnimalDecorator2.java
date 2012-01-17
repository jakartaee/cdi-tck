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
package org.jboss.cdi.tck.tests.lookup.injectionpoint;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

@Decorator
public abstract class AnimalDecorator2 implements Animal {

    @Inject
    private InjectionPoint injectedMetadata;

    @Inject
    @Delegate
    private Animal animal;

    @Inject
    private Toy toy;

    public String hello() {
        return animal.hello() + "!";
    }

    @Override
    public InjectionPoint getDecorator2InjectionPoint() {
        return injectedMetadata;
    }

    @Override
    public Toy getToy() {
        return toy;
    }
}
