/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.lookup.injectionpoint.dynamic.event;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@RequestScoped
public class Notifier {

    @Inject
    InfoObserver infoObserver;

    @Inject
    private Event<Info> infoEvent;

    @Inject
    private transient Event<Info> transientInfoEvent;

    private Event<Info> constructorInjectionInfoEvent;

    private Event<Info> initializerInjectionInfoEvent;

    public Notifier() {
    }

    @Inject
    public Notifier(@Nice Event<Info> constructorInjectionInfoEvent) {
        this.constructorInjectionInfoEvent = constructorInjectionInfoEvent;
    }

    @Inject
    public void setInitializerInjectionInfoEvent(Event<Info> initializerInjectionInfoEvent) {
        this.initializerInjectionInfoEvent = initializerInjectionInfoEvent;
    }

    public void fireInfoEvent() {
        infoObserver.reset();
        infoEvent.fire(new Info());
    }

    public void fireTransientInfoEvent() {
        infoObserver.reset();
        transientInfoEvent.fire(new Info());
    }

    public void fireConstructorInfoEvent() {
        infoObserver.reset();
        constructorInjectionInfoEvent.fire(new Info());
    }

    public void fireInitializerInfoEvent() {
        infoObserver.reset();
        initializerInjectionInfoEvent.fire(new Info());
    }
}

