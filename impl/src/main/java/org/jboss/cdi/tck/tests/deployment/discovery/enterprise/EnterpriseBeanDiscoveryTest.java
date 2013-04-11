/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.deployment.discovery.enterprise;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DEFINING_ANNOTATIONS;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY;
import static org.jboss.cdi.tck.shrinkwrap.descriptors.Beans11DescriptorImpl.newBeans11Descriptor;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.descriptors.Beans11DescriptorImpl.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.shrinkwrap.descriptor.api.spec.se.manifest.ManifestDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class EnterpriseBeanDiscoveryTest extends AbstractTest {

    private static final String ALPHA_JAR = "alpha.jar";
    private static final String BRAVO_JAR = "bravo.jar";
    private static final String CHARLIE_JAR = "charlie.jar";
    private static final String DELTA_JAR = "delta.jar";
    private static final String ECHO_JAR = "echo.jar";
    private static final String FOXTROT_JAR = "foxtrot.jar";

    @Deployment
    public static EnterpriseArchive createTestArchive() {

        // 1.1 version beans.xml with bean-discovery-mode of all
        JavaArchive alpha = ShrinkWrap
                .create(JavaArchive.class, ALPHA_JAR)
                .addClasses(Alpha.class, AlphaLocal.class)
                .addAsManifestResource(
                        new StringAsset(newBeans11Descriptor().setBeanDiscoveryMode(BeanDiscoveryMode.ALL).exportAsString()),
                        "beans.xml");
        // Empty beans.xml
        JavaArchive bravo = ShrinkWrap.create(JavaArchive.class, BRAVO_JAR).addClasses(Bravo.class, BravoLocal.class)
                .addAsManifestResource(new StringAsset(""), "beans.xml");
        // No version beans.xml
        JavaArchive charlie = ShrinkWrap
                .create(JavaArchive.class, CHARLIE_JAR)
                .addClasses(Charlie.class, CharlieLocal.class)
                .addAsManifestResource(new StringAsset(Descriptors.create(BeansDescriptor.class).exportAsString()), "beans.xml");
        // Session bean and no beans.xml
        JavaArchive delta = ShrinkWrap.create(JavaArchive.class, DELTA_JAR).addClasses(Delta.class, DeltaLocal.class);
        // Session bean and 1.1 version beans.xml with bean-discovery-mode of annotated
        JavaArchive echo = ShrinkWrap
                .create(JavaArchive.class, ECHO_JAR)
                .addClasses(Echo.class, EchoLocal.class)
                .addAsManifestResource(
                        new StringAsset(newBeans11Descriptor().setBeanDiscoveryMode(BeanDiscoveryMode.ANNOTATED)
                                .exportAsString()), "beans.xml");
        // Session bean and 1.1 version beans.xml with bean-discovery-mode of none
        JavaArchive foxtrot = ShrinkWrap
                .create(JavaArchive.class, FOXTROT_JAR)
                .addClasses(Foxtrot.class, FoxtrotLocal.class)
                .addAsManifestResource(
                        new StringAsset(newBeans11Descriptor().setBeanDiscoveryMode(BeanDiscoveryMode.NONE).exportAsString()),
                        "beans.xml");

        WebArchive webArchive = new WebArchiveBuilder()
                .withClasses(EnterpriseBeanDiscoveryTest.class)
                .notTestArchive()
                .build()
                .setManifest(
                        new StringAsset(Descriptors.create(ManifestDescriptor.class)
                                .addToClassPath(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME).addToClassPath(ALPHA_JAR)
                                .addToClassPath(BRAVO_JAR).addToClassPath(CHARLIE_JAR).addToClassPath(DELTA_JAR)
                                .addToClassPath(ECHO_JAR).addToClassPath(FOXTROT_JAR).exportAsString()));

        return new EnterpriseArchiveBuilder().noDefaultWebModule().withTestClassDefinition(EnterpriseBeanDiscoveryTest.class)
                .withClasses(VerifyingExtension.class).withExtension(VerifyingExtension.class).withLibrary(Ping.class).build()
                .addAsModules(webArchive, alpha, bravo, charlie, delta, echo, foxtrot);
    }

    @Inject
    VerifyingExtension extension;

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "ba"), @SpecAssertion(section = BEAN_DISCOVERY, id = "tc") })
    public void testExplicitBeanArchiveModeAll() {
        assertDiscoveredAndAvailable(AlphaLocal.class, Alpha.class);
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "bb"), @SpecAssertion(section = BEAN_ARCHIVE, id = "bc"),
            @SpecAssertion(section = BEAN_DISCOVERY, id = "tc") })
    public void testExplicitBeanArchiveEmptyDescriptor() {
        assertDiscoveredAndAvailable(BravoLocal.class, Bravo.class);
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "bc"), @SpecAssertion(section = BEAN_DISCOVERY, id = "tc"),
            @SpecAssertion(section = BEAN_DISCOVERY, id = "ta") })
    public void testExplicitBeanArchiveLegacyDescriptor() {
        assertDiscoveredAndAvailable(CharlieLocal.class, Charlie.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "ca"), @SpecAssertion(section = BEAN_DISCOVERY, id = "tc"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "b") })
    public void testImplicitBeanArchiveNoDescriptor() {
        assertDiscoveredAndAvailable(DeltaLocal.class, Delta.class);
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "ca"), @SpecAssertion(section = BEAN_DISCOVERY, id = "tc"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "b") })
    public void testImplicitBeanArchiveModeAnnotated() {
        assertDiscoveredAndAvailable(EchoLocal.class, Echo.class);
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "oa"), @SpecAssertion(section = BEAN_DISCOVERY, id = "tc") })
    public void testNoBeanArchiveModeNone() {
        assertNotDiscoveredAndNotAvailable(FoxtrotLocal.class, Foxtrot.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "ob") })
    public void testNotBeanArchiveExtension() {
        assertNotDiscoveredAndNotAvailable(LegacyBean.class, LegacyBean.class);
    }

    private <T extends Ping, B extends Ping> void assertDiscoveredAndAvailable(Class<T> beanType, Class<B> beanClazz) {
        T instance = getContextualReference(beanType);
        assertNotNull(instance);
        assertTrue(extension.getObservedAnnotatedTypes().contains(beanClazz));
        instance.pong();
    }


    private <T extends Ping, B extends Ping> void assertNotDiscoveredAndNotAvailable(Class<T> beanType, Class<B> beanClazz) {
        assertFalse(extension.getObservedAnnotatedTypes().contains(beanClazz));
        assertTrue(getBeans(beanType).isEmpty());
    }

}
