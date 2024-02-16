/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.implementation.simple.definition.ee;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessManagedBean;
import org.jboss.cdi.tck.tests.implementation.simple.definition.Sheep;

public class EnterpriseBeanObserver implements Extension {

    public static boolean observedEnterpriseBean;
    public static boolean observedAnotherBean;

    public void observeAnotherBean(@Observes ProcessManagedBean<Sheep> event) {
        observedAnotherBean = true;
    }

    public void observeEnterpriseBean(@Observes ProcessManagedBean<MockEnterpriseBean> event) {
        observedEnterpriseBean = true;
    }

}
