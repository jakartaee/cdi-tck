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
package org.jboss.cdi.tck.interceptors.tests.contract.lifecycleCallback;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

@AnimalBinding
@ApplicationScoped
class Hen extends Animal {
    protected static final String HEN = "Hen";

    private static boolean postConstructInterceptorCalled = false;
    private static boolean preDestroyInterceptorCalled = false;

    @PostConstruct
    public void postConstruct() {
        postConstructInterceptorCalled = true;
    }

    public String echo(String message) {
        return message;
    }

    @PreDestroy
    public void preDestroy() {
        preDestroyInterceptorCalled = true;
    }

    public static boolean isPostConstructInterceptorCalled() {
        return postConstructInterceptorCalled;
    }

    public static boolean isPreDestroyInterceptorCalled() {
        return preDestroyInterceptorCalled;
    }

    @Override
    public String getAnimalType() {
        return HEN;
    }
}
