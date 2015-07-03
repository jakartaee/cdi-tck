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
package org.jboss.cdi.tck.tests.extensions.lifecycle.bbd;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY_STEPS;
import static org.jboss.cdi.tck.cdi.Sections.BEFORE_BEAN_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.INITIALIZATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.extensions.lifecycle.bbd.lib.Bar;
import org.jboss.cdi.tck.tests.extensions.lifecycle.bbd.lib.Boss;
import org.jboss.cdi.tck.tests.extensions.lifecycle.bbd.lib.Foo;
import org.jboss.cdi.tck.tests.extensions.lifecycle.bbd.lib.Pro;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author pmuir
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0-EDR1")
@Test(groups = INTEGRATION)
public class BeforeBeanDiscoveryTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeforeBeanDiscoveryTest.class)
                .withExtension(BeforeBeanDiscoveryObserver.class)
                .withLibrary(Boss.class, Foo.class, Bar.class, Pro.class)
                .build();
    }

    @Test
    @SpecAssertions({
        @SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "a"),
        @SpecAssertion(section = INITIALIZATION, id = "b"),
        @SpecAssertion(section = INITIALIZATION, id = "c")})
    public void testBeforeBeanDiscoveryEventIsCalled() {
        assertTrue(BeforeBeanDiscoveryObserver.isObserved());
    }

    @Test
    @SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "ac")
    public void testAddingScopeType() {
        assertTrue(BeforeBeanDiscoveryObserver.isObserved());
        assertEquals(getBeans(RomanEmpire.class).size(), 1);
        Bean<RomanEmpire> bean = getBeans(RomanEmpire.class).iterator().next();
        assertTrue(bean.getScope().equals(EpochScoped.class));
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "ab")
    public void testAddingQualifierByClass() {
        assertTrue(BeforeBeanDiscoveryObserver.isObserved());
        assertEquals(getBeans(Alligator.class).size(), 0);
        assertEquals(getBeans(Alligator.class, new AnnotationLiteral<Tame>() {
        }).size(), 1);
        assertTrue(getCurrentManager().isQualifier(Tame.class));
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "aba")
    public void testAddingQualifierByAnnotatedType() {
        assertTrue(BeforeBeanDiscoveryObserver.isObserved());

        // @Skill#level should be ignored
        assertEquals(beanManager.getBeans(Programmer.class, new SkillLiteral() {
            @Override
            public String language() {
                return "Java";
            }

            @Override
            public String level() {
                return "whatever";
            }
        }).size(), 1);

        // there should be no @Skill(language="C++")Programmer
        assertEquals(beanManager.getBeans(Programmer.class, new SkillLiteral() {
            @Override
            public String language() {
                return "C++";
            }

            @Override
            public String level() {
                return "guru";
            }
        }).size(), 0);
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertions({@SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "af"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "r")})
    public void testAddAnnotatedType() {
        getUniqueBean(Boss.class);
        assertEquals(getBeans(Bar.class).size(), 0);
        assertEquals(getBeans(Bar.class, new AnnotationLiteral<Pro>() {
        }).size(), 1);
        assertEquals(getBeans(Foo.class).size(), 0);
        assertEquals(getBeans(Foo.class, new AnnotationLiteral<Pro>() {
        }).size(), 1);
    }

}
