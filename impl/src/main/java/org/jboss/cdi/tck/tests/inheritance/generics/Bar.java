/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.inheritance.generics;

import java.util.List;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Vetoed;
import jakarta.inject.Inject;

/**
 *
 */
@Vetoed
public class Bar<T1, T2> {

    @Inject
    private Baz<T1> baz;

    @Inject
    @Amazing
    private T1 t1;

    @Inject
    private Baz<List<T2>> t2BazList;

    private T1[] t1Array;

    private Baz<T1> t1BazEvent;

    private T1 t1ObserverInjectionPoint;

    @Inject
    public void setT1Array(T1[] t1Array) {
        this.t1Array = t1Array;
    }

    public void observeBaz(@Observes Baz<T1> baz, @Amazing T1 t1) {
        t1BazEvent = baz;
        t1ObserverInjectionPoint = t1;
    }

    public Baz<T1> getBaz() {
        return baz;
    }

    public Baz<List<T2>> getT2BazList() {
        return t2BazList;
    }

    public T1 getT1() {
        return t1;
    }

    public T1[] getT1Array() {
        return t1Array;
    }

    public Baz<T1> getT1BazEvent() {
        return t1BazEvent;
    }

    public T1 getT1ObserverInjectionPoint() {
        return t1ObserverInjectionPoint;
    }

}
