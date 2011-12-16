/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.jsr299.tck.tests.extensions.modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
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
        private final Set<Class<?>> alternatives;
        private final Set<AnnotatedType<?>> annotatedTypes;
        private final Set<Class<?>> classes;
        private final String beansXml;

        public ProcessModuleHolder(ProcessModule event) {
            this.interceptors = new ArrayList<Class<?>>(event.getInterceptors());
            this.decorators = new ArrayList<Class<?>>(event.getDecorators());
            this.alternatives = new HashSet<Class<?>>(event.getAlternatives());
            this.annotatedTypes = new HashSet<AnnotatedType<?>>();
            this.classes = new HashSet<Class<?>>();
            for (Iterator<AnnotatedType<?>> i = event.getAnnotatedTypes(); i.hasNext();) {
                AnnotatedType<?> type = i.next();
                annotatedTypes.add(type);
                classes.add(type.getJavaClass());
            }
            this.beansXml = readBeansXml(event);
        }

        public List<Class<?>> getInterceptors() {
            return interceptors;
        }

        public List<Class<?>> getDecorators() {
            return decorators;
        }

        public Set<Class<?>> getAlternatives() {
            return alternatives;
        }

        public Set<AnnotatedType<?>> getAnnotatedTypes() {
            return annotatedTypes;
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
