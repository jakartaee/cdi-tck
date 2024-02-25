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
package org.jboss.cdi.tck.tests.full.decorators.invocation.producer.method;

import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

/**
 * @author pmuir
 *
 */
public class ProducerImpl implements Producer {

    private static boolean disposedCorrectly = false;

    /**
     */
    public static void reset() {
        disposedCorrectly = false;
    }

    @Produces
    public Foo produce() {
        return new Foo("foo!");
    }

    public void dispose(@Disposes Foo foo) {
        disposedCorrectly = foo.getFoo().equals("decorated");
    }

    /**
     * @return the disposedCorrectly
     */
    public static boolean isDisposedCorrectly() {
        return disposedCorrectly;
    }

}
