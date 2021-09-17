/*
 * JBoss, Home of Professional Open Source
 * Copyright 2018, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd.massOperations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterTypeDiscovery;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class AfterTypeBeanDiscoveryMassOperationObserver implements Extension {

    private List<Class<?>> interceptors = null;
    private List<Class<?>> alternatives = null;
    private List<Class<?>> decorators = null;

    private boolean contains = false;
    private boolean containsAll = false;

    public void observeAfterTypeDiscovery(@Observes AfterTypeDiscovery event, BeanManager beanManager) {

        // gather initial information
        interceptors = Collections.unmodifiableList(new ArrayList<Class<?>>(event.getInterceptors()));
        alternatives = Collections.unmodifiableList(new ArrayList<Class<?>>(event.getAlternatives()));
        decorators = Collections.unmodifiableList(new ArrayList<Class<?>>(event.getDecorators()));

        // removeAll()
        Collection<Object> toBeRemoved = new ArrayList<Object>();
        toBeRemoved.add(AlphaAlternative.class);
        toBeRemoved.add(BetaAlternative.class);
        event.getAlternatives().removeAll(toBeRemoved);

        // retainAll()
        Collection<Object> toBeRetained = new ArrayList<Object>();
        toBeRetained.add(GammaDecorator.class);
        event.getDecorators().retainAll(toBeRetained);

        // verify contains() and containsAll()
        Collection<Object> shouldContain = new ArrayList<Object>();
        shouldContain.add(AlphaInterceptor.class);
        shouldContain.add(BetaInterceptor.class);
        containsAll = event.getInterceptors().containsAll(shouldContain);
        contains = event.getInterceptors().contains(GammaInterceptor.class);
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

    public boolean containsWorks() {
        return contains;
    }

    public boolean containsAllWorks() {
        return containsAll;
    }
}
