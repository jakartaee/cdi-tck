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
package org.jboss.cdi.tck.tests.full.extensions.annotated.delivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.WithAnnotations;

public class ProcessAnnotatedTypeObserver implements Extension {

    private List<Class<?>> processedDesiredTypes = new ArrayList<Class<?>>();
    private List<Class<?>> processedDesiredAndWantedTypes = new ArrayList<Class<?>>();
    private List<Class<?>> processedMetaAnnotationTypes = new ArrayList<Class<?>>();
    private List<Class<?>> processedRequestScopeTypes = new ArrayList<Class<?>>();

    public void observeDesiredTypes(@WithAnnotations({ Desired.class }) @Observes ProcessAnnotatedType<?> pat) {
        processedDesiredTypes.add(pat.getAnnotatedType().getJavaClass());
    }

    public void observeDesiredAndWanted(@WithAnnotations({ Desired.class, Wanted.class }) @Observes ProcessAnnotatedType<?> pat) {
        processedDesiredAndWantedTypes.add(pat.getAnnotatedType().getJavaClass());
    }

    public void observeMetaAnnotationTypes(@WithAnnotations({ MetaAnnotation.class }) @Observes ProcessAnnotatedType<?> pat) {
        processedMetaAnnotationTypes.add(pat.getAnnotatedType().getJavaClass());
    }

    public void observeRequestScopeTypes(@WithAnnotations({ RequestScoped.class }) @Observes ProcessAnnotatedType<?> pat) {
        processedRequestScopeTypes.add(pat.getAnnotatedType().getJavaClass());
    }

    public List<Class<?>> getProcessedDesiredTypes() {
        return Collections.unmodifiableList(processedDesiredTypes);
    }

    public List<Class<?>> getProcessedDesiredAndWantedTypes() {
        return Collections.unmodifiableList(processedDesiredAndWantedTypes);
    }

    public List<Class<?>> getProcessedMetaAnnotationTypes() {
        return Collections.unmodifiableList(processedMetaAnnotationTypes);
    }

    public List<Class<?>> getProcessedRequestScopeTypes() {
        return Collections.unmodifiableList(processedRequestScopeTypes);
    }

}
