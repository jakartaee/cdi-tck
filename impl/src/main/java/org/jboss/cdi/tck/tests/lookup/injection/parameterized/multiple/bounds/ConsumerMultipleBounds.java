/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.lookup.injection.parameterized.multiple.bounds;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

class ConsumerMultipleBounds<T extends BarImpl & Baz & Foo, U extends Bar & Baz> {

    @Inject
    @Default
    private GenericInterface<T> giBarBazFooImpl;

    @Inject
    @BarFooQualifier
    private GenericInterface<T> giBarFooImpl;

    @Inject
    @BazQualifier
    private GenericInterface<T> giSuperBazImpl;

    @Inject
    @Any
    private GenericInterface<BarFooImpl> giBarFooImpl2;

    @Inject
    @Any
    private GenericInterface<? super U> giSuperBazImpl2;

    public GenericInterface<T> getGenericInterfaceBarFooImpl() {
        return giBarFooImpl;
    }

    public GenericInterface<T> getGenericInterfaceBarBazFooImpl() {
        return giBarBazFooImpl;
    }

    public GenericInterface<T> getGenericInterfaceSuperBazImpl() {
        return giSuperBazImpl;
    }

    public GenericInterface<BarFooImpl> getGenericInterfaceBarFooImpl2() {
        return giBarFooImpl2;
    }

    public GenericInterface<? super U> getGenericInterfaceSuperBazImpl2() {
        return giSuperBazImpl2;
    }
}
