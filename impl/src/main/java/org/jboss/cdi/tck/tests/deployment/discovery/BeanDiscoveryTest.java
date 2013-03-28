package org.jboss.cdi.tck.tests.deployment.discovery;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DEFINING_ANNOTATIONS;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY;
import static org.jboss.cdi.tck.shrinkwrap.descriptors.Beans11DescriptorImpl.newBeans11Descriptor;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.descriptors.Beans11DescriptorImpl.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class BeanDiscoveryTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {

        // 1.1 version beans.xml with bean-discovery-mode of all
        JavaArchive alpha = ShrinkWrap
                .create(JavaArchive.class)
                .addClass(Alpha.class)
                .addAsManifestResource(
                        new StringAsset(newBeans11Descriptor().setBeanDiscoveryMode(BeanDiscoveryMode.ALL).exportAsString()),
                        "beans.xml");
        // Empty beans.xml
        JavaArchive bravo = ShrinkWrap.create(JavaArchive.class).addClass(Bravo.class)
                .addAsManifestResource(new StringAsset(""), "beans.xml");
        // No version beans.xml
        JavaArchive charlie = ShrinkWrap
                .create(JavaArchive.class)
                .addClass(Charlie.class)
                .addAsManifestResource(new StringAsset(Descriptors.create(BeansDescriptor.class).exportAsString()), "beans.xml");
        // Bean defining annotation and no beans.xml
        JavaArchive delta = ShrinkWrap.create(JavaArchive.class).addClass(Delta.class);
        // Bean defining annotation and 1.1 version beans.xml with bean-discovery-mode of annotated
        JavaArchive echo = ShrinkWrap
                .create(JavaArchive.class)
                .addClass(Echo.class)
                .addAsManifestResource(
                        new StringAsset(newBeans11Descriptor().setBeanDiscoveryMode(BeanDiscoveryMode.ANNOTATED)
                                .exportAsString()), "beans.xml");
        // Bean defining annotation and 1.1 version beans.xml with bean-discovery-mode of none
        JavaArchive foxtrot = ShrinkWrap
                .create(JavaArchive.class)
                .addClass(Foxtrot.class)
                .addAsManifestResource(
                        new StringAsset(newBeans11Descriptor().setBeanDiscoveryMode(BeanDiscoveryMode.NONE).exportAsString()),
                        "beans.xml");

        return new WebArchiveBuilder().withTestClass(BeanDiscoveryTest.class).withClasses(VerifyingExtension.class)
                .withExtension(VerifyingExtension.class).withLibrary(Ping.class)
                .withLibraries(alpha, bravo, charlie, delta, echo, foxtrot).build();
    }

    @Inject
    VerifyingExtension extension;

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "ba"), @SpecAssertion(section = BEAN_DISCOVERY, id = "ta") })
    public void testExplicitBeanArchiveModeAll(Alpha alpha) {
        assertDiscoveredAndAvailable(alpha, Alpha.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "bb"), @SpecAssertion(section = BEAN_ARCHIVE, id = "bc"),
            @SpecAssertion(section = BEAN_DISCOVERY, id = "ta") })
    public void testExplicitBeanArchiveEmptyDescriptor(Bravo bravo) {
        assertDiscoveredAndAvailable(bravo, Bravo.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "bc"), @SpecAssertion(section = BEAN_ARCHIVE, id = "bc"),
            @SpecAssertion(section = BEAN_DISCOVERY, id = "ta") })
    public void testExplicitBeanArchiveLegacyDescriptor(Charlie charlie) {
        assertDiscoveredAndAvailable(charlie, Charlie.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "ca"), @SpecAssertion(section = BEAN_DISCOVERY, id = "tb"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "b") })
    public void testImplicitBeanArchiveNoDescriptor(Delta delta) {
        assertDiscoveredAndAvailable(delta, Delta.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "ca"), @SpecAssertion(section = BEAN_DISCOVERY, id = "tb"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "b") })
    public void testImplicitBeanArchiveModeAnnotated(Echo echo) {
        assertDiscoveredAndAvailable(echo, Echo.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "cb"), @SpecAssertion(section = BEAN_DISCOVERY, id = "tb") })
    public void testImplicitBeanArchiveModeNone(Foxtrot foxtrot) {
        assertNotDiscoveredAndNotAvailable(foxtrot, Foxtrot.class);
    }

    private <T extends Ping> void assertDiscoveredAndAvailable(T reference, Class<T> clazz) {
        assertTrue(extension.getObservedAnnotatedTypes().contains(clazz));
        assertNotNull(reference);
        reference.pong();
        getUniqueBean(clazz);
    }

    private <T extends Ping> void assertNotDiscoveredAndNotAvailable(T reference, Class<T> clazz) {
        assertFalse(extension.getObservedAnnotatedTypes().contains(clazz));
        assertNull(reference);
        assertTrue(getBeans(clazz).isEmpty());
    }

}
