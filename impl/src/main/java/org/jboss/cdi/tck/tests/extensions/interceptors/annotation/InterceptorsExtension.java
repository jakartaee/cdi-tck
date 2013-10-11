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
package org.jboss.cdi.tck.tests.extensions.interceptors.annotation;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.util.AnnotationLiteral;
import javax.interceptor.Interceptors;

import org.jboss.cdi.tck.util.ForwardingAnnotatedType;

public class InterceptorsExtension implements Extension {

    void registerCustomInterceptor(@Observes ProcessAnnotatedType<SimpleBean> pat) {
        final AnnotatedType<SimpleBean> oldAnnotatedType = pat.getAnnotatedType();

        AnnotatedType<SimpleBean> modifiedSimpleAnnotatedType = new ForwardingAnnotatedType<SimpleBean>() {
            private final AnnotationLiteral<Interceptors> interceptorsAnnotation = new InterceptorsLiteral();

            @Override
            public AnnotatedType<SimpleBean> delegate() {
                return oldAnnotatedType;
            }

            @Override
            public Set<Annotation> getAnnotations() {
                Set<Annotation> annotations = new HashSet<Annotation>();
                annotations.add(interceptorsAnnotation);
                return annotations;
            }

            @Override
            public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
                return Interceptors.class.equals(annotationType);
            }
        };

        pat.setAnnotatedType(modifiedSimpleAnnotatedType);


    }

}
