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
package org.jboss.cdi.tck.tests.definition.name;

import static org.jboss.cdi.tck.cdi.Sections.BEANS_WITH_NO_NAME;
import static org.jboss.cdi.tck.cdi.Sections.CONCEPTS;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_BEAN_NAME;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_MANAGED_BEAN;
import static org.jboss.cdi.tck.cdi.Sections.DEFAULT_NAME;
import static org.jboss.cdi.tck.cdi.Sections.MANAGED_BEAN_NAME;
import static org.jboss.cdi.tck.cdi.Sections.NAMED_STEREOTYPE;
import static org.jboss.cdi.tck.cdi.Sections.STEREOTYPES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.literal.NamedLiteral;
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
public class NameDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(NameDefinitionTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_BEAN_NAME, id = "a"), @SpecAssertion(section = CONCEPTS, id = "e"),
            @SpecAssertion(section = DECLARING_MANAGED_BEAN, id = "bb") })
    public void testNonDefaultNamed() {
        Bean<Moose> moose = getUniqueBean(Moose.class);
        assertEquals(moose.getName(), "aMoose");
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DEFAULT_NAME, id = "a"), @SpecAssertion(section = MANAGED_BEAN_NAME, id = "a"),
            @SpecAssertion(section = DEFAULT_NAME, id = "fa") })
    public void testDefaultNamed() {
        String name = "haddock";
        Bean<Haddock> haddock = getUniqueBean(Haddock.class);
        assertEquals(haddock.getName(), name);
        assertTrue(annotationSetMatches(haddock.getQualifiers(), Any.Literal.INSTANCE, Default.Literal.INSTANCE,
                NamedLiteral.of(name)));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = STEREOTYPES, id = "a"), @SpecAssertion(section = NAMED_STEREOTYPE, id = "aa") })
    public void testStereotypeDefaultsName() {
        Bean<RedSnapper> bean = getUniqueBean(RedSnapper.class);
        assertEquals(bean.getName(), "redSnapper");
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BEANS_WITH_NO_NAME, id = "a"), @SpecAssertion(section = CONCEPTS, id = "e") })
    public void testNamedNotDeclaredByBean() {
        Bean<SeaBass> bean = getUniqueBean(SeaBass.class);
        assertNull(bean.getName());
    }

    @Test
    @SpecAssertion(section = BEANS_WITH_NO_NAME, id = "a")
    public void testNamedNotDeclaredByStereotype() {
        Bean<Minnow> bean = getUniqueBean(Minnow.class);
        assertNull(bean.getName());
    }

    @Test
    @SpecAssertion(section = MANAGED_BEAN_NAME, id = "a")
    public void testNameStartingWithMultipleUpperCaseCharacters() {
        Bean<JSFBean> bean = getUniqueBean(JSFBean.class);
        assertEquals(bean.getName(), "jSFBean");
    }

    @Test
    @SpecAssertion(section = MANAGED_BEAN_NAME, id = "a")
    public void testNameStartingWithUnderScoreCharacter() {
        Bean<_Underscore> bean = getUniqueBean(_Underscore.class);
        assertEquals(bean.getName(), "_Underscore");
    }

    @Test
    @SpecAssertion(section = MANAGED_BEAN_NAME, id = "a")
    public void testNameStartingWithDollarCharacter() {
        Bean<$Dollar> bean = getUniqueBean($Dollar.class);
        assertEquals(bean.getName(), "$Dollar");
    }

    @Test
    @SpecAssertion(section = MANAGED_BEAN_NAME, id = "a")
    public void testNameStartingWithLowerCaseCharacter() {
        Bean<lowerCase> bean = getUniqueBean(lowerCase.class);
        assertEquals(bean.getName(), "lowerCase");
    }

}
