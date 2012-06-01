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

package org.jboss.cdi.tck.tests.deployment.shutdown;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.SERVLET;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.net.URLEncoder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.BeforeShutdown;

import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.cdi.tck.util.SimpleLogger;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Test application shutdown lifecycle.
 * 
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
@Test(groups = { INTEGRATION, SERVLET })
public class ApplicationShutdownLifecycleTest extends AbstractTest {

    private static final String FOO = "foo";

    private static final String INFO = "info";

    @Deployment(name = FOO, managed = false, testable = false)
    public static WebArchive createFooTestArchive() {
        return new WebArchiveBuilder()
                .notTestArchive()
                .withClasses(Foo.class, Bar.class, ContextDestructionObserver.class, LifecycleMonitoringExtension.class,
                        SimpleLogger.class, InitServlet.class, InfoClient.class)
                .withExtension(LifecycleMonitoringExtension.class).build();
    }

    @Deployment(name = INFO, managed = false, testable = false)
    public static WebArchive createBarTestArchive() {
        return new WebArchiveBuilder().notTestArchive().withClasses(InfoServlet.class, ActionSequence.class).build();
    }

    @ArquillianResource
    Deployer deployer;

    /**
     * This is not a real test method.
     * 
     * @see #testShutdown()
     */
    @Test
    public void deployArchives() {
        // In order to use @ArquillianResource URLs we need to deploy both test archives first
        deployer.deploy(FOO);
        deployer.deploy(INFO);
    }

    /**
     * Note that this test method depends on (must be run after)
     * 
     * @throws Exception
     */
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, dependsOnMethods = "deployArchives")
    @SpecAssertions({ @SpecAssertion(section = "12.3", id = "a"), @SpecAssertion(section = "12.3", id = "b"),
            @SpecAssertion(section = "12.3", id = "c"), @SpecAssertion(section = "6.7.1", id = "ja"),
            @SpecAssertion(section = "6.7.3", id = "ga"), @SpecAssertion(section = "11.5.4", id = "a") })
    public void testShutdown(@ArquillianResource @OperateOnDeployment(FOO) URL fooContext,
            @ArquillianResource @OperateOnDeployment(INFO) URL infoContext) throws Exception {

        // Init foo - set info archive deployment url
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        webClient.getPage(fooContext + "init?url=" + URLEncoder.encode(infoContext.toExternalForm(), "UTF-8"));

        // Undeploy foo
        deployer.undeploy(FOO);

        // 1. Destroy contexts
        // 2. Destroy dependent objects injected into enums
        // 3. BeforeShutdown event
        ActionSequence correctSequence = new ActionSequence().add(RequestScoped.class.getName())
                .add(ApplicationScoped.class.getName()).add(Foo.class.getName()).add(BeforeShutdown.class.getName());
        TextPage info = webClient.getPage(infoContext + "info?action=get");
        assertEquals(info.getContent(), correctSequence.toString());

        // Undeploy info
        deployer.undeploy(INFO);
    }

}
