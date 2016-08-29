/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.configurators.invalid;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.api.InSequence;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Tests the invalid use case of calling set method and configurator in the same listener. Deployments are done manually so that
 * we can, one by one assert that we get exception.
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
@RunAsClient
public class ConfiguratorAndSetMethodTest extends AbstractTest {

    public static final String PAT = "PAT";
    public static final String PAT_REVERSE = "PAT_REVERSE";
    public static final String PBA = "PBA";
    public static final String PBA_REVERSE = "PBA_REVERSE";
    public static final String PIP = "PIP";
    public static final String PIP_REVERSE = "PIP_REVERSE";
    public static final String POM = "POM";
    public static final String POM_REVERSE = "POM_REVERSE";

    @ArquillianResource
    Deployer deployer;

    @Deployment(name = PAT, managed = false, testable = false)
    public static WebArchive createTestArchiveOne() {
        return new WebArchiveBuilder().withTestClassPackage(ConfiguratorAndSetMethodTest.class)
            .withExtension(PATExtension.class).withName(PAT + ".war").build();
    }

    @Deployment(name = PAT_REVERSE, managed = false, testable = false)
    public static WebArchive createTestArchiveTwo() {
        return new WebArchiveBuilder().withTestClassPackage(ConfiguratorAndSetMethodTest.class)
            .withExtension(PATReverseExtension.class).withName(PAT_REVERSE + ".war").build();
    }

    @Deployment(name = PIP, managed = false, testable = false)
    public static WebArchive createTestArchiveThree() {
        return new WebArchiveBuilder().withTestClassPackage(ConfiguratorAndSetMethodTest.class)
            .withExtension(PIPExtension.class).withName(PIP + ".war").build();
    }

    @Deployment(name = PIP_REVERSE, managed = false, testable = false)
    public static WebArchive createTestArchiveFour() {
        return new WebArchiveBuilder().withTestClassPackage(ConfiguratorAndSetMethodTest.class)
            .withExtension(PIPReverseExtension.class).withName(PIP_REVERSE + ".war").build();
    }

    @Deployment(name = PBA, managed = false, testable = false)
    public static WebArchive createTestArchiveFive() {
        return new WebArchiveBuilder().withTestClassPackage(ConfiguratorAndSetMethodTest.class)
            .withExtension(PBAExtension.class).withName(PBA + ".war").build();
    }

    @Deployment(name = PBA_REVERSE, managed = false, testable = false)
    public static WebArchive createTestArchiveSix() {
        return new WebArchiveBuilder().withTestClassPackage(ConfiguratorAndSetMethodTest.class)
            .withExtension(PBAReverseExtension.class).withName(PBA_REVERSE + ".war").build();
    }

    @Deployment(name = POM, managed = false, testable = false)
    public static WebArchive createTestArchiveSeven() {
        return new WebArchiveBuilder().withTestClassPackage(ConfiguratorAndSetMethodTest.class)
            .withExtension(POMExtension.class).withName(POM + ".war").build();
    }

    @Deployment(name = POM_REVERSE, managed = false, testable = false)
    public static WebArchive createTestArchiveEight() {
        return new WebArchiveBuilder().withTestClassPackage(ConfiguratorAndSetMethodTest.class)
            .withExtension(POMReverseExtension.class).withName(POM_REVERSE + ".war").build();
    }

    @Test
    @InSequence(1)
    public void preDeploy() {
        // we need to deploy everything here, so that later on we can use @ArquillianResource to get contexts
        deployer.deploy(PAT);
        deployer.deploy(PAT_REVERSE);
        deployer.deploy(PBA);
        deployer.deploy(PBA_REVERSE);
        deployer.deploy(PIP);
        deployer.deploy(PIP_REVERSE);
        deployer.deploy(POM);
        deployer.deploy(POM_REVERSE);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.PROCESS_ANNOTATED_TYPE, id = "k")
    public void testPAT(@ArquillianResource @OperateOnDeployment(PAT) URL extContext) throws IOException {
        WebClient webClient = new WebClient();
        TextPage exceptionThrown = webClient.getPage(extContext + "test?ext=" + PAT);
        deployer.undeploy(PAT);
        assertTrue(Boolean.valueOf(exceptionThrown.getContent()));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.PROCESS_ANNOTATED_TYPE, id = "k")
    public void testPATReverse(@ArquillianResource @OperateOnDeployment(PAT_REVERSE) URL extContext) throws IOException {
        WebClient webClient = new WebClient();
        TextPage exceptionThrown = webClient.getPage(extContext + "test?ext=" + PAT_REVERSE);
        deployer.undeploy(PAT_REVERSE);
        assertTrue(Boolean.valueOf(exceptionThrown.getContent()));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.PROCESS_BEAN_ATTRIBUTES, id = "g")
    public void testPBA(@ArquillianResource @OperateOnDeployment(PBA) URL extContext) throws IOException {
        WebClient webClient = new WebClient();
        TextPage exceptionThrown = webClient.getPage(extContext + "test?ext=" + PBA);
        deployer.undeploy(PBA);
        assertTrue(Boolean.valueOf(exceptionThrown.getContent()));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.PROCESS_BEAN_ATTRIBUTES, id = "g")
    public void testPBAReverse(@ArquillianResource @OperateOnDeployment(PBA_REVERSE) URL extContext) throws IOException {
        WebClient webClient = new WebClient();
        TextPage exceptionThrown = webClient.getPage(extContext + "test?ext=" + PBA_REVERSE);
        deployer.undeploy(PBA_REVERSE);
        assertTrue(Boolean.valueOf(exceptionThrown.getContent()));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.PROCESS_INJECTION_POINT, id = "g")
    public void testPIP(@ArquillianResource @OperateOnDeployment(PIP) URL extContext) throws IOException {
        WebClient webClient = new WebClient();
        TextPage exceptionThrown = webClient.getPage(extContext + "test?ext=" + PIP);
        deployer.undeploy(PIP);
        assertTrue(Boolean.valueOf(exceptionThrown.getContent()));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.PROCESS_INJECTION_POINT, id = "g")
    public void testPIPReverse(@ArquillianResource @OperateOnDeployment(PIP_REVERSE) URL extContext) throws IOException {
        WebClient webClient = new WebClient();
        TextPage exceptionThrown = webClient.getPage(extContext + "test?ext=" + PIP_REVERSE);
        deployer.undeploy(PIP_REVERSE);
        assertTrue(Boolean.valueOf(exceptionThrown.getContent()));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.PROCESS_OBSERVER_METHOD, id = "e")
    public void testPOM(@ArquillianResource @OperateOnDeployment(POM) URL extContext) throws IOException {
        WebClient webClient = new WebClient();
        TextPage exceptionThrown = webClient.getPage(extContext + "test?ext=" + POM);
        deployer.undeploy(POM);
        assertTrue(Boolean.valueOf(exceptionThrown.getContent()));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.PROCESS_OBSERVER_METHOD, id = "e")
    public void testPOMReverse(@ArquillianResource @OperateOnDeployment(POM_REVERSE) URL extContext) throws IOException {
        WebClient webClient = new WebClient();
        TextPage exceptionThrown = webClient.getPage(extContext + "test?ext=" + POM_REVERSE);
        deployer.undeploy(POM_REVERSE);
        assertTrue(Boolean.valueOf(exceptionThrown.getContent()));
    }
}
