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
package org.jboss.cdi.tck.tests.definition.qualifier;

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_BEAN_QUALIFIERS;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_MANAGED_BEAN;
import static org.jboss.cdi.tck.cdi.Sections.DEFINING_QUALIFIER_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.METHOD_CONSTRUCTOR_PARAMETER_QUALIFIERS;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_LEVEL_INHERITANCE;
import static org.testng.Assert.assertFalse;

import java.lang.annotation.Annotation;
import java.util.Set;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
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
public class QualifierDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(QualifierDefinitionTest.class).build();
    }

    @Test
    @SpecAssertion(section = DEFINING_QUALIFIER_TYPES, id = "ba")
    public void testQualifierDeclaresBindingAnnotation() {
        assertFalse(getBeans(Tarantula.class, new TameLiteral()).isEmpty());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_BEAN_QUALIFIERS, id = "a"),
            @SpecAssertion(section = DECLARING_MANAGED_BEAN, id = "be") })
    public void testQualifiersDeclaredInJava() {
        Bean<Cat> cat = getBeans(Cat.class, new SynchronousQualifier()).iterator().next();
        assert cat.getQualifiers().size() == 2;
        assert cat.getQualifiers().contains(new SynchronousQualifier());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_BEAN_QUALIFIERS, id = "d") })
    public void testMultipleQualifiers() {
        Bean<?> model = getBeans(Cod.class, new ChunkyQualifier(true), new WhitefishQualifier()).iterator().next();
        assert model.getQualifiers().size() == 4;
    }

    @SpecAssertion(section = METHOD_CONSTRUCTOR_PARAMETER_QUALIFIERS, id = "a")
    public void testFieldInjectedFromProducerMethod() throws Exception {
        Bean<Barn> barnBean = getBeans(Barn.class).iterator().next();
        Barn barn = barnBean.create(getCurrentManager().createCreationalContext(barnBean));
        assert barn.petSpider != null;
        assert barn.petSpider instanceof DefangedTarantula;
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "aa")
    public void testQualifierDeclaredInheritedIsInherited() throws Exception {
        Set<? extends Annotation> bindings = getBeans(BorderCollie.class, new HairyQualifier(false)).iterator().next()
                .getQualifiers();
        assert bindings.size() == 2;
        assert bindings.contains(new HairyQualifier(false));
        assert bindings.contains(Any.Literal.INSTANCE);
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "aaa")
    public void testQualifierNotDeclaredInheritedIsNotInherited() {
        Set<? extends Annotation> bindings = getBeans(ShetlandPony.class).iterator().next().getQualifiers();
        assert bindings.size() == 2;
        assert bindings.contains(Default.Literal.INSTANCE);
        assert bindings.contains(Any.Literal.INSTANCE);
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "aa")
    public void testQualifierDeclaredInheritedIsBlockedByIntermediateClass() {
        Set<? extends Annotation> bindings = getBeans(ClippedBorderCollie.class, new HairyQualifier(true)).iterator().next()
                .getQualifiers();
        assert bindings.size() == 2;
        Annotation hairyLiteral = new HairyQualifier(true);
        assert bindings.contains(hairyLiteral);
        assert bindings.contains(Any.Literal.INSTANCE);
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "ag")
    public void testQualifierDeclaredInheritedIsIndirectlyInherited() {
        Set<? extends Annotation> bindings = getBeans(EnglishBorderCollie.class, new HairyQualifier(false)).iterator().next()
                .getQualifiers();
        assert bindings.size() == 2;
        assert bindings.contains(new HairyQualifier(false));
    }

    @Test
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "aga")
    public void testQualifierNotDeclaredInheritedIsNotIndirectlyInherited() {
        Set<? extends Annotation> bindings = getBeans(MiniatureShetlandPony.class).iterator().next().getQualifiers();
        assert bindings.size() == 2;
        assert bindings.contains(Default.Literal.INSTANCE);
        assert bindings.contains(Any.Literal.INSTANCE);
    }

}
