/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.shrinkwrap.descriptors;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.shrinkwrap.descriptor.api.DescriptorExportException;
import org.jboss.shrinkwrap.descriptor.api.beans10.Alternatives;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.shrinkwrap.descriptor.api.beans10.Decorators;
import org.jboss.shrinkwrap.descriptor.api.beans10.Interceptors;

/**
 * Simple temporary workaround to support CDI 1.1 <code>beans.xml</code>.
 * 
 * Will be deprecated as soon as ShrinkWrap Descriptors supports it.
 * 
 * @author Martin Kouba
 * 
 */
public class Beans11DescriptorImpl implements BeansDescriptor {

    private List<BeansXmlClass> alternatives = new ArrayList<BeansXmlClass>();
    private List<BeansXmlClass> interceptors = new ArrayList<BeansXmlClass>();
    private List<BeansXmlClass> decorators = new ArrayList<BeansXmlClass>();

    public Beans11DescriptorImpl alternatives(BeansXmlClass... alternatives) {
        this.alternatives.addAll(Arrays.asList(alternatives));
        return this;
    }

    public Beans11DescriptorImpl interceptors(BeansXmlClass... interceptors) {
        this.interceptors.addAll(Arrays.asList(interceptors));
        return this;
    }

    public Beans11DescriptorImpl decorators(BeansXmlClass... decorators) {
        this.decorators.addAll(Arrays.asList(decorators));
        return this;
    }

    @Override
    public String getDescriptorName() {
        return "beans.xml";
    }

    @Override
    public String exportAsString() throws DescriptorExportException {

        StringBuilder xml = new StringBuilder();
        xml.append("<beans>\n");
        appendSection("alternatives", alternatives, xml);
        appendSection("interceptors", interceptors, xml);
        appendSection("decorators", decorators, xml);
        xml.append("</beans>");

        return xml.toString();
    }

    private void appendSection(String name, List<BeansXmlClass> classes, StringBuilder xml) {
        if (classes.size() > 0) {
            xml.append("<").append(name).append(">\n");
            appendClasses(classes, xml);
            xml.append("</").append(name).append(">\n");
        }
    }

    private void appendClasses(List<BeansXmlClass> classes, StringBuilder xml) {
        for (BeansXmlClass clazz : classes) {
            xml.append(clazz.asXmlElement()).append("\n");
        }
    }

    @Override
    public void exportTo(OutputStream output) throws DescriptorExportException, IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public BeansDescriptor addDefaultNamespaces() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BeansDescriptor addNamespace(String name, String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getNamespaces() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BeansDescriptor removeAllNamespaces() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Interceptors<BeansDescriptor> getOrCreateInterceptors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Interceptors<BeansDescriptor> createInterceptors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Interceptors<BeansDescriptor>> getAllInterceptors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BeansDescriptor removeAllInterceptors() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Decorators<BeansDescriptor> getOrCreateDecorators() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Decorators<BeansDescriptor> createDecorators() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Decorators<BeansDescriptor>> getAllDecorators() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BeansDescriptor removeAllDecorators() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Alternatives<BeansDescriptor> getOrCreateAlternatives() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Alternatives<BeansDescriptor> createAlternatives() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Alternatives<BeansDescriptor>> getAllAlternatives() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BeansDescriptor removeAllAlternatives() {
        throw new UnsupportedOperationException();
    }

}
