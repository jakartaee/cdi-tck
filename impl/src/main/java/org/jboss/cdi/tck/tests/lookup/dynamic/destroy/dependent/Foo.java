/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.tests.lookup.dynamic.destroy.dependent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class Foo {

    private static final AtomicInteger generator = new AtomicInteger();

    private static final List<Foo> destroyedComponents = Collections.synchronizedList(new ArrayList<Foo>());

    private final int id;

    @Inject
    Qux qux;

    @Inject
    Baz baz;

    public Foo() {
        this.id = generator.incrementAndGet();
    }

    public void ping() {
        qux.ping();
        baz.ping();
    }

    public int getId() {
        return id;
    }

    public Qux getQux() {
        return qux;
    }

    public Baz getBaz() {
        return baz;
    }

    @PreDestroy
    void destroy() {
        destroyedComponents.add(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Foo))
            return false;
        Foo other = (Foo) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public static List<Foo> getDestroyedComponents() {
        return destroyedComponents;
    }

    public static void reset() {
        destroyedComponents.clear();
    }

}
