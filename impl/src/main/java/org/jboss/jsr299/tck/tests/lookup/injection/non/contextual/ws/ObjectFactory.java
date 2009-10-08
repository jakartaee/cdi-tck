
package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private final static QName _IsSheepInjected_QNAME = new QName("http://ws.contextual.non.injection.lookup.tests.tck.jsr299.jboss.org/", "isSheepInjected");
    private final static QName _IsSheepInjectedResponse_QNAME = new QName("http://ws.contextual.non.injection.lookup.tests.tck.jsr299.jboss.org/", "isSheepInjectedResponse");

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
        return new JAXBElement<IsSheepInjectedResponse>(_IsSheepInjectedResponse_QNAME, IsSheepInjectedResponse.class, null, value);
    }
}
