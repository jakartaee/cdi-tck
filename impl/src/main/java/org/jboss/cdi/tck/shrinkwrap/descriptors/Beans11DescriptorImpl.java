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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jboss.shrinkwrap.descriptor.api.DescriptorExportException;
import org.jboss.shrinkwrap.descriptor.api.beans10.Alternatives;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.shrinkwrap.descriptor.api.beans10.Decorators;
import org.jboss.shrinkwrap.descriptor.api.beans10.Interceptors;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Simple temporary workaround to support CDI 1.1 <code>beans.xml</code>.
 *
 * Deprecate as soon as ShrinkWrap Descriptors is ready.
 *
 * @author Martin Kouba
 */
public class Beans11DescriptorImpl implements BeansDescriptor {

    private static final String NS = "http://xmlns.jcp.org/xml/ns/javaee";

    private BeanDiscoveryMode mode = BeanDiscoveryMode.ANNOTATED;

    private List<Class<?>> alternatives = new ArrayList<Class<?>>(5);

    private List<Class<?>> interceptors = new ArrayList<Class<?>>(5);

    private List<Class<?>> decorators = new ArrayList<Class<?>>(5);

    private List<Class<?>> alternativeStereotypes = new ArrayList<Class<?>>(5);

    private List<Exclude> excludes = new ArrayList<Exclude>(5);

    /**
     *
     * @param mode
     * @return
     */
    public Beans11DescriptorImpl setBeanDiscoveryMode(BeanDiscoveryMode mode) {
        this.mode = mode;
        return this;
    }

    /**
     *
     * @param exclude
     * @return
     */
    public Beans11DescriptorImpl excludes(Exclude... excludes) {
        this.excludes.addAll(Arrays.asList(excludes));
        return this;
    }

    /**
     *
     * @param alternatives
     * @return
     */
    public Beans11DescriptorImpl alternatives(Class<?>... alternatives) {
        this.alternatives.addAll(Arrays.asList(alternatives));
        return this;
    }

    /**
     *
     * @param stereotypes
     * @return
     */
    public Beans11DescriptorImpl alternativeStereotypes(Class<?>... stereotypes) {
        this.alternativeStereotypes.addAll(Arrays.asList(stereotypes));
        return this;
    }

    /**
     *
     * @param interceptors
     * @return
     */
    public Beans11DescriptorImpl interceptors(Class<?>... interceptors) {
        this.interceptors.addAll(Arrays.asList(interceptors));
        return this;
    }

    /**
     *
     * @param decorators
     * @return
     */
    public Beans11DescriptorImpl decorators(Class<?>... decorators) {
        this.decorators.addAll(Arrays.asList(decorators));
        return this;
    }

    @Override
    public String getDescriptorName() {
        return "beans.xml";
    }

    @Override
    public String exportAsString() throws DescriptorExportException {

        try {

            StringWriter writer = new StringWriter();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(buildDocument()), new StreamResult(writer));

            return writer.toString();

        } catch (Exception e) {
            throw new IllegalStateException("Cannot export beans.xml", e);
        }
    }

    private Document buildDocument() {

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();
            Document doc = impl.createDocument(NS, "beans", null);

            Element rootElement = doc.getDocumentElement();
            rootElement.setAttribute("version", "1.1");
            rootElement.setAttribute("bean-discovery-mode", this.mode.toString().toLowerCase());

            if (!interceptors.isEmpty()) {
                Element interceptorsElement = doc.createElement("interceptors");
                rootElement.appendChild(interceptorsElement);
                for (Class<?> clazz : interceptors) {
                    appendChildTextElement(doc, interceptorsElement, "class", clazz.getName());
                }
            }

            if (!decorators.isEmpty()) {
                Element decoratorsElement = doc.createElement("decorators");
                rootElement.appendChild(decoratorsElement);
                for (Class<?> clazz : decorators) {
                    appendChildTextElement(doc, decoratorsElement, "class", clazz.getName());
                }
            }

            if (!alternatives.isEmpty() || !alternativeStereotypes.isEmpty()) {
                Element alternativesElement = doc.createElement("alternatives");
                rootElement.appendChild(alternativesElement);
                for (Class<?> clazz : alternatives) {
                    appendChildTextElement(doc, alternativesElement, "class", clazz.getName());
                }
                for (Class<?> clazz : alternativeStereotypes) {
                    appendChildTextElement(doc, alternativesElement, "stereotype", clazz.getName());
                }
            }

            if (!excludes.isEmpty()) {

                Element scanElement = doc.createElement("scan");
                rootElement.appendChild(scanElement);

                for (Exclude exclude : excludes) {

                    Element exludeElement = doc.createElement("exclude");
                    exludeElement.setAttribute("name", exclude.getName());
                    if (!exclude.getActivators().isEmpty()) {
                        for (Activator activator : exclude.getActivators()) {
                            Element activatorElement = doc.createElement(activator.getElementName());
                            exludeElement.appendChild(activatorElement);
                            activatorElement.setAttribute("name", activator.getNameAttribute());
                            if (activator.getValueAttribute() != null) {
                                activatorElement.setAttribute("value", activator.getValueAttribute());
                            }
                        }
                    }
                    scanElement.appendChild(exludeElement);
                }
            }
            return doc;

        } catch (Exception e) {
            throw new IllegalStateException("Cannot build beans.xml", e);
        }
    }

    private void appendChildTextElement(Document doc, Element element, String name, String text) {
        Element child = doc.createElement(name);
        child.appendChild(doc.createTextNode(text));
        element.appendChild(child);
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

    /**
     *
     *
     */
    public static enum BeanDiscoveryMode {

        ANNOTATED, ALL, NONE;

    }

    public static Beans11DescriptorImpl newBeans11Descriptor() {
        return new Beans11DescriptorImpl();
    }

}
