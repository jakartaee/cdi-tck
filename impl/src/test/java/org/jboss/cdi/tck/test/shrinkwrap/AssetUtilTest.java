/*
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.test.shrinkwrap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.apache.commons.io.FileUtils;
import org.jboss.cdi.tck.shrinkwrap.AssetUtil;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.BeansXmlVersion;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class AssetUtilTest {

    @Test
    public void testReadAssetContentAsString() throws IOException {

        // TODO these are weak assertions because BeansXml cannot be translated to String easily
        Asset asset = new BeansXml(BeanDiscoveryMode.ANNOTATED).alternatives(Engine.class).setBeansXmlVersion(BeansXmlVersion.v20);
        String content = AssetUtil.readAssetContent(asset);
        assertTrue(content != null);
        assertTrue(content.length() > 0);
        assertTrue(content.contains("bean-discovery-mode=\"annotated\""));
        assertTrue(content.contains("version=\"2.0\""));
        assertTrue(content.contains("<alternatives>"));
        assertTrue(content.contains("<class>org.jboss.cdi.tck.test.shrinkwrap.Engine</class>"));
        assertTrue(content.contains("</alternatives>"));

        ClassLoaderAsset classLoaderAsset = new ClassLoaderAsset("beans-01.xml");
        assertEquals(AssetUtil.readAssetContent(classLoaderAsset).trim().replaceAll("\\s", ""),
                FileUtils.readFileToString(new File("src/test/resources/beans-01.xml")).trim().replaceAll("\\s", ""));
    }

}
