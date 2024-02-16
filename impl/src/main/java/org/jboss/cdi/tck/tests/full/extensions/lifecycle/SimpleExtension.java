/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

/**
 * @author Martin Kouba
 */
public class SimpleExtension implements Extension, SuperExtension {

    private long id;

    private boolean isContainerEventObserved = false;

    private boolean isSimpleEventObserved = false;

    public SimpleExtension() {
        id = System.currentTimeMillis();
    }

    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager beanManager) throws SecurityException,
            NoSuchMethodException {
        isContainerEventObserved = true;
    }

    public void observeSimpleEvent(@Observes SimpleEvent event, BeanManager beanManager) {
        isSimpleEventObserved = true;
    }

    public boolean isContainerEventObserved() {
        return isContainerEventObserved;
    }

    public boolean isSimpleEventObserved() {
        return isSimpleEventObserved;
    }

    public long getId() {
        return id;
    }

}
