/*
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.deployment.discovery;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DEFINING_ANNOTATIONS;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_DISCOVERY_STEPS;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.spi.Extension;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.BeansXmlVersion;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 * @author Matus Abaffy
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class BeanDiscoveryTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {

        // beans.xml with bean-discovery-mode of all
        JavaArchive alpha = ShrinkWrap
                .create(JavaArchive.class)
                .addClass(Alpha.class)
                .addAsManifestResource(new BeansXml(BeanDiscoveryMode.ALL), "beans.xml");
        // beans.xml with version 1.1 and bean-discovery-mode of all
        JavaArchive alpha2 = ShrinkWrap
                .create(JavaArchive.class)
                .addClass(Alpha2.class)
                .addAsManifestResource(new BeansXml(BeanDiscoveryMode.ALL).setBeansXmlVersion(BeansXmlVersion.v11), "beans.xml");
        // beans.xml with version 2.0 and bean-discovery-mode of all
        JavaArchive alpha3 = ShrinkWrap
                .create(JavaArchive.class)
                .addClass(Alpha3.class)
                .addAsManifestResource(new BeansXml(BeanDiscoveryMode.ALL).setBeansXmlVersion(BeansXmlVersion.v20), "beans.xml");
        // Empty beans.xml, since CDI 4.0 this is discovery mode "annotated"
        JavaArchive bravo = ShrinkWrap.create(JavaArchive.class).addClass(Bravo.class)
                .addAsManifestResource(new StringAsset(""), "beans.xml");
        // No version beans.xml, since CDI 4.0 this is "annotated" unless explicitly stated otherwise
        JavaArchive charlie = ShrinkWrap
                .create(JavaArchive.class)
                .addClass(Charlie.class)
                // using null values enforces missing attribute in resulting XML file
                .addAsManifestResource(new BeansXml().setBeansXmlVersion(null).setBeanDiscoveryMode(null),"beans.xml");
        // Bean defining annotation and no beans.xml
        JavaArchive delta = ShrinkWrap.create(JavaArchive.class).addClasses(Delta.class, Golf.class, India.class, Kilo.class,
                Mike.class, Interceptor1.class, Decorator1.class);
        // Bean defining annotation and beans.xml with bean-discovery-mode of annotated
        JavaArchive echo = ShrinkWrap
                .create(JavaArchive.class)
                .addClasses(Echo.class, EchoNotABean.class, Hotel.class, Juliet.class, JulietNotABean.class, Lima.class,
                        November.class, Interceptor2.class, Decorator2.class)
                .addAsManifestResource(new BeansXml(BeanDiscoveryMode.ANNOTATED), "beans.xml");
        // Bean defining annotation and beans.xml with bean-discovery-mode of none
        JavaArchive foxtrot = ShrinkWrap
                .create(JavaArchive.class)
                .addClass(Foxtrot.class)
                .addAsManifestResource(new BeansXml(BeanDiscoveryMode.NONE), "beans.xml");

        // Archive which contains an extension and no beans.xml file
        JavaArchive legacy = ShrinkWrap.create(JavaArchive.class).addClasses(LegacyExtension.class, LegacyAlpha.class,
                LegacyBravo.class).addAsServiceProvider(Extension.class, LegacyExtension.class);

        return new WebArchiveBuilder().withTestClass(BeanDiscoveryTest.class)
                .withClasses(VerifyingExtension.class, ScopesExtension.class, Binding.class, MyNormalScope.class, MyPseudoScope.class,
                        MyNormalContext.class, MyPseudoContext.class, MyStereotype.class)
                .withExtensions(VerifyingExtension.class, ScopesExtension.class).withLibrary(Ping.class)
                .withLibraries(alpha, alpha2, alpha3, bravo, charlie, delta, echo, foxtrot, legacy).build();
    }

    @Inject
    VerifyingExtension extension;

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "ba"), @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "a") })
    public void testExplicitBeanArchiveModeAll(Alpha alpha) {
        assertDiscoveredAndAvailable(alpha, Alpha.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "ba"), @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "a") })
    public void testExplicitBeanArchiveModeAllVersion11(Alpha2 alpha) {
        assertDiscoveredAndAvailable(alpha, Alpha2.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "ba"), @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "a") })
    public void testExplicitBeanArchiveModeAllVersion20(Alpha3 alpha) {
        assertDiscoveredAndAvailable(alpha, Alpha3.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "bb"), @SpecAssertion(section = BEAN_ARCHIVE, id = "bc"),
            @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "a") })
    public void testImplicitBeanArchiveEmptyDescriptor(Bravo bravo) {
        assertDiscoveredAndAvailable(bravo, Bravo.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    // TODO might require change of @SpecAssertions value due to spec wording changes
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "bc"), @SpecAssertion(section = BEAN_ARCHIVE, id = "bc"),
            @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "a") })
    public void testArchiveWithNoVersionBeansXml(Charlie charlie) {
        assertDiscoveredAndAvailable(charlie, Charlie.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "ca"), @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "b"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "ba"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "bb") })
    public void testNormalScopeImplicitBeanArchiveNoDescriptor(Delta delta, Golf golf) {
        assertDiscoveredAndAvailable(delta, Delta.class);
        assertDiscoveredAndAvailable(golf, Golf.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "ca"), @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "b"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "ba"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "bb") })
    public void testNormalScopeImplicitBeanArchiveModeAnnotated(Echo echo, Hotel hotel) {
        assertDiscoveredAndAvailable(echo, Echo.class);
        assertNotDiscoveredAndNotAvailable(EchoNotABean.class);
        assertDiscoveredAndAvailable(hotel, Hotel.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "b"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "bf") })
    public void testDependentScopeImplicitBeanArchiveNoDescriptor(India india) {
        assertDiscoveredAndAvailable(india, India.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "b"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "bf"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "ca") })
    public void testPseudoScopeImplicitBeanArchiveModeAnnotated(Juliet juliet) {
        assertDiscoveredAndAvailable(juliet, Juliet.class);
        assertNotDiscoveredAndNotAvailable(JulietNotABean.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "bc"),
            @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "b") })
    public void testInterceptorIsBeanDefiningAnnotation() {
        assertDiscovered(Interceptor1.class);
        assertDiscovered(Interceptor2.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "bd"),
            @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "b") })
    public void testDecoratorIsBeanDefiningAnnotation() {
        assertDiscovered(Decorator1.class);
        assertDiscovered(Decorator2.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "b"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "be"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "cb") })
    public void testStereotypeImplicitBeanArchiveNoDescriptor(Mike mike) {
        assertDiscoveredAndAvailable(mike, Mike.class);
        assertDiscovered(Kilo.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "b"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "be"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "cb") })
    public void testStereotypeImplicitBeanArchiveModeAnnotated(November november) {
        assertDiscoveredAndAvailable(november, November.class);
        assertDiscovered(Lima.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "oa") })
    public void testNoBeanArchiveModeNone() {
        assertNotDiscoveredAndNotAvailable(Foxtrot.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "ob") })
    public void testNotBeanArchiveExtension(LegacyAlpha legacyAlpha) {
        assertDiscoveredAndAvailable(legacyAlpha, LegacyAlpha.class);
        assertNotDiscoveredAndNotAvailable(LegacyBravo.class);
    }

    private <T extends Ping> void assertDiscoveredAndAvailable(T reference, Class<T> clazz) {
        assertDiscovered(clazz);
        assertNotNull(reference);
        reference.pong();
        getUniqueBean(clazz);
    }

    private void assertDiscovered(Class<?> clazz) {
        assertTrue(extension.getObservedAnnotatedTypes().contains(clazz), clazz.getSimpleName() + " not discovered.");
    }

    private <T> void assertNotDiscoveredAndNotAvailable(Class<T> clazz) {
        assertFalse(extension.getObservedAnnotatedTypes().contains(clazz));
        assertTrue(getBeans(clazz).isEmpty());
    }

}
