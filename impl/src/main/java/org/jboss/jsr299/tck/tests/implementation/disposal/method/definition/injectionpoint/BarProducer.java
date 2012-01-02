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
package org.jboss.jsr299.tck.tests.implementation.disposal.method.definition.injectionpoint;

import java.lang.reflect.Member;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class BarProducer {

    private static Bar disposedBar;
    private static Member disposedInjectionPointMember;
    private static Member producedInjectionPointMember;

    @Produces
    public Bar getBar(InjectionPoint injectionPoint) {
        producedInjectionPointMember = injectionPoint.getMember();
        return new Bar("blah");
    }

    public void dispose(@Disposes @Any Bar bar, InjectionPoint injectionPoint) {
        disposedBar = bar;
        disposedInjectionPointMember = injectionPoint.getMember();
    }

    public static Bar getDisposedBar() {
        return disposedBar;
    }

    public static Member getDisposedInjectionPointMember() {
        return disposedInjectionPointMember;
    }

    public static Member getProducedInjectionPointMember() {
        return producedInjectionPointMember;
    }

    public static void reset() {
        disposedBar = null;
        disposedInjectionPointMember = null;
        producedInjectionPointMember = null;
    }
}
