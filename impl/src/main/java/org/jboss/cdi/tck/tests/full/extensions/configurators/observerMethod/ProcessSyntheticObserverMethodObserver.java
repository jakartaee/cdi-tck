/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.configurators.observerMethod;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessSyntheticObserverMethod;

/**
 * Verify that observer for synthetic OM was invoked and that the source was AfterBeanDiscoveryObserver.
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class ProcessSyntheticObserverMethodObserver implements Extension {

    private Integer timesInvoked = new Integer(0);
    private Map<Type, Extension> extensionSources = new HashMap<>();

    void observesAllSyntheticOM(@Observes ProcessSyntheticObserverMethod<?, FruitObserver> event) {
        timesInvoked += 1;
        extensionSources.put(event.getObserverMethod().getObservedType(), event.getSource());
    }

    public Map<Type, Extension> getSources() {
        return extensionSources;
    }

    public Integer timesInvoked() {
        return timesInvoked;
    }
}
