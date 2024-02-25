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
package org.jboss.cdi.tck.tests.full.interceptors.definition.custom;

import static jakarta.enterprise.inject.spi.InterceptionType.AROUND_INVOKE;
import static jakarta.enterprise.inject.spi.InterceptionType.AROUND_TIMEOUT;
import static jakarta.enterprise.inject.spi.InterceptionType.POST_ACTIVATE;
import static jakarta.enterprise.inject.spi.InterceptionType.POST_CONSTRUCT;
import static jakarta.enterprise.inject.spi.InterceptionType.PRE_DESTROY;
import static jakarta.enterprise.inject.spi.InterceptionType.PRE_PASSIVATE;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;

public class AfterBeanDiscoveryObserver implements Extension {
    public static final CustomInterceptorImplementation POST_CONSTRUCT_INTERCEPTOR = new CustomInterceptorImplementation(
            POST_CONSTRUCT);
    public static final CustomInterceptorImplementation PRE_DESTROY_INTERCEPTOR = new CustomInterceptorImplementation(
            PRE_DESTROY);
    public static final CustomInterceptorImplementation POST_ACTIVATE_INTERCEPTOR = new CustomInterceptorImplementation(
            POST_ACTIVATE);
    public static final CustomInterceptorImplementation PRE_PASSIVATE_INTERCEPTOR = new CustomInterceptorImplementation(
            PRE_PASSIVATE);
    public static final CustomInterceptorImplementation AROUND_INVOKE_INTERCEPTOR = new CustomInterceptorImplementation(
            AROUND_INVOKE);
    public static final CustomInterceptorImplementation AROUND_TIMEOUT_INTERCEPTOR = new CustomInterceptorImplementation(
            AROUND_TIMEOUT);

    public void addInterceptors(@Observes AfterBeanDiscovery event) {
        event.addBean(POST_CONSTRUCT_INTERCEPTOR);
        event.addBean(PRE_DESTROY_INTERCEPTOR);
        event.addBean(POST_ACTIVATE_INTERCEPTOR);
        event.addBean(PRE_PASSIVATE_INTERCEPTOR);
        event.addBean(AROUND_INVOKE_INTERCEPTOR);
        event.addBean(AROUND_TIMEOUT_INTERCEPTOR);
    }
}
