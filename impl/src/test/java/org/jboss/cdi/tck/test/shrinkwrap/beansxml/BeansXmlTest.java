/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.test.shrinkwrap.beansxml;

import org.jboss.cdi.tck.shrinkwrap.AssetUtil;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.BeansXmlVersion;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * A very simple test that checks if {@link BeansXml} API constructs a valid descriptor with certain format.
 * Note that {@link BeansXml} doesn't include XML namespace.
 */
public class BeansXmlTest {

    // prepared String representations of expected outcomes
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

    private final String expectedNoVersionBeanXmlFormat = "<beans>\n" +
            "<alternatives>\n" +
            "<class>org.jboss.cdi.tck.test.shrinkwrap.beansxml.DummyReferenceClass</class>\n" +
            "</alternatives>\n" +
            "<interceptors>\n" +
            "<class>org.jboss.cdi.tck.test.shrinkwrap.beansxml.DummyReferenceClass</class>\n" +
            "</interceptors>\n" +
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
        Assert.assertEquals(stringRepresentation, expectedBeansXmlFormat);
    }

    @Test
    public void testNoVersionNoModeBeansXml() {
        BeansXml asset = new BeansXml()
                .setBeansXmlVersion(null)
                .setBeanDiscoveryMode(null)
                .alternatives(DummyReferenceClass.class)
                .interceptors(DummyReferenceClass.class);
        String stringRepresentation = AssetUtil.readAssetContent(asset);
        Assert.assertEquals(stringRepresentation, expectedNoVersionBeanXmlFormat);
    }
}
