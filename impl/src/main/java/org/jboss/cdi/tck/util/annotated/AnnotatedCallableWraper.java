/*
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.util.annotated;

import jakarta.enterprise.inject.spi.AnnotatedCallable;
import jakarta.enterprise.inject.spi.AnnotatedParameter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnnotatedCallableWraper<X> extends AnnotatedMemberWrapper<X> implements AnnotatedCallable<X> {

    private AnnotatedCallable<X> delegate;
    private List<AnnotatedParameter<X>> wrappedParameters;

    public AnnotatedCallableWraper(AnnotatedCallable<X> delegate, AnnotatedTypeWrapper<X> declaringType, boolean keepOriginalAnnotations,
            Annotation... annotations) {
        super(delegate, declaringType, keepOriginalAnnotations, annotations);
        this.delegate = delegate;
        this.wrappedParameters = new ArrayList<AnnotatedParameter<X>>();

        for (AnnotatedParameter<X> annotatedParameter : delegate.getParameters()) {
            wrappedParameters
                    .add(new AnnotatedParameterWrapper<X>(annotatedParameter, this, true, annotatedParameter.getAnnotations().toArray(
                            new Annotation[] { })));
        }
    }

    public List<AnnotatedParameter<X>> getParameters() {
        return wrappedParameters;
    }

    public AnnotatedCallableWraper<X> replaceParameters(AnnotatedParameter<X>... parameters) {
        this.wrappedParameters = Arrays.asList(parameters);
        return this;
    }

}
