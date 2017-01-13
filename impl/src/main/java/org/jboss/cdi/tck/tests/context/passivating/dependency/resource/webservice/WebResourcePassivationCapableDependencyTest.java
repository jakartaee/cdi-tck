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
package org.jboss.cdi.tck.tests.context.passivating.dependency.resource.webservice;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.JAX_WS;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_DEPENDENCY_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0-PFD")
public class WebResourcePassivationCapableDependencyTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(WebResourcePassivationCapableDependencyTest.class).build();
    }

    @Test(groups = { JAVAEE_FULL, JAX_WS })
    @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY_EE, id = "de")
    public void testWebResourcePassivated() throws IOException, ClassNotFoundException {

        PassivatedBean bean = getContextualReference(PassivatedBean.class);

        assertNotNull(bean);
        assertNotNull(bean.getHelloWeb());

        String beanId = bean.getId();

        byte[] serializedBean = passivate(bean);

        PassivatedBean beanCopy = (PassivatedBean) activate(serializedBean);
        String message = beanCopy.getHelloWeb().sayHello("John");

        assertEquals(beanCopy.getId(), beanId);
        assertEquals(message, "Hello John.");
    }

}