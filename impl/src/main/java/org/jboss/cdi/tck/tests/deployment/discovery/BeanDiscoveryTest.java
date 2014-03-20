package org.jboss.cdi.tck.tests.deployment.discovery;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DEFINING_ANNOTATIONS;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY;
import static org.jboss.cdi.tck.shrinkwrap.descriptors.Beans11DescriptorImpl.newBeans11Descriptor;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.inject.spi.Extension;
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
 * @author Matus Abaffy
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class BeanDiscoveryTest extends AbstractTest {

    @SuppressWarnings("unchecked")
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
        JavaArchive delta = ShrinkWrap.create(JavaArchive.class).addClasses(Delta.class, Golf.class, India.class, Kilo.class,
                Mike.class, Interceptor1.class, Decorator1.class);
        // Bean defining annotation and 1.1 version beans.xml with bean-discovery-mode of annotated
        JavaArchive echo = ShrinkWrap
                .create(JavaArchive.class)
                .addClasses(Echo.class, EchoNotABean.class, Hotel.class, Juliet.class, JulietNotABean.class, Lima.class,
                        November.class, Interceptor2.class, Decorator2.class)
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

        // Archive which contains an extension and no beans.xml file
        JavaArchive legacy = ShrinkWrap.create(JavaArchive.class).addClasses(LegacyExtension.class, LegacyAlpha.class,
                LegacyBravo.class).addAsServiceProvider(Extension.class, LegacyExtension.class);

        return new WebArchiveBuilder().withTestClass(BeanDiscoveryTest.class)
                .withClasses(VerifyingExtension.class, ScopesExtension.class, Binding.class, MyNormalScope.class, MyPseudoScope.class,
                        MyNormalContext.class, MyPseudoContext.class)
                .withExtensions(VerifyingExtension.class, ScopesExtension.class).withLibrary(Ping.class)
                .withLibraries(alpha, bravo, charlie, delta, echo, foxtrot, legacy).build();
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
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "ba"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "bb") })
    public void testNormalScopeImplicitBeanArchiveNoDescriptor(Delta delta, Golf golf) {
        assertDiscoveredAndAvailable(delta, Delta.class);
        assertDiscoveredAndAvailable(golf, Golf.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "ca"), @SpecAssertion(section = BEAN_DISCOVERY, id = "tb"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "ba"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "bb") })
    public void testNormalScopeImplicitBeanArchiveModeAnnotated(Echo echo, Hotel hotel) {
        assertDiscoveredAndAvailable(echo, Echo.class);
        assertNotDiscoveredAndNotAvailable(EchoNotABean.class);
        assertDiscoveredAndAvailable(hotel, Hotel.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_DISCOVERY, id = "tb"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "bf") })
    public void testDependentScopeImplicitBeanArchiveNoDescriptor(India india) {
        assertDiscoveredAndAvailable(india, India.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_DISCOVERY, id = "tb"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "bf"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "ca") })
    public void testPseudoScopeImplicitBeanArchiveModeAnnotated(Juliet juliet) {
        assertDiscoveredAndAvailable(juliet, Juliet.class);
        assertNotDiscoveredAndNotAvailable(JulietNotABean.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "bc"),
            @SpecAssertion(section = BEAN_DISCOVERY, id = "tb") })
    public void testInterceptorIsBeanDefiningAnnotation() {
        assertDiscovered(Interceptor1.class);
        assertDiscovered(Interceptor2.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "bd"),
            @SpecAssertion(section = BEAN_DISCOVERY, id = "tb") })
    public void testDecoratorIsBeanDefiningAnnotation() {
        assertDiscovered(Decorator1.class);
        assertDiscovered(Decorator2.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_DISCOVERY, id = "tb"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "be"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "cb") })
    public void testStereotypeImplicitBeanArchiveNoDescriptor(Mike mike) {
        assertDiscoveredAndAvailable(mike, Mike.class);
        assertDiscovered(Kilo.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_DISCOVERY, id = "tb"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "be"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "cb") })
    public void testStereotypeImplicitBeanArchiveModeAnnotatedr(November november) {
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
