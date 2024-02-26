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
package org.jboss.cdi.tck.tests.event.broken.observer.isDisposer;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;

@Dependent
public class FoxTerrier_Broken {
    /*
     * (non-Javadoc)
     *
     * @see org.jboss.cdi.tck.unit.event.broken.observer6.Terrier#observeInitialized(jakarta.inject.manager.Manager,
     * java.lang.String)
     */
    public void observeInitialized(@Observes BeforeBeanDiscovery event, @Disposes String badParam) {
    }

}
