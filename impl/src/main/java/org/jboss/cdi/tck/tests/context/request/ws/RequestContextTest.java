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
package org.jboss.cdi.tck.tests.context.request.ws;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.JAX_WS;
import static org.jboss.cdi.tck.cdi.Sections.APPLICATION_CONTEXT_EE;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT_EE;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.Timer;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class RequestContextTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(RequestContextTest.class)
                .withExcludedClass(TranslatorService.class.getName())

                //TODO - keeping servlet definition in web.xml due to GLASSFISH-21303
                .withWebXml(
                        Descriptors.create(WebAppDescriptor.class).createServlet().servletName("Translator")
                                .servletClass("org.jboss.cdi.tck.tests.context.request.ws.TranslatorEndpoint").loadOnStartup(1)
                                .up().createServletMapping().servletName("Translator").urlPattern("/translator").up()).build();
    }

    @Test(groups = { JAVAEE_FULL, JAX_WS })
    @SpecAssertions({ @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "b"), @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "d"),
            @SpecAssertion(section = APPLICATION_CONTEXT_EE, id = "b") })
    public void testRequestScopeActiveDuringWebServiceInvocation() throws Exception {

        URL wsdlLocation = new URL(contextPath.toExternalForm() + "translator?wsdl");
        TranslatorService endpointService = new TranslatorService(wsdlLocation);
        Translator translator = endpointService.getTranslatorEndpointPort();

        // New instance of Foo is created for each WS request
        String id01 = translator.translate();
        String id02 = translator.translate();
        assertNotEquals(id01, id02);

        final WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);

        TextPage info = webClient.getPage(contextPath + "info");
        Timer timer = new Timer().setDelay(5, TimeUnit.SECONDS).setSleepInterval(1000).addStopCondition(new Timer.StopCondition() {

            @Override
            public boolean isSatisfied() {
                TextPage info = null;
                try {
                    info = webClient.getPage(contextPath + "info");
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                Matcher matcher = Pattern.compile("(Foo destroyed:)(\\w+)").matcher(info.getContent());
                if (matcher.find()) {
                    String value = matcher.group(2);
                    return Integer.valueOf(value) == 2 ? true : false;
                }
                return false;
            }
        }).start();

        assertTrue(timer.isStopConditionsSatisfiedBeforeTimeout());
    }

}
