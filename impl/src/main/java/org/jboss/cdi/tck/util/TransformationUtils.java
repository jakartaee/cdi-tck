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
package org.jboss.cdi.tck.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class TransformationUtils {

    /**
     * @param <F> the input type of this function
     * @param <T> the output type of this function
     */
    public static interface Function<F, T> {
        /**
         * Returns the result of applying this function to {@code input}.
         */
        T apply(F input);
    }

    /**
     * Returns a list that applies {@code function} to each element of {@code fromCollection}.
     * 
     * @throws IllegalArgumentException in case of a null argument
     */
    public static <F, T> List<T> transform(final Function<? super F, ? extends T> function, final Collection<F> fromCollection) {
        checkNotNull(fromCollection);
        checkNotNull(function);
        List<T> result = new ArrayList<T>(fromCollection.size());
        for (F element : fromCollection) {
            result.add(function.apply(element));
        }
        return result;
    }

    /**
     * Returns a list that applies {@code function} to each element of {@code inputElements}.
     */
    @SafeVarargs
    public static <F, T> List<T> transform(final Function<? super F, ? extends T> function, final F... inputElements) {
        return transform(function, Arrays.asList(inputElements));
    }

    private static void checkNotNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("Null argument.");
        }
    }
}
