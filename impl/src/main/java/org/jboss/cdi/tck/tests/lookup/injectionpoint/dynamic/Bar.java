/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.lookup.injectionpoint.dynamic;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Inject;


/**
 * @author Martin Kouba
 */
@Dependent
public class Bar {

    @Inject
    @Any
    private Instance<Foo> fooInstance;

    @Inject
    @Any
    private transient Instance<Foo> transientFooInstance;

    private Instance<Foo> constructorInjectionFooInstance;

    private Instance<Foo> initializerInjectionFooInstance;

    @Inject
    public Bar(Instance<Foo> constructorInjectionFooInstance) {
        this.constructorInjectionFooInstance = constructorInjectionFooInstance;
    }

    @Inject
    public void setInitializerInjectionFooInstance(Instance<Foo> initializerInjectionFooInstance) {
        this.initializerInjectionFooInstance = initializerInjectionFooInstance;
    }

    public Foo getFoo() {
        return fooInstance.select(Default.Literal.INSTANCE).get();
    }

    public Foo getTypeNiceFoo() {
        return fooInstance.select(NiceFoo.class).get();
    }

    @SuppressWarnings("serial")
    public Foo getQualifierNiceFoo() {
        return fooInstance.select(new Nice.Literal()).get();
    }

    public Foo getConstructorInjectionFoo() {
        return constructorInjectionFooInstance.get();
    }

    public Foo getInitializerFoo() {
        return initializerInjectionFooInstance.get();
    }

    public Foo getTransientFoo() {
        return transientFooInstance.select(Default.Literal.INSTANCE).get();
    }

}
