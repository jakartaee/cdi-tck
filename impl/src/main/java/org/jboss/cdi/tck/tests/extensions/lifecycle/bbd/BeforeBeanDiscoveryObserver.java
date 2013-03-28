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
package org.jboss.cdi.tck.tests.extensions.lifecycle.bbd;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.Nonbinding;

import org.jboss.cdi.tck.tests.extensions.lifecycle.bbd.lib.Boss;
import org.jboss.cdi.tck.util.AddForwardingAnnotatedTypeAction;
import org.jboss.cdi.tck.util.annotated.AnnotatedMethodWrapper;
import org.jboss.cdi.tck.util.annotated.AnnotatedTypeWrapper;

/**
 * @author pmuir
 * @author Martin Kouba
 */
public class BeforeBeanDiscoveryObserver implements Extension {

    private static boolean observed;

    /**
     * @return the observed
     */
    public static boolean isObserved() {
        return observed;
    }

    /**
     * @param observed the observed to set
     */
    public static void setObserved(boolean observed) {
        BeforeBeanDiscoveryObserver.observed = observed;
    }

    public void addScope(@Observes BeforeBeanDiscovery beforeBeanDiscovery) {
        setObserved(true);
        beforeBeanDiscovery.addScope(EpochScoped.class, false, false);
    }

    public void addQualifierByClass(@Observes BeforeBeanDiscovery beforeBeanDiscovery) {
        setObserved(true);
        beforeBeanDiscovery.addQualifier(Tame.class);
    }

    public void addQualifierByAnnotatedType(@Observes BeforeBeanDiscovery beforeBeanDiscovery, BeanManager beanManager) {
        setObserved(true);

        // add @Skill(language(); @Nonbinding level()) as qualifier
        beforeBeanDiscovery.addQualifier(new AnnotatedTypeWrapper<Skill>(beanManager.createAnnotatedType(Skill.class), true) {
            Set<AnnotatedMethod<? super Skill>> methods;

            {
                methods = new HashSet<AnnotatedMethod<? super Skill>>();
                for (final AnnotatedMethod<? super Skill> method : super.getMethods()) {
                    if ("level".equals(method.getJavaMember().getName())) {
                        methods.add(new AnnotatedMethodWrapper<Skill>((AnnotatedMethod<Skill>) method, true,
                                new AnnotationLiteral<Nonbinding>() {
                                }));
                    } else {
                        methods.add(method);
                    }
                }
            }

            @Override
            public Set<AnnotatedMethod<? super Skill>> getMethods() {
                return methods;
            }
        });
    }

    public void addAnnotatedType(@Observes BeforeBeanDiscovery event, final BeanManager beanManager) {

        new AddForwardingAnnotatedTypeAction<Boss>() {

            @Override
            public String getBaseId() {
                return BeforeBeanDiscoveryObserver.class.getName();
            }

            @Override
            public AnnotatedType<Boss> delegate() {
                return new AnnotatedTypeWrapper<Boss>(beanManager.createAnnotatedType(Boss.class), true);
            }

        }.perform(event);

    }

}