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

package org.jboss.cdi.tck.tests.deployment.packaging.ejb;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.ejb.EJB;
import javax.enterprise.inject.spi.Extension;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "cdi", version = "20091101")
public class EJBJarDeploymentTest extends AbstractTest {

    @Deployment(name = "EJB", order = 2, testable = false)
    public static JavaArchive createEjbtArchive() {
        return ShrinkWrap
                .create(JavaArchive.class, "test-ejb.jar")
                .addClasses(FooRemote.class, FooBean.class, Bar.class, ProcessBeanObserver.class)
                .addAsServiceProvider(Extension.class, ProcessBeanObserver.class)
                .addAsManifestResource(new StringAsset(Descriptors.create(BeansDescriptor.class).exportAsString()), "beans.xml");
    }

    @Deployment(name = "TEST", order = 1)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(EJBJarDeploymentTest.class).withClasses(FooRemote.class)
                .setAsClientMode(false).build();
    }

    @EJB(lookup = "java:global/test-ejb/FooBean!org.jboss.cdi.tck.tests.deployment.packaging.ejb.FooRemote")
    FooRemote foo;

    @OperateOnDeployment("TEST")
    @SpecAssertion(section = "12.1", id = "bcd")
    public void testContainerSearchesEJBJar() throws Exception {
        assertNotNull(foo);
        assertTrue(foo.isBarBeanObserved());
    }

}
