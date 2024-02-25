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
package org.jboss.cdi.tck.tests.full.extensions.interceptionFactory.customBean;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.InterceptionFactory;

public class AfterBeanDiscoveryObserver implements Extension {

    void observeABD(@Observes AfterBeanDiscovery abd, BeanManager beanManager) {

        abd.addBean()
                .addType(Account.class)
                .addQualifier(Custom.CustomLiteral.INSTANCE)
                .<Account> createWith((cc) -> {
                    InterceptionFactory<Account> factory = beanManager.createInterceptionFactory(cc, Account.class);
                    factory.configure().add(FeeBinding.FeeLiteral.INSTANCE);
                    return factory.createInterceptedInstance(new Account());
                });
    }
}
