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
package org.jboss.cdi.tck.tests.extensions.modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessModule;

import org.apache.commons.io.IOUtils;

public class ModuleProcessingExtension implements Extension {

    private final List<ProcessModuleHolder> modules = new ArrayList<ModuleProcessingExtension.ProcessModuleHolder>();

    public void observe(@Observes ProcessModule event) {
        modules.add(new ProcessModuleHolder(event));
    }

    public List<ProcessModuleHolder> getModules() {
        return modules;
    }

    public ProcessModuleHolder getFirstModule() {

        if (modules == null || modules.isEmpty())
            return null;

        return modules.iterator().next();
    }

    /**
     * 
     */
    public static class ProcessModuleHolder {
        private final List<Class<?>> interceptors;
        private final List<Class<?>> decorators;
        private final List<Class<?>> alternatives;
        private final Set<Class<?>> classes;
        private final String beansXml;

        public ProcessModuleHolder(ProcessModule event) {
            this.interceptors = new ArrayList<Class<?>>(event.getInterceptors());
            this.decorators = new ArrayList<Class<?>>(event.getDecorators());
            this.alternatives = new ArrayList<Class<?>>(event.getAlternatives());
            this.classes = new HashSet<Class<?>>();
            this.beansXml = readBeansXml(event);
        }

        public List<Class<?>> getInterceptors() {
            return interceptors;
        }

        public List<Class<?>> getDecorators() {
            return decorators;
        }

        public List<Class<?>> getAlternatives() {
            return alternatives;
        }

        public Set<Class<?>> getClasses() {
            return classes;
        }

        public String getBeansXml() {
            return beansXml;
        }

        /**
         * 
         * @param event
         * @return
         */
        private String readBeansXml(ProcessModule event) {

            try {
                return IOUtils.toString(event.getBeansXml());
            } catch (Exception e) {
                // Descriptor is not available
                return null;
            }
        }
    }

}
