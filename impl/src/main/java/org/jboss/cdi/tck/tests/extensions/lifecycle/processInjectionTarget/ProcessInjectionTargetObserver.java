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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processInjectionTarget;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ProcessInjectionTargetObserver implements Extension {

    private static ProcessInjectionTarget<TestServlet> servletEvent = null;
    private static ProcessInjectionTarget<TestListener> listenerEvent = null;
    private static ProcessInjectionTarget<TestFilter> filterEvent = null;
    private static ProcessInjectionTarget<TagLibraryListener> tagLibraryListenerEvent = null;
    private static ProcessInjectionTarget<TestTagHandler> tagHandlerEvent = null;

    private static boolean servletSuperTypeObserved = false;
    private static boolean servletSubTypeObserved = false;
    private static boolean listenerSuperTypeObserved = false;
    private static boolean tagHandlerSuperTypeObserved = false;
    private static boolean tagHandlerSubTypeObserved = false;
    private static boolean stringObserved = false;

    public void observeServlet(@Observes ProcessInjectionTarget<TestServlet> event) {
        servletEvent = event;
        event.setInjectionTarget(new CustomInjectionTarget<TestServlet>(event.getInjectionTarget()));
    }

    public void observeFilter(@Observes ProcessInjectionTarget<TestFilter> event) {
        filterEvent = event;
        event.setInjectionTarget(new CustomInjectionTarget<TestFilter>(event.getInjectionTarget()));
    }

    public void observeListener(@Observes ProcessInjectionTarget<TestListener> event) {
        listenerEvent = event;
        event.setInjectionTarget(new CustomInjectionTarget<TestListener>(event.getInjectionTarget()));
    }

    public void observeTagHandler(@Observes ProcessInjectionTarget<TestTagHandler> event) {
        tagHandlerEvent = event;
        event.setInjectionTarget(new CustomInjectionTarget<TestTagHandler>(event.getInjectionTarget()));
    }

    public void observeTagLibraryListener(@Observes ProcessInjectionTarget<TagLibraryListener> event) {
        tagLibraryListenerEvent = event;
        event.setInjectionTarget(new CustomInjectionTarget<TagLibraryListener>(event.getInjectionTarget()));
    }

    public void observeServletSuperType(@Observes ProcessInjectionTarget<? super HttpServlet> event) {
        servletSuperTypeObserved = true;
    }

    public void observeServletSubType(@Observes ProcessInjectionTarget<? extends HttpServlet> event) {
        servletSubTypeObserved = true;

    }

    public void observeListenerSuperType(@Observes ProcessInjectionTarget<? super ServletContextListener> event) {
        listenerSuperTypeObserved = true;
    }

    public void tagHandlerSuperType(@Observes ProcessInjectionTarget<? super SimpleTagSupport> event) {
        tagHandlerSuperTypeObserved = true;
    }

    public void tagHandlerSubType(@Observes ProcessInjectionTarget<? extends SimpleTag> event) {
        tagHandlerSubTypeObserved = true;
    }

    public void observeSessionBean(@Observes ProcessInjectionTarget<Fence> event) {
        event.setInjectionTarget(new CustomInjectionTarget<Fence>(event.getInjectionTarget()));
    }

    public void observeEJBInterceptor(@Observes ProcessInjectionTarget<FenceInterceptor> event) {
        event.setInjectionTarget(new CustomInjectionTarget<FenceInterceptor>(event.getInjectionTarget()));
    }

    public void observeWSEndpoint(@Observes ProcessInjectionTarget<CowboyEndpoint> event) {
        event.setInjectionTarget(new CustomInjectionTarget<CowboyEndpoint>(event.getInjectionTarget()));
    }

    public void stringObserver(@Observes ProcessInjectionTarget<String> event) {
        stringObserved = true;
    }

    public static ProcessInjectionTarget<TestServlet> getServletEvent() {
        return servletEvent;
    }

    public static ProcessInjectionTarget<TestListener> getListenerEvent() {
        return listenerEvent;
    }

    public static ProcessInjectionTarget<TestFilter> getFilterEvent() {
        return filterEvent;
    }

    public static ProcessInjectionTarget<TagLibraryListener> getTagLibraryListenerEvent() {
        return tagLibraryListenerEvent;
    }

    public static ProcessInjectionTarget<TestTagHandler> getTagHandlerEvent() {
        return tagHandlerEvent;
    }

    public static boolean isServletSuperTypeObserved() {
        return servletSuperTypeObserved;
    }

    public static boolean isServletSubTypeObserved() {
        return servletSubTypeObserved;
    }

    public static boolean isListenerSuperTypeObserved() {
        return listenerSuperTypeObserved;
    }

    public static boolean isTagHandlerSuperTypeObserved() {
        return tagHandlerSuperTypeObserved;
    }

    public static boolean isTagHandlerSubTypeObserved() {
        return tagHandlerSubTypeObserved;
    }

    public static boolean isStringObserved() {
        return stringObserved;
    }
}
