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
package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class ProcessAnnotatedTypeObserver implements Extension {
    private static ProcessAnnotatedType<TestServlet> servletEvent = null;
    private static ProcessAnnotatedType<TestListener> listenerEvent = null;
    private static ProcessAnnotatedType<TestFilter> filterEvent = null;
    private static ProcessAnnotatedType<TagLibraryListener> tagLibraryListenerEvent = null;
    private static ProcessAnnotatedType<TestTagHandler> tagHandlerEvent = null;
    private static ProcessAnnotatedType<Farm> jsfManagedBeanEvent = null;

    public void cleanup(@Observes BeforeShutdown shutdown) {
        servletEvent = null;
        listenerEvent = null;
        filterEvent = null;
        tagLibraryListenerEvent = null;
        tagHandlerEvent = null;
        jsfManagedBeanEvent = null;
    }

    public void observeServlet(@Observes ProcessAnnotatedType<TestServlet> event) {
        servletEvent = event;
    }

    public void observeFilter(@Observes ProcessAnnotatedType<TestFilter> event) {
        filterEvent = event;
    }

    public void observeListener(@Observes ProcessAnnotatedType<TestListener> event) {
        listenerEvent = event;
    }

    public void observeTagHandler(@Observes ProcessAnnotatedType<TestTagHandler> event) {
        tagHandlerEvent = event;
    }

    public void observeTagLibraryListener(@Observes ProcessAnnotatedType<TagLibraryListener> event) {
        tagLibraryListenerEvent = event;
    }

    public void observeJsfManagedBean(@Observes ProcessAnnotatedType<Farm> event) {
        jsfManagedBeanEvent = event;
    }

    public static ProcessAnnotatedType<TestServlet> getServletEvent() {
        return servletEvent;
    }

    public static ProcessAnnotatedType<TestListener> getListenerEvent() {
        return listenerEvent;
    }

    public static ProcessAnnotatedType<TestFilter> getFilterEvent() {
        return filterEvent;
    }

    public static ProcessAnnotatedType<TagLibraryListener> getTagLibraryListenerEvent() {
        return tagLibraryListenerEvent;
    }

    public static ProcessAnnotatedType<TestTagHandler> getTagHandlerEvent() {
        return tagHandlerEvent;
    }

    public static ProcessAnnotatedType<Farm> getJsfManagedBeanEvent() {
        return jsfManagedBeanEvent;
    }
}
