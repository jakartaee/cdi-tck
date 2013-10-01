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

package org.jboss.cdi.tck.tests.implementation.simple.resource.ws.staticProducer;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "SheepWS", targetNamespace = "http://staticProducer.ws.resource.simple.implementation.tests.tck.cdi.jboss.org/")
public class SheepWSEndPointService extends Service {

    public SheepWSEndPointService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    @WebEndpoint(name = "SheepWSPort")
    public SheepWS getSheepWSPort() {
        return super.getPort(SheepWS.class);
    }

    @WebEndpoint(name = "SheepWSPort")
    public SheepWS getSheepWSPort(WebServiceFeature... features) {
        return super.getPort(SheepWS.class, features);
    }
}
