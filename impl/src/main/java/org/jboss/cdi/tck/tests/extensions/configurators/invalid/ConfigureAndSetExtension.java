/*
 * JBoss, Home of Professional Open Source
 * Copyright 2017, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.configurators.invalid;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.BeanAttributes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;
import jakarta.enterprise.inject.spi.ProcessInjectionPoint;
import jakarta.enterprise.inject.spi.ProcessObserverMethod;

import org.jboss.cdi.tck.util.ForwardingInjectionPoint;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class ConfigureAndSetExtension implements Extension {

    public static boolean PAT_ISE_CAUGHT = false;
    public static boolean PAT_REVERSE_ISE_CAUGHT = false;
    public static boolean PBA_ISE_CAUGHT = false;
    public static boolean PBA_REVERSE_ISE_CAUGHT = false;
    public static boolean PIP_ISE_CAUGHT = false;
    public static boolean PIP_REVERSE_ISE_CAUGHT = false;
    public static boolean POM_ISE_CAUGHT = false;
    public static boolean POM_REVERSE_ISE_CAUGHT = false;

    void observePAT(@Observes ProcessAnnotatedType<Box> event) {
        event.configureAnnotatedType().add(Sorted.SortedLiteral.INSTANCE);
        try {
            event.setAnnotatedType(new TestAnnotatedType<>(event.getAnnotatedType()));
        } catch (IllegalStateException e) {
            // expected should blow up with ISE
            PAT_ISE_CAUGHT = true;
        }
    }

    void observePATReverse(@Observes ProcessAnnotatedType<Box> event) {
        event.setAnnotatedType(new TestAnnotatedType<>(event.getAnnotatedType()));
        try {
            event.configureAnnotatedType().add(Sorted.SortedLiteral.INSTANCE);
        } catch (IllegalStateException e) {
            // expected should blow up with ISE
            PAT_REVERSE_ISE_CAUGHT = true;
        }
    }

    void observePBA(@Observes ProcessBeanAttributes<Box> event) {
        event.configureBeanAttributes().name("test");
        try {
            event.setBeanAttributes(new BeanAttributes<Box>() {

                @SuppressWarnings("unchecked")
                public Set<Type> getTypes() {
                    return Collections.unmodifiableSet(new HashSet<Type>(Arrays.asList(Object.class, Box.class)));
                }

                public Set<Annotation> getQualifiers() {
                    return Collections.unmodifiableSet(new HashSet<Annotation>(Arrays.asList(Any.Literal.INSTANCE, Default.Literal.INSTANCE)));
                }

                public Class<? extends Annotation> getScope() {
                    return ApplicationScoped.class;
                }

                public String getName() {
                    return "definitelyNotABox";
                }

                public Set<Class<? extends Annotation>> getStereotypes() {
                    return Collections.<Class<? extends Annotation>>emptySet();
                }

                public boolean isAlternative() {
                    return true;
                }
            });
        } catch (IllegalStateException e) {
            // expected should blow up with ISE
            PBA_ISE_CAUGHT = true;
        }
    }

    void observePBAReverse(@Observes ProcessBeanAttributes<Box> event) {
        event.setBeanAttributes(new BeanAttributes<Box>() {

            @SuppressWarnings("unchecked")
            public Set<Type> getTypes() {
                return Collections.unmodifiableSet(new HashSet<Type>(Arrays.asList(Object.class, Box.class)));
            }

            public Set<Annotation> getQualifiers() {
                return Collections.unmodifiableSet(new HashSet<Annotation>(Arrays.asList(Any.Literal.INSTANCE, Default.Literal.INSTANCE)));
            }

            public Class<? extends Annotation> getScope() {
                return Dependent.class;
            }

            public String getName() {
                return "box";
            }

            public Set<Class<? extends Annotation>> getStereotypes() {
                return Collections.<Class<? extends Annotation>>emptySet();
            }

            public boolean isAlternative() {
                return false;
            }
        });
        try {
            event.configureBeanAttributes().name("test");
        } catch (IllegalStateException e) {
            // expected should blow up with ISE
            PBA_REVERSE_ISE_CAUGHT = true;
        }
    }

    void observePIP(@Observes ProcessInjectionPoint<Warehouse, Box> event) {
        event.configureInjectionPoint().addQualifier(Any.Literal.INSTANCE);

        final InjectionPoint delegate = event.getInjectionPoint();
        try {
            event.setInjectionPoint(new ForwardingInjectionPoint() {

                @Override
                protected InjectionPoint delegate() {
                    return delegate;
                }

                @Override
                public boolean isTransient() {
                    return true;
                }

                @Override
                public Type getType() {
                    return Box.class;
                }

                @Override
                public Set<Annotation> getQualifiers() {
                    return Collections.unmodifiableSet(new HashSet<Annotation>(Arrays.asList(Any.Literal.INSTANCE)));
                }
            });
        } catch (IllegalStateException e) {
            // expected should blow up with ISE
            PIP_ISE_CAUGHT = true;
        }
    }

    void observePIPReverse(@Observes ProcessInjectionPoint<Warehouse, Box> event) {
        final InjectionPoint delegate = event.getInjectionPoint();
        event.setInjectionPoint(new ForwardingInjectionPoint() {

            @Override
            protected InjectionPoint delegate() {
                return delegate;
            }

            @Override
            public boolean isTransient() {
                return true;
            }

            @Override
            public Type getType() {
                return Box.class;
            }

            @Override
            public Set<Annotation> getQualifiers() {
                return Collections.unmodifiableSet(new HashSet<Annotation>(Arrays.asList(Any.Literal.INSTANCE)));
            }
        });
        try {
            event.configureInjectionPoint().addQualifiers(Sorted.SortedLiteral.INSTANCE);
        } catch (IllegalStateException e) {
            // expected should blow up with ISE
            PIP_REVERSE_ISE_CAUGHT = true;
        }
    }

    void observePOM(@Observes ProcessObserverMethod<Box, Worker> event) {
        event.configureObserverMethod().async(true);
        try {
            event.setObserverMethod(new BoxObserverMethod());
        } catch (IllegalStateException e) {
            // expected should blow up with ISE
            POM_ISE_CAUGHT = true;
        }
    }

    void observePOMReverse(@Observes ProcessObserverMethod<Box, Worker> event) {
        event.setObserverMethod(new BoxObserverMethod());
        try {
            event.configureObserverMethod().async(true);
        } catch (IllegalStateException e) {
            // expected should blow up with ISE
            POM_REVERSE_ISE_CAUGHT = true;
        }
    }
}
