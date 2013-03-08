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

package org.jboss.cdi.tck.test.shrinkwrap.descriptors;

import static org.jboss.cdi.tck.shrinkwrap.descriptors.Beans11DescriptorImpl.newBeans11Descriptor;
import static org.jboss.cdi.tck.shrinkwrap.descriptors.ClassActivator.newClassAvailableActivator;
import static org.jboss.cdi.tck.shrinkwrap.descriptors.ClassActivator.newClassNotAvailableActivator;
import static org.jboss.cdi.tck.shrinkwrap.descriptors.Exclude.newExclude;
import static org.jboss.cdi.tck.shrinkwrap.descriptors.SystemPropertyActivator.newSystemPropertyActivator;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.cdi.tck.shrinkwrap.descriptors.Beans11DescriptorImpl;
import org.jboss.cdi.tck.shrinkwrap.descriptors.Beans11DescriptorImpl.BeanDiscoveryMode;
import org.jboss.shrinkwrap.descriptor.api.DescriptorExportException;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 *
 */
public class Beans11DescriptorImplTest {

    @Test
    public void testAlternatives() throws Exception {
        checkResult(newBeans11Descriptor().alternatives(Foo.class).alternativeStereotypes(Bar.class),
                "src/test/resources/beans-01.xml");
    }

    @Test
    public void testInterceptors() throws Exception {
        checkResult(newBeans11Descriptor().interceptors(Bar.class, Foo.class), "src/test/resources/beans-02.xml");
    }

    @Test
    public void testDecorators() throws Exception {
        checkResult(newBeans11Descriptor().decorators(Bar.class, Foo.class), "src/test/resources/beans-03.xml");
    }

    @Test
    public void testBeanDiscoveryMode() throws Exception {
        checkResult(newBeans11Descriptor().setBeanDiscoveryMode(BeanDiscoveryMode.ALL), "src/test/resources/beans-04.xml");
    }

    @Test
    public void testExcludes() throws Exception {
        checkResult(
                newBeans11Descriptor().excludes(
                        newExclude("org.jboss.cdi.tck.*"),
                        newExclude("org.jboss.cdi.tck.**").activators(newClassAvailableActivator("Foo"),
                                newClassNotAvailableActivator("Bar"), newSystemPropertyActivator("HOME"),
                                newSystemPropertyActivator("HOME").setValue("true"))), "src/test/resources/beans-05.xml");
    }

    private void checkResult(Beans11DescriptorImpl descriptor, String filePath) throws DescriptorExportException, IOException {
        assertEquals(prepare(descriptor.exportAsString()), prepare(FileUtils.readFileToString(new File(filePath))));
    }

    private String prepare(String text) {
        return StringUtils.deleteWhitespace(text);
    }

}
