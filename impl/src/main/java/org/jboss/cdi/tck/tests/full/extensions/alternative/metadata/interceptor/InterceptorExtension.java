/*
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
package org.jboss.cdi.tck.tests.full.extensions.alternative.metadata.interceptor;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.util.AnnotationLiteral;

import org.jboss.cdi.tck.util.ForwardingAnnotatedType;


public class InterceptorExtension implements Extension {

    public void registerAdditionalLoginAnnotatedType(@Observes BeforeBeanDiscovery event, BeanManager manager) {
        final AnnotatedType<Login> interceptedLogin = manager.createAnnotatedType(Login.class);
        AnnotatedType<Login> modifiedInterceptedLogin = new ForwardingAnnotatedType<Login>() {
            @SuppressWarnings("serial")
            private final AnnotationLiteral<LoginInterceptorBinding> interceptorBinding = new LoginInterceptorBinding.Literal();
            @SuppressWarnings("serial")
            private final AnnotationLiteral<Secured> securedAnnotation = new Secured.Literal();

            @Override
            public AnnotatedType<Login> delegate() {
                return interceptedLogin;
            }

            @Override
            public Set<Annotation> getAnnotations() {
                Set<Annotation> annotations = new HashSet<Annotation>();
                annotations.add(interceptorBinding);
                annotations.add(securedAnnotation);
                return annotations;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <T extends Annotation> T getAnnotation(final Class<T> annType) {
                if (LoginInterceptorBinding.class.equals(annType)) {
                    return (T) interceptorBinding;
                }
                if (Secured.class.equals(annType)) {
                    return (T) securedAnnotation;
                }

                return null;
            }

            @Override
            public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
                return LoginInterceptorBinding.class.equals(annotationType) || Secured.class.equals(annotationType);
            }
        };
        event.addAnnotatedType(modifiedInterceptedLogin, Login.class.getSimpleName());
    }

}
