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

package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private final static QName _IsSheepInjected_QNAME = new QName(
            "http://ws.contextual.non.injection.lookup.tests.tck.jsr299.jboss.org/", "isSheepInjected");
    private final static QName _IsSheepInjectedResponse_QNAME = new QName(
            "http://ws.contextual.non.injection.lookup.tests.tck.jsr299.jboss.org/", "isSheepInjectedResponse");

    public ObjectFactory() {
    }

    public IsSheepInjected createIsSheepInjected() {
        return new IsSheepInjected();
    }

    public IsSheepInjectedResponse createIsSheepInjectedResponse() {
        return new IsSheepInjectedResponse();
    }

    @XmlElementDecl(namespace = "http://ws.contextual.non.injection.lookup.tests.tck.jsr299.jboss.org/", name = "isSheepInjected")
    public JAXBElement<IsSheepInjected> createIsSheepInjected(IsSheepInjected value) {
        return new JAXBElement<IsSheepInjected>(_IsSheepInjected_QNAME, IsSheepInjected.class, null, value);
    }

    @XmlElementDecl(namespace = "http://ws.contextual.non.injection.lookup.tests.tck.jsr299.jboss.org/", name = "isSheepInjectedResponse")
    public JAXBElement<IsSheepInjectedResponse> createIsSheepInjectedResponse(IsSheepInjectedResponse value) {
        return new JAXBElement<IsSheepInjectedResponse>(_IsSheepInjectedResponse_QNAME, IsSheepInjectedResponse.class, null,
                value);
    }
}
