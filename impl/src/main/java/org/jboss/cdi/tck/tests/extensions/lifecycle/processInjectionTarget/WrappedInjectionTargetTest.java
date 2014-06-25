/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processInjectionTarget;

import static org.jboss.cdi.tck.cdi.Sections.PIT;
import static org.testng.Assert.assertTrue;

import javax.xml.namespace.QName;
import java.net.URL;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class WrappedInjectionTargetTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    private static final String LIBRARY_TLD_PATH = "WEB-INF/TestLibrary.tld";
    private static final String TEST_URI = "http://processInjectionTarget.lifecycle.extensions.tests.tck.cdi.jboss.org/";

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(WrappedInjectionTargetTest.class)
                .withExtension(ProcessInjectionTargetObserver.class)
                .withExcludedClass(ContainerEventTest.class.getName())
                .withWebResource("TestLibrary.tld", LIBRARY_TLD_PATH)
                .withWebResource("index.jsp", "index.jsp")
                .withWebXml(
                        Descriptors.create(WebAppDescriptor.class)
                                .createJspConfig().createTaglib()
                                .taglibUri(TEST_URI)
                                .taglibLocation(LIBRARY_TLD_PATH).up().up()
                                .createServlet().servletName("Cowboy")
                                .servletClass(CowboyEndpoint.class.getName()).loadOnStartup(1)
                                .up().createServletMapping().servletName("Cowboy").urlPattern("/cowboy").up()).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "eb") })
    public void testWrappedInjectionTargetUsedForSessionBean() throws Exception {

        TextPage page = invokeContextPath("test?type=sessionbean", TextPage.class);
        assertTrue(page.getContent().contains("true"));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "ec") })
    public void testWrappedInjectionTargetUsedForServletListener() throws Exception {

        TextPage page = invokeContextPath("test?type=listener", TextPage.class);
        assertTrue(page.getContent().contains(Sheep.class.getName()));
        assertTrue(page.getContent().contains("true"));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "ed") })
    public void testWrappedInjectionTargetUsedForTagHandler() throws Exception {

        HtmlPage page = invokeContextPath("index.jsp", HtmlPage.class);
        assertTrue(page.asText().contains("true"));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "ee") })
    public void testWrappedInjectionTargetUsedForTagLibraryListener() throws Exception {

        TextPage page = invokeContextPath("test?type=taglibrary", TextPage.class);
        assertTrue(page.getContent().contains("true"));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "eh") })
    public void testWrappedInjectionTargetUsedForServlet() throws Exception {

        TextPage page = invokeContextPath("test?type=servlet", TextPage.class);
        assertTrue(page.getContent().contains("true"));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "ef") })
    public void testWrappedInjectionTargetUsedForEjbInterceptor() throws Exception {

        TextPage page = invokeContextPath("test?type=interceptor", TextPage.class);
        assertTrue(page.getContent().contains("true"));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "eg") })
    public void testWrappedInjectionTargetUsedForWsEndpoint() throws Exception {
        URL wsdlLocation = new URL(contextPath.toExternalForm() + "cowboy?wsdl");
        CowboyEndpointService endpointService = new CowboyEndpointService(wsdlLocation, new QName(TEST_URI, "Cowboy"));

        // cowboy endpoint injection should be catched by InjectionObserverHelper
        // and added to servlet output
        Cowboy cowboy = endpointService.getPort(Cowboy.class);
        cowboy.catchSheep();

        TextPage page = invokeContextPath("test?type=wsendpoint", TextPage.class);
        assertTrue(page.getContent().contains("true"));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "ei") })
    public void testWrappedInjectionTargetIsUsedForFilter() throws Exception {

        TextPage page = invokeContextPath("test?type=filter", TextPage.class);
        assertTrue(page.getContent().contains("true"));
    }

    public <T extends Page> T invokeContextPath(String path, Class<T> type) throws Exception {

        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);

        if (type.equals(TextPage.class)) {
            TextPage page = webClient.getPage(contextPath + path);
            return type.cast(page);
        } else {
            HtmlPage page = webClient.getPage(contextPath + path);
            return type.cast(page);
        }
    }

}
