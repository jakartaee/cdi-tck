/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle.bbd;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BEFORE_BEAN_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.INITIALIZATION;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_DISCOVERY_STEPS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.bbd.lib.Bar;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.bbd.lib.Baz;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.bbd.lib.Boss;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.bbd.lib.Foo;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.bbd.lib.Pro;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author pmuir
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class BeforeBeanDiscoveryTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeforeBeanDiscoveryTest.class)
                .withExtension(BeforeBeanDiscoveryObserver.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withLibrary(Boss.class, Foo.class, Bar.class, Baz.class, Pro.class)
                .build();
    }

    @Test
    @SpecAssertions({
            @SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "a"),
            @SpecAssertion(section = INITIALIZATION, id = "b"),
            @SpecAssertion(section = INITIALIZATION, id = "c") })
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
        assertEquals(getBeans(Alligator.class, new Tame.Literal()).size(), 1);
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
    @SpecAssertions({ @SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "af"),
            @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "d") })
    public void testAddAnnotatedType() {
        getUniqueBean(Boss.class);
        assertEquals(getBeans(Bar.class).size(), 0);
        assertEquals(getBeans(Bar.class, Pro.ProLiteral.INSTANCE).size(), 1);
        assertEquals(getBeans(Foo.class).size(), 0);
        assertEquals(getBeans(Foo.class, Pro.ProLiteral.INSTANCE).size(), 1);
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertions({ @SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "b") })
    public void testAddAnnotatedTypeWithConfigurator() {
        Bean<Baz> bazBean = getUniqueBean(Baz.class, Pro.ProLiteral.INSTANCE);
        assertEquals(bazBean.getScope(), RequestScoped.class);
        Baz baz = (Baz) getCurrentManager().getReference(bazBean, Baz.class,
                getCurrentManager().createCreationalContext(bazBean));
        assertFalse(baz.getBarInstance().isUnsatisfied());
    }

}
