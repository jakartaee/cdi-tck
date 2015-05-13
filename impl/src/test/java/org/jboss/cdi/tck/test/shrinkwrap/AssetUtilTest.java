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
package org.jboss.cdi.tck.test.shrinkwrap;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jboss.cdi.tck.shrinkwrap.AssetUtil;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.testng.annotations.Test;

public class AssetUtilTest {

    @Test
    public void testReadAssetContentAsString() throws IOException {

        String content = Descriptors.create(BeansDescriptor.class).exportAsString();
        StringAsset stringAsset = new StringAsset(content);
        assertEquals(AssetUtil.readAssetContent(stringAsset).trim(), content.trim());

        ClassLoaderAsset classLoaderAsset = new ClassLoaderAsset("beans-01.xml");
        assertEquals(AssetUtil.readAssetContent(classLoaderAsset).trim().replaceAll("\\s", ""),
                FileUtils.readFileToString(new File("src/test/resources/beans-01.xml")).trim().replaceAll("\\s", ""));
    }

}
