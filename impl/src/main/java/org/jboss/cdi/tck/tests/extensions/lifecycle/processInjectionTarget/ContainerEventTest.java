/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processInjectionTarget;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.PIT;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * This test verifies that ProcessInjectionTarget event is fired for various Java EE components.
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class ContainerEventTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ContainerEventTest.class)
                .withExtension(ProcessInjectionTargetObserver.class)
                .withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml")
                .withWebResource("TestLibrary.tld", "WEB-INF/TestLibrary.tld").build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "aac"), @SpecAssertion(section = PIT, id = "abc"),
            @SpecAssertion(section = BEAN_DISCOVERY, id = "de") })
    public void testProcessInjectionTargetEventFiredForServletListener() {
        assertNotNull(ProcessInjectionTargetObserver.getListenerEvent());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "aad"), @SpecAssertion(section = PIT, id = "abd"),
            @SpecAssertion(section = BEAN_DISCOVERY, id = "df") })
    public void testProcessInjectionTargetEventFiredForTagHandler() {
        assertNotNull(ProcessInjectionTargetObserver.getTagHandlerEvent());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "aae"), @SpecAssertion(section = PIT, id = "abe"),
            @SpecAssertion(section = BEAN_DISCOVERY, id = "dg") })
    public void testProcessInjectionTargetEventFiredForTagLibraryListener() {
        assertNotNull(ProcessInjectionTargetObserver.getTagLibraryListenerEvent());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "aah"), @SpecAssertion(section = PIT, id = "abh"),
            @SpecAssertion(section = BEAN_DISCOVERY, id = "dj") })
    public void testProcessInjectionTargetEventFiredForServlet() {
        assertNotNull(ProcessInjectionTargetObserver.getServletEvent());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "aai"), @SpecAssertion(section = PIT, id = "abi"),
            @SpecAssertion(section = BEAN_DISCOVERY, id = "dk") })
    public void testProcessInjectionTargetEventFiredForFilter() {
        assertNotNull(ProcessInjectionTargetObserver.getFilterEvent());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "aas"), @SpecAssertion(section = PIT, id = "aao"),
            @SpecAssertion(section = PIT, id = "aan") })
    public void testTypeOfProcessInjectionTargetParameter() {
        assertFalse(ProcessInjectionTargetObserver.isStringObserved());
        assertTrue(ProcessInjectionTargetObserver.isTagHandlerSubTypeObserved());
        assertFalse(ProcessInjectionTargetObserver.isTagHandlerSuperTypeObserved());
        assertFalse(ProcessInjectionTargetObserver.isServletSuperTypeObserved());
        assertTrue(ProcessInjectionTargetObserver.isServletSubTypeObserved());
        assertFalse(ProcessInjectionTargetObserver.isListenerSuperTypeObserved());
    }

}
