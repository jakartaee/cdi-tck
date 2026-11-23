/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
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
package org.jboss.cdi.tck.tests.full.interceptors.contract.invocationContext.atInterceptors;

import jakarta.enterprise.context.Dependent;
import jakarta.interceptor.Interceptors;

/**
 * Bean that associates interceptors via {@link Interceptors} so that
 * {@link jakarta.interceptor.InvocationContext} assertions can be validated
 * for that association path as well as interceptor bindings.
 */
@Interceptors(LifecycleInterceptor.class)
@Dependent
class AtInterceptorsBean {

    private int id = 0;
    private static boolean echoCalled = false;

    @Interceptors(GetTargetInterceptor.class)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Interceptors(GetTimerInterceptor.class)
    public boolean testGetTimer() {
        return false;
    }

    @Interceptors(GetMethodInterceptor.class)
    public boolean testGetMethod() {
        return false;
    }

    @Interceptors(MethodParametersInterceptor.class)
    public int add(int i, int j) {
        return i + j;
    }

    @Interceptors(IllegalNumberOfParametersInterceptor.class)
    public int add2(int i, int j) {
        return i + j;
    }

    @Interceptors(IllegalTypeOfParametersInterceptor.class)
    public int add3(int i, int j) {
        return i + j;
    }

    @Interceptors(ProceedReturnsNullInterceptor.class)
    public void voidMethod() {
    }

    @Interceptors({ ContextDataInterceptor1.class, ContextDataInterceptor2.class })
    public void foo() {
    }

    @Interceptors(NoProceedInterceptor.class)
    public String echo(String s) {
        echoCalled = true;
        return s;
    }

    public static boolean isEchoCalled() {
        return echoCalled;
    }

    public static void reset() {
        echoCalled = false;
    }
}
