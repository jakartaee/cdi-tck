/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.lifecycle.atd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterTypeDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.jboss.cdi.tck.tests.extensions.lifecycle.atd.lib.Boss;

/**
 *
 * @author Martin Kouba
 */
public class AfterTypeDiscoveryObserver implements Extension {

    private List<Class<?>> interceptors = null;
    private List<Class<?>> alternatives = null;
    private List<Class<?>> decorators = null;
    private boolean bossObserved = false;

    public void observeAfterTypeDiscovery(@Observes AfterTypeDiscovery event, BeanManager beanManager) {

        interceptors = Collections.unmodifiableList(new ArrayList<Class<?>>(event.getInterceptors()));
        alternatives = Collections.unmodifiableList(new ArrayList<Class<?>>(event.getAlternatives()));
        decorators = Collections.unmodifiableList(new ArrayList<Class<?>>(event.getDecorators()));

        event.addAnnotatedType(beanManager.createAnnotatedType(Boss.class), AfterTypeDiscoveryObserver.class.getName());

        // Bravo interceptor removed
        for (Iterator<Class<?>> iterator = event.getInterceptors().iterator(); iterator.hasNext();) {
            if(BravoInterceptor.class.equals(iterator.next())) {
                iterator.remove();
            }
        }
        // The order of decorators reverted
        Collections.reverse(event.getDecorators());
        // No selected alternative
        event.getAlternatives().clear();
    }

    public void observeBossAnnotatedType(@Observes ProcessAnnotatedType<Boss> event) {
        bossObserved = true;
    }

    public List<Class<?>> getInterceptors() {
        return interceptors;
    }

    public List<Class<?>> getAlternatives() {
        return alternatives;
    }

    public List<Class<?>> getDecorators() {
        return decorators;
    }

    public boolean isBossObserved() {
        return bossObserved;
    }

}
