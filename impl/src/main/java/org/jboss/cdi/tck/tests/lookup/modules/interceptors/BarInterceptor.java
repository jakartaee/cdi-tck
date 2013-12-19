/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.cdi.tck.tests.lookup.modules.interceptors;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@SuppressWarnings("serial")
@BarBinding
@Priority(1000)
@Interceptor
public class BarInterceptor extends BarSuperInterceptor implements Serializable {

    private static boolean interceptorCalled = false;

    private static Animal animal;


    public static boolean isInterceptorCalled() {
        return interceptorCalled;
    }

    @Inject
    public void setAnimal(Animal animal) {
        BarInterceptor.animal = animal;
    }

    public static Animal getAnimal() {
        return animal;
    }

    public static void reset() {
        interceptorCalled = false;
    }

    @PostConstruct
    public void intercept(InvocationContext ctx) {
        interceptorCalled = true;
        try {
            ctx.proceed();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
