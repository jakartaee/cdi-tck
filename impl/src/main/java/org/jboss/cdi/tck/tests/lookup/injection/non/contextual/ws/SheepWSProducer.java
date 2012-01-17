package org.jboss.cdi.tck.tests.lookup.injection.non.contextual.ws;

import javax.enterprise.inject.Produces;
import javax.xml.ws.WebServiceRef;

public class SheepWSProducer {

    @Produces
    @WebServiceRef
    SheepWS sheepWS;

}
