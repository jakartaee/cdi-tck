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
package org.jboss.cdi.tck.tests.event.broken.raw;

import java.lang.reflect.Type;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionPoint;

import org.jboss.cdi.tck.util.ForwardingInjectionPoint;

public class ProcessInjectionPointObserver implements Extension {

    @SuppressWarnings("rawtypes")
    public void observeProcessInjectionPoint(@Observes ProcessInjectionPoint<Baz, Event> event, BeanManager beanManager) {
        event.setInjectionPoint(new BrokenInjectionPoint(event.getInjectionPoint()));
    }

    protected class BrokenInjectionPoint extends ForwardingInjectionPoint {

        private InjectionPoint delegate;

        public BrokenInjectionPoint(InjectionPoint injectionPoint) {
            this.delegate = injectionPoint;
        }

        @Override
        public Type getType() {
            return Event.class;
        }

        @Override
        protected InjectionPoint delegate() {
            return delegate;
        }

    }

}
