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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle.bbd;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.AfterDeploymentValidation;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;

public class ManagerObserver implements Extension {
    private static boolean afterBeanDiscoveryCalled = false;
    private static boolean afterDeploymentValidationCalled = false;

    public void managerInitialized(@Observes AfterBeanDiscovery event, BeanManager beanManager) {
        afterBeanDiscoveryCalled = true;
    }

    public void managerDeployed(@Observes AfterDeploymentValidation event, BeanManager beanManager) {
        assert afterBeanDiscoveryCalled : "AfterBeanDiscovery should have been called before AfterDeploymentValidation";
        afterDeploymentValidationCalled = true;
    }

    public static boolean isAfterBeanDiscoveryCalled() {
        return afterBeanDiscoveryCalled;
    }

    public static void reset() {
        ManagerObserver.afterBeanDiscoveryCalled = false;
        ManagerObserver.afterDeploymentValidationCalled = false;
    }

    public static boolean isAfterDeploymentValidationCalled() {
        return afterDeploymentValidationCalled;
    }

}
