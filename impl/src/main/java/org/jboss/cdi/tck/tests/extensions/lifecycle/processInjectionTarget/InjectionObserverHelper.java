/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class InjectionObserverHelper {

    public void observeServlet(@Observes TestServlet servlet) {
        TestServlet.setIsWrappedInjectionSuccessfull(true);
    }

    public void observeTagHandler(@Observes TestTagHandler tagHandler) {
        TestTagHandler.setIsWrappedInjectionSuccessfull(true);
    }

    public void observeTagLibraryListener(@Observes TagLibraryListener tagListener) {
        TagLibraryListener.setIsWrappedInjectionSuccessfull(true);
    }

    public void observeWSEndpoint(@Observes CowboyEndpoint endpoint) {
        CowboyEndpoint.setIsWrappedInjectionSuccessfull(true);
    }

    public void observeServletFilter(@Observes TestFilter filter) {
        TestFilter.setIsWrappedInjectionSuccessfull(true);
    }

    public void observeSessionBean(@Observes Fence fence) {
        Fence.setIsWrappedInjectionSuccessfull(true);
    }

    public void observeEjbInterceptor(@Observes FenceInterceptor fenceInterceptor) {
        FenceInterceptor.setIsWrappedInjectionSuccessfull(true);
    }

    public void observeEjbTestListener(@Observes TestListener listener) {
        TestListener.setIsWrappedInjectionSuccessfull(true);
    }

}
