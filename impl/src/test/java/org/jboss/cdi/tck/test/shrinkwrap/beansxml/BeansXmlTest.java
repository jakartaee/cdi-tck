package org.jboss.cdi.tck.test.shrinkwrap.beansxml;

import org.jboss.cdi.tck.shrinkwrap.AssetUtil;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.BeansXmlVersion;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.junit.Assert;
import org.junit.Test;

/**
 * A very simple test that checks if {@link BeansXml} API constructs a valid descriptor with certain format.
 * Note that {@link BeansXml} doesn't include XML namespace.
 */
public class BeansXmlTest {

    // prepared String representation of expected outcome
    private final String expectedBeansXmlFormat = "<beans version=\"1.1\" bean-discovery-mode=\"annotated\">\n" +
            "<scan>\n" +
            "<exclude name=\"org.jboss.cdi.tck.test.shrinkwrap.beansxml.*\" />\n" +
            "</scan>\n" +
            "<alternatives>\n" +
            "<class>org.jboss.cdi.tck.test.shrinkwrap.beansxml.DummyReferenceClass</class>\n" +
            "<stereotype>org.jboss.cdi.tck.test.shrinkwrap.beansxml.DummyReferenceClass</stereotype>\n" +
            "</alternatives>\n" +
            "<interceptors>\n" +
            "<class>org.jboss.cdi.tck.test.shrinkwrap.beansxml.DummyReferenceClass</class>\n" +
            "</interceptors>\n" +
            "<decorators>\n" +
            "<class>org.jboss.cdi.tck.test.shrinkwrap.beansxml.DummyReferenceClass</class>\n" +
            "</decorators>\n" +
            "</beans>\n";

    @Test
    public void testGeneratedBeansXml() {
        BeansXml asset = new BeansXml(BeanDiscoveryMode.ANNOTATED)
                .setBeansXmlVersion(BeansXmlVersion.v11)
                .alternatives(DummyReferenceClass.class)
                .stereotype(DummyReferenceClass.class)
                .decorators(DummyReferenceClass.class)
                .interceptors(DummyReferenceClass.class)
                .excludeFilters(BeansXml.Exclude.match(BeansXmlTest.class.getPackage().getName() + ".*"));
        String stringRepresentation = AssetUtil.readAssetContent(asset);
        Assert.assertEquals(expectedBeansXmlFormat, stringRepresentation);
    }
}
