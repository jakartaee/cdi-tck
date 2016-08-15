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
package org.jboss.cdi.tck.tests.definition.scope;

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_BEAN_SCOPE;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_STEREOTYPES;
import static org.jboss.cdi.tck.cdi.Sections.DEFAULT_SCOPE;
import static org.jboss.cdi.tck.cdi.Sections.DEFINING_NEW_SCOPE_TYPE;
import static org.jboss.cdi.tck.cdi.Sections.SCOPES;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_LEVEL_INHERITANCE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.NormalScope;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class ScopeDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ScopeDefinitionTest.class).build();
    }

    @Test
    @SpecAssertion(section = SCOPES, id = "c")
    public void testScopeTypesAreExtensible() {
        assert getBeans(Mullet.class).size() == 1;
        Bean<Mullet> bean = getBeans(Mullet.class).iterator().next();
        assert bean.getScope().equals(AnotherScopeType.class);
    }

    @Test
    @SpecAssertion(section = DEFINING_NEW_SCOPE_TYPE, id = "aa")
    public void testScopeTypeHasCorrectTarget() {
        assert getBeans(Mullet.class).size() == 1;
        Bean<Mullet> bean = getBeans(Mullet.class).iterator().next();
        Target target = bean.getScope().getAnnotation(Target.class);
        List<ElementType> elements = Arrays.asList(target.value());
        assert elements.contains(ElementType.TYPE);
        assert elements.contains(ElementType.METHOD);
        assert elements.contains(ElementType.FIELD);
    }

    @Test
    @SpecAssertion(section = DEFINING_NEW_SCOPE_TYPE, id = "ba")
    public void testScopeTypeDeclaresScopeTypeAnnotation() {
        assert getBeans(Mullet.class).size() == 1;
        Bean<Mullet> bean = getBeans(Mullet.class).iterator().next();
        assert bean.getScope().getAnnotation(NormalScope.class) != null;
    }

    @Test
    @SpecAssertion(section = DECLARING_BEAN_SCOPE, id = "a")
    public void testScopeDeclaredInJava() {
        assert getBeans(SeaBass.class).size() == 1;
        Bean<SeaBass> bean = getBeans(SeaBass.class).iterator().next();
        assert bean.getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = DEFAULT_SCOPE, id = "aa")
    public void testDefaultScope() {
        assert getBeans(Order.class).size() == 1;
        Bean<Order> bean = getBeans(Order.class).iterator().next();
        assert bean.getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DEFAULT_SCOPE, id = "e"), @SpecAssertion(section = DECLARING_STEREOTYPES, id = "a") })
    public void testScopeSpecifiedAndStereotyped() {
        assert getBeans(Minnow.class).size() == 1;
        Bean<Minnow> bean = getBeans(Minnow.class).iterator().next();
        assert bean.getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = DEFAULT_SCOPE, id = "da")
    public void testMultipleIncompatibleScopeStereotypesWithScopeSpecified() {
        assert getBeans(Pollock.class).size() == 1;
        Bean<Pollock> bean = getBeans(Pollock.class).iterator().next();
        assert bean.getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertion(section = DEFAULT_SCOPE, id = "c")
    public void testMultipleCompatibleScopeStereotypes() {
        assert getBeans(Grayling.class).size() == 1;
        Bean<Grayling> bean = getBeans(Grayling.class).iterator().next();
        assert bean.getScope().equals(ApplicationScoped.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_STEREOTYPES, id = "db"), @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "ab") })
    public void testWebBeanScopeTypeOverridesStereotype() {
        assert getBeans(RedSnapper.class).size() == 1;
        Bean<RedSnapper> bean = getBeans(RedSnapper.class).iterator().next();
        assert bean.getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "ba")
    public void testScopeTypeDeclaredInheritedIsInherited() throws Exception {
        assert getBeans(BorderCollie.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "baa")
    public void testScopeTypeNotDeclaredInheritedIsNotInherited() {
        assert getBeans(ShetlandPony.class).size() == 1;
        assert getBeans(ShetlandPony.class).iterator().next().getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "ba")
    public void testScopeTypeDeclaredInheritedIsBlockedByIntermediateScopeTypeMarkedInherited() {
        assert getBeans(GoldenRetriever.class).size() == 1;
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "ba")
    public void testScopeTypeDeclaredInheritedIsBlockedByIntermediateScopeTypeNotMarkedInherited() {
        assert getBeans(GoldenLabrador.class).size() == 1;
        assert getBeans(GoldenLabrador.class).iterator().next().getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "bc")
    public void testScopeTypeDeclaredInheritedIsIndirectlyInherited() {
        assert getBeans(EnglishBorderCollie.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "bca")
    public void testScopeTypeNotDeclaredInheritedIsNotIndirectlyInherited() {
        assert getBeans(MiniatureClydesdale.class).size() == 1;
        assert getBeans(MiniatureClydesdale.class).iterator().next().getScope().equals(Dependent.class);
    }

}
