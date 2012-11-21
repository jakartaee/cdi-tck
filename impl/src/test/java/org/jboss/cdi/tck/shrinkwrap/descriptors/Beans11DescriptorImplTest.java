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

package org.jboss.cdi.tck.shrinkwrap.descriptors;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.shrinkwrap.descriptor.api.DescriptorExportException;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 * 
 */
public class Beans11DescriptorImplTest {

    @Test
    public void testAlternatives() throws DescriptorExportException, IOException {
        checkResult(new Beans11DescriptorImpl().alternatives(new BeansXmlStereotype(Foo.class, 100), new BeansXmlClass(
                Bar.class, false, 200)), "src/test/resources/beans-01.xml");
    }

    @Test
    public void testInterceptors() throws DescriptorExportException, IOException {
        checkResult(new Beans11DescriptorImpl().interceptors(new BeansXmlClass(Bar.class, false, 200), new BeansXmlClass(
                Foo.class, 800)), "src/test/resources/beans-02.xml");
    }

    @Test
    public void testDecorators() throws DescriptorExportException, IOException {
        checkResult(new Beans11DescriptorImpl().decorators(new BeansXmlClass(Bar.class), new BeansXmlClass(Foo.class, 100)),
                "src/test/resources/beans-03.xml");
    }

    private void checkResult(Beans11DescriptorImpl descriptor, String filePath) throws DescriptorExportException, IOException {
        assertEquals(prepare(descriptor.exportAsString()), prepare(FileUtils.readFileToString(new File(filePath))));
    }

    private String prepare(String text) {
        return StringUtils.deleteWhitespace(text);
    }

}
