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
package org.jboss.jsr299.tck.tests.definition.stereotype;

import java.lang.annotation.Annotation;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class StereotypeDefinitionTest extends AbstractJSR299Test {
    private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(StereotypeDefinitionTest.class).withBeansXml("beans.xml").build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.7.1.1", id = "aa"), @SpecAssertion(section = "2.4.3", id = "c") })
    public void testStereotypeWithScopeType() {
        assert getBeans(Moose.class).size() == 1;
        assert getBeans(Moose.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.7.1.1", id = "aa"), @SpecAssertion(section = "2.4.4", id = "b") })
    public void testStereotypeWithoutScopeType() {
        assert getBeans(Reindeer.class).size() == 1;
        assert getBeans(Reindeer.class).iterator().next().getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.7", id = "c"), @SpecAssertion(section = "2.7.1", id = "b") })
    public void testOneStereotypeAllowed() {
        Bean<LongHairedDog> bean = getBeans(LongHairedDog.class).iterator().next();
        assert bean.getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.7.2", id = "e"), @SpecAssertion(section = "2.7", id = "d") })
    public void testMultipleStereotypesAllowed() {
        assert getBeans(HighlandCow.class, TAME_LITERAL).size() == 1;
        Bean<HighlandCow> highlandCow = getBeans(HighlandCow.class, TAME_LITERAL).iterator().next();
        assert highlandCow.getName() == null;
        assert highlandCow.getQualifiers().contains(TAME_LITERAL);
        assert highlandCow.getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.7.2", id = "e"), @SpecAssertion(section = "2.4.4", id = "e") })
    public void testExplicitScopeOverridesMergedScopesFromMultipleStereotype() {
        assert getBeans(Springbok.class).size() == 1;
        assert getBeans(Springbok.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = "4.1", id = "ab")
    public void testStereotypeDeclaredInheritedIsInherited() throws Exception {
        assert getBeans(BorderCollie.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = "4.1", id = "aba")
    public void testStereotypeNotDeclaredInheritedIsNotInherited() {
        assert getBeans(ShetlandPony.class).size() == 1;
        assert getBeans(ShetlandPony.class).iterator().next().getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertion(section = "4.1", id = "ah")
    public void testStereotypeDeclaredInheritedIsIndirectlyInherited() {
        assert getBeans(EnglishBorderCollie.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = "4.1", id = "aha")
    public void testStereotypeNotDeclaredInheritedIsNotIndirectlyInherited() {
        assert getBeans(MiniatureClydesdale.class).size() == 1;
        assert getBeans(MiniatureClydesdale.class).iterator().next().getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertion(section = "4.1", id = "hhh")
    public void testStereotypeScopeIsOverriddenByInheritedScope() {
        assert getBeans(Chihuahua.class).iterator().next().getScope().equals(SessionScoped.class);
    }

    @Test
    @SpecAssertion(section = "4.1", id = "hhi")
    public void testStereotypeScopeIsOverriddenByIndirectlyInheritedScope() {
        assert getBeans(MexicanChihuahua.class).iterator().next().getScope().equals(SessionScoped.class);
    }

}
