package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual.ws;

import javax.enterprise.inject.Produces;
import javax.xml.ws.WebServiceRef;

public class SheepWSProducer {

    @Produces
    @WebServiceRef
    SheepWS sheepWS;

}
