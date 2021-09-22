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

package org.jboss.cdi.tck.tests.event.resolve.typeWithParameters;

import java.util.List;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;

@Dependent
public class WildcardObserver extends AbstractObserver {

    public static final String SEQUENCE = "quxWildcard";

    public static final String SEQUENCE_UPPER = "quxWildcardUpper";

    public static final String SEQUENCE_LOWER = "quxWildcardLower";

    public void observeTypeParameterWildcard(@Observes Qux<?> qux) {
        addAction(SEQUENCE, qux);
    }

    @SuppressWarnings("rawtypes")
    public void observeTypeParameterWildcardUpper(@Observes Qux<? extends List> qux) {
        addAction(SEQUENCE_UPPER, qux);
    }

    public void observeTypeParameterWildcardLower(@Observes Qux<? super Integer> qux) {
        addAction(SEQUENCE_LOWER, qux);
    }

}
