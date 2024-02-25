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
package org.jboss.cdi.tck.tests.lookup.injection.enterprise;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class InjectedSessionBean extends SuperInjectedSessionBean implements InjectedSessionBeanLocal {
    @EJB
    private FarmLocal farm;
    private boolean initializerCalledAfterResourceInjection = false;

    public FarmLocal getFarm() {
        return farm;
    }

    @Inject
    public void initialize() {
        initializerCalledAfterResourceInjection = (farm != null && deluxeHenHouse != null);
    }

    public boolean isInitializerCalledAfterResourceInjection() {
        return initializerCalledAfterResourceInjection;
    }
}
