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
package org.jboss.cdi.tck.tests.deployment.lifecycle;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author pmuir
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
@Test(groups = INTEGRATION)
public class ExtensionsTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ExtensionsTest.class)
                .withExtension(BeforeBeanDiscoveryObserver.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.1", id = "a"), @SpecAssertion(section = "12.2", id = "b"),
            @SpecAssertion(section = "12.2", id = "c") })
    public void testBeforeBeanDiscoveryEventIsCalled() {
        assertTrue(BeforeBeanDiscoveryObserver.isObserved());
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertion(section = "11.5.1", id = "ab")
    public void testAddingBindingType() {
        assertTrue(BeforeBeanDiscoveryObserver.isObserved());
        assertEquals(getBeans(Alligator.class).size(), 0);
        assertEquals(getBeans(Alligator.class, new AnnotationLiteral<Tame>() {
        }).size(), 1);
        assertTrue(getCurrentManager().isQualifier(Tame.class));
    }

    @Test
    @SpecAssertion(section = "11.5.1", id = "ac")
    public void testAddingScopeType() {
        assertTrue(BeforeBeanDiscoveryObserver.isObserved());
        assertEquals(getBeans(RomanEmpire.class).size(), 1);
        Bean<RomanEmpire> bean = getBeans(RomanEmpire.class).iterator().next();
        assertTrue(bean.getScope().equals(EpochScoped.class));
    }

}
