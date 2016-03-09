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
package org.jboss.cdi.tck.tests.extensions.afterBeanDiscovery;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessObserverMethod;

import org.jboss.cdi.tck.util.SimpleLogger;

public class AfterBeanDiscoveryObserver implements Extension {

    private static final SimpleLogger logger = new SimpleLogger(AfterBeanDiscoveryObserver.class);

    public static TestableObserverMethod<Talk> addedObserverMethod;

    public static boolean isTalkProcessObserverMethodObserved = false;

    public static boolean isProcessBeanFiredForCockatooBean = false;

    public void observeProcessBean(@Observes ProcessBean<Cockatoo> event) {
        isProcessBeanFiredForCockatooBean = true;
        assert event.getBean().getName().equals("cockatoo");
    }

    public void observeProcessObserverMethod(@Observes ProcessObserverMethod<Talk, Listener> event) {
        isTalkProcessObserverMethodObserved = true;
    }

    public void addABean(@Observes AfterBeanDiscovery event, BeanManager beanManager) {
        addBean(event, beanManager);
        addObserverMethod(event);
        event.addContext(new SuperContext());
    }

    private void addBean(AfterBeanDiscovery event, final BeanManager beanManager) {
        event.addBean(new Bean<Cockatoo>() {

            private final Set<Annotation> qualifiers = new HashSet<Annotation>(Arrays.asList(Default.Literal.INSTANCE));
            private final Set<Type> types = new HashSet<Type>(Arrays.<Type> asList(Cockatoo.class));

            public Class<?> getBeanClass() {
                return Cockatoo.class;
            }

            public Set<InjectionPoint> getInjectionPoints() {
                return Collections.emptySet();
            }

            public String getName() {
                return "cockatoo";
            }

            public Set<Annotation> getQualifiers() {
                return qualifiers;
            }

            public Class<? extends Annotation> getScope() {
                return Dependent.class;
            }

            public Set<Class<? extends Annotation>> getStereotypes() {
                return Collections.emptySet();
            }

            public Set<Type> getTypes() {
                return types;
            }

            public boolean isAlternative() {
                return false;
            }

            public boolean isNullable() {
                return true;
            }

            public Cockatoo create(CreationalContext<Cockatoo> creationalContext) {

                Cockatoo cockatoo = new Cockatoo("Billy");

                try {
                    // Try to lookup InjectionPoint metadata
                    AnnotatedType<Cockatoo> annotatedType = beanManager.createAnnotatedType(Cockatoo.class);
                    AnnotatedField<? super Cockatoo> injectionPointField = null;

                    for (AnnotatedField<? super Cockatoo> field : annotatedType.getFields()) {
                        if (field.getBaseType().equals(InjectionPoint.class)) {
                            injectionPointField = field;
                        }
                    }

                    if (injectionPointField != null) {
                        Object injectionPoint = beanManager.getInjectableReference(
                                beanManager.createInjectionPoint(injectionPointField), creationalContext);
                        if (injectionPoint != null) {
                            cockatoo.setInjectionPoint((InjectionPoint) injectionPoint);
                        }
                    }
                } catch (Exception e) {
                    logger.log("InjectionPoint is not available: {0}", e.getMessage());
                }
                return cockatoo;
            }

            public void destroy(Cockatoo instance, CreationalContext<Cockatoo> creationalContext) {
                // No-op
            }
        });
    }

    private void addObserverMethod(AfterBeanDiscovery event) {

        addedObserverMethod = new TestableObserverMethod<Talk>() {

            boolean observed = false;

            public void notify(Talk event) {
                observed = true;
            }

            public void notify(Talk event, Set<Annotation> qualifiers) {
                observed = true;
            }

            public boolean isObserved() {
                return observed;
            }

            public Class<?> getBeanClass() {
                return Listener.class;
            }

            public Set<Annotation> getObservedQualifiers() {
                return Collections.<Annotation> singleton(Any.Literal.INSTANCE);
            }

            public Type getObservedType() {
                return Talk.class;
            }

            public Reception getReception() {
                return Reception.ALWAYS;
            }

            public TransactionPhase getTransactionPhase() {
                return TransactionPhase.IN_PROGRESS;
            }

        };
        event.addObserverMethod(addedObserverMethod);
    }

}
