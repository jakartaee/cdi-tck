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
package org.jboss.cdi.tck.tests.definition.stereotype;

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_BEAN_SCOPE;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_STEREOTYPES;
import static org.jboss.cdi.tck.cdi.Sections.DEFAULT_SCOPE;
import static org.jboss.cdi.tck.cdi.Sections.DEFINING_NEW_STEREOTYPE;
import static org.jboss.cdi.tck.cdi.Sections.SPECIFY_STEREOTYPE_INTERCEPTOR_BINDINGS;
import static org.jboss.cdi.tck.cdi.Sections.STEREOTYPES;
import static org.jboss.cdi.tck.cdi.Sections.STEREOTYPE_DEFAULT_SCOPE;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_LEVEL_INHERITANCE;

import java.lang.annotation.Annotation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class StereotypeDefinitionTest extends AbstractTest {
    private static final Annotation TAME_LITERAL = new Tame.Literal();

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(StereotypeDefinitionTest.class).withBeansXml("beans.xml").build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = STEREOTYPE_DEFAULT_SCOPE, id = "aa"),
            @SpecAssertion(section = DECLARING_BEAN_SCOPE, id = "c") })
    public void testStereotypeWithScopeType() {
        assert getBeans(Moose.class).size() == 1;
        assert getBeans(Moose.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = STEREOTYPE_DEFAULT_SCOPE, id = "aa"),
            @SpecAssertion(section = DEFAULT_SCOPE, id = "b") })
    public void testStereotypeWithoutScopeType() {
        assert getBeans(Reindeer.class).size() == 1;
        assert getBeans(Reindeer.class).iterator().next().getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = STEREOTYPES, id = "c"),
            @SpecAssertion(section = DEFINING_NEW_STEREOTYPE, id = "b"),
            @SpecAssertion(section = SPECIFY_STEREOTYPE_INTERCEPTOR_BINDINGS, id = "a") })
    public void testOneStereotypeAllowed() {
        Bean<LongHairedDog> bean = getBeans(LongHairedDog.class).iterator().next();
        assert bean.getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_STEREOTYPES, id = "e"),
            @SpecAssertion(section = STEREOTYPES, id = "d") })
    public void testMultipleStereotypesAllowed() {
        assert getBeans(HighlandCow.class, TAME_LITERAL).size() == 1;
        Bean<HighlandCow> highlandCow = getBeans(HighlandCow.class, TAME_LITERAL).iterator().next();
        assert highlandCow.getName() == null;
        assert highlandCow.getQualifiers().contains(TAME_LITERAL);
        assert highlandCow.getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_STEREOTYPES, id = "e"),
            @SpecAssertion(section = DEFAULT_SCOPE, id = "e") })
    public void testExplicitScopeOverridesMergedScopesFromMultipleStereotype() {
        assert getBeans(Springbok.class).size() == 1;
        assert getBeans(Springbok.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "ab")
    public void testStereotypeDeclaredInheritedIsInherited() throws Exception {
        assert getBeans(BorderCollie.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "aba")
    public void testStereotypeNotDeclaredInheritedIsNotInherited() {
        assert getBeans(ShetlandPony.class).size() == 1;
        assert getBeans(ShetlandPony.class).iterator().next().getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "ah")
    public void testStereotypeDeclaredInheritedIsIndirectlyInherited() {
        assert getBeans(EnglishBorderCollie.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "aha")
    public void testStereotypeNotDeclaredInheritedIsNotIndirectlyInherited() {
        assert getBeans(MiniatureClydesdale.class).size() == 1;
        assert getBeans(MiniatureClydesdale.class).iterator().next().getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "hhh")
    public void testStereotypeScopeIsOverriddenByInheritedScope() {
        assert getBeans(Chihuahua.class).iterator().next().getScope().equals(ApplicationScoped.class);
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "hhi")
    public void testStereotypeScopeIsOverriddenByIndirectlyInheritedScope() {
        assert getBeans(MexicanChihuahua.class).iterator().next().getScope().equals(ApplicationScoped.class);
    }

}
