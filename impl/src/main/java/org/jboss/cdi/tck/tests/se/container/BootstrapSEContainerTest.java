/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.se.container;

import static org.jboss.cdi.tck.TestGroups.SE;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE_SE;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.spi.CDI;

import org.jboss.arquillian.container.se.api.ClassPath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

// TODO reflect new assertions
@Test(groups = SE)
@SpecVersion(spec = "cdi", version = "2.0-EDR1")
public class BootstrapSEContainerTest extends Arquillian {

    private static final String IMPLICIT_SCAN_KEY = "javax.enterprise.inject.scan.implicit";

    @Deployment
    public static Archive<?> deployment() {
        final JavaArchive testArchive = ShrinkWrap.create(JavaArchive.class).addClasses(Foo.class, BootstrapSEContainerTest.class)
                .addAsResource(EmptyAsset.INSTANCE,
                        "META-INF/beans.xml");
        final JavaArchive implicitArchive = ShrinkWrap.create(JavaArchive.class).addClass(Bar.class);
        return ClassPath.builder().add(testArchive, implicitArchive).build();
    }

    @Test
//    @SpecAssertions({ @SpecAssertion(section = BOOTSTRAPSE, id = "a"), @SpecAssertion(section = INIT_CONTAINER, id = "a"),
//            @SpecAssertion(section = STOP_CONTAINER, id = "a") })
    public void testContainerIsInitialized() {
        //        FIXME
        //        CDIProvider cdiProvider = CDI.getCDIProvider();
        try (CDI<Object> cdi = CDI.current()) {
            //            Assert.assertTrue(cdiProvider.isInitialized());

            Foo foo = cdi.select(Foo.class).get();
            Assert.assertNotNull(foo);
            foo.ping();
        }
        //        Assert.assertFalse(cdiProvider.isInitialized());
    }

    @Test(expectedExceptions = IllegalStateException.class)
//    @SpecAssertions(@SpecAssertion(section = STOP_CONTAINER, id = "b"))
    public void testContainerShutdownMethodOnNotInitializedContainer() {
        //        FIXME
        //        CDIProvider cdiProvider = CDI.getCDIProvider();
        CDI<Object> cdi = CDI.current();
        cdi.shutdown();
        cdi.shutdown();
    }

    @Test
//    @SpecAssertions({ @SpecAssertion(section = BOOTSTRAPSE, id = "a"), @SpecAssertion(section = INIT_CONTAINER, id = "b") })
    public void testInvocationOfInitializedMethodReturnsNewCDIInstance() {
        //        FIXME
        //        CDIProvider cdiProvider = CDI.getCDIProvider();
        CDI<Object> cdi1 = CDI.current();//.initialize();
        Assert.assertNotNull(cdi1);
        cdi1.shutdown();
        CDI<Object> cdi2 = CDI.current();//.initialize();
        Assert.assertNotNull(cdi2);
        cdi2.shutdown();
        Assert.assertNotEquals(cdi1, cdi2);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE_SE, id = "b") })
    public void testImplicitArchiveDiscovered() {
        Map<String, Object> params = new HashMap<>();
        params.put(IMPLICIT_SCAN_KEY, Boolean.TRUE);

        //        FIXME
        //        CDIProvider cdiProvider = CDI.getCDIProvider();
        //        try (CDI<Object> cdi = cdiProvider.initialize(params)) {
        //            Bar bar = cdi.select(Bar.class).get();
        //            Assert.assertNotNull(bar);
        //            bar.ping();
        //        }
    }

}
