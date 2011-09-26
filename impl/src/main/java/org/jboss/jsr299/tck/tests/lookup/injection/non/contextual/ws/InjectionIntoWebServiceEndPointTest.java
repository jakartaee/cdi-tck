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

import java.net.URL;

import javax.xml.namespace.QName;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 1. Needs javaee-full profile (use serverConfig property in arquillian.xml) 2. Injection not working - AS7-1903
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class InjectionIntoWebServiceEndPointTest extends AbstractJSR299Test {

    @ArquillianResource
    protected URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InjectionIntoWebServiceEndPointTest.class)
        // .withClasses(Sheep.class, SheepWSEndPoint.class)
                .withWebXml("web.xml").build();
    }

    @Test(groups = "javaee-full-only")
    @SpecAssertions({ @SpecAssertion(section = "5.5", id = "ee"), @SpecAssertion(section = "5.5.2", id = "aq"),
            @SpecAssertion(section = "5.5.2", id = "ar") })
    public void testInjectionIntoWebServiceEndpoint() throws Exception {
        URL wsdlLocation = new URL(contextPath.toExternalForm() + "TestWebService?wsdl");
        SheepWSEndPointService service = new SheepWSEndPointService(wsdlLocation, new QName(
                "http://ws.contextual.non.injection.lookup.tests.tck.jsr299.jboss.org/", "SheepWS"));
        SheepWS ws = service.getSheepWSPort();
        assert ws.isSheepInjected();
    }
}
