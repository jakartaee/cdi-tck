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
package org.jboss.cdi.tck.tests.implementation.producer.field.definition;

import static org.jboss.cdi.tck.cdi.Sections.BEANS_WITH_NO_NAME;
import static org.jboss.cdi.tck.cdi.Sections.BUILTIN_QUALIFIERS;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_BEAN_NAME;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_BEAN_QUALIFIERS;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_PRODUCER_FIELD;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_STEREOTYPES;
import static org.jboss.cdi.tck.cdi.Sections.DEFAULT_NAME;
import static org.jboss.cdi.tck.cdi.Sections.LEGAL_BEAN_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.MEMBER_LEVEL_INHERITANCE;
import static org.jboss.cdi.tck.cdi.Sections.NAMED_STEREOTYPE;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_FIELD;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_FIELD_NAME;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_FIELD_TYPES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.literals.DefaultLiteral;
import org.jboss.cdi.tck.literals.NamedLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SuppressWarnings("serial")
@SpecVersion(spec = "cdi", version = "20091101")
public class ProducerFieldDefinitionTest extends AbstractTest {

    private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {
    };
    private static final Annotation PET_LITERAL = new AnnotationLiteral<Pet>() {
    };
    private static final Annotation FOO_LITERAL = new AnnotationLiteral<Foo>() {
    };
    private static final Annotation STATIC_LITERAL = new AnnotationLiteral<Static>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProducerFieldDefinitionTest.class).build();
    }

    @Test
    @SpecAssertion(section = PRODUCER_FIELD, id = "fa")
    public void testParameterizedReturnType() throws Exception {
        FunnelWeaverSpiderConsumer spiderConsumer = getContextualReference(FunnelWeaverSpiderConsumer.class);
        assert spiderConsumer != null;
        assert spiderConsumer.getInjectedSpider() != null;
        assert spiderConsumer.getInjectedSpider().equals(FunnelWeaverSpiderProducer.getSpider());
    }

   @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_FIELD, id = "j"), @SpecAssertion(section = PRODUCER_FIELD, id = "c"),
            @SpecAssertion(section = DECLARING_PRODUCER_FIELD, id = "a") })
    public void testBeanDeclaresMultipleProducerFields() {
        assert getBeans(Tarantula.class, TAME_LITERAL).size() == 1;
        assert getContextualReference(WolfSpider.class, PET_LITERAL).equals(OtherSpiderProducer.WOLF_SPIDER);
        assert getBeans(BlackWidow.class, TAME_LITERAL).size() == 1;
        assert getContextualReference(BlackWidow.class, TAME_LITERAL).equals(OtherSpiderProducer.BLACK_WIDOW);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BUILTIN_QUALIFIERS, id = "aa") })
    public void testDefaultBindingType() {
        Set<Bean<Tarantula>> tarantulaBeans = getBeans(Tarantula.class);
        assert tarantulaBeans.size() == 2;
        assert tarantulaBeans.iterator().next().getQualifiers().contains(new DefaultLiteral());
    }

    @Test
    @SpecAssertion(section = PRODUCER_FIELD_TYPES, id = "c")
    public void testApiTypeForClassReturn() {
        Set<Bean<Tarantula>> tarantulaBeans = getBeans(Tarantula.class, PET_LITERAL);
        assert tarantulaBeans.size() == 1;
        Bean<Tarantula> tarantulaBean = tarantulaBeans.iterator().next();
        assert tarantulaBean.getTypes().size() == 6;
        assert tarantulaBean.getTypes().contains(Tarantula.class);
        assert tarantulaBean.getTypes().contains(DeadlySpider.class);
        assert tarantulaBean.getTypes().contains(Spider.class);
        assert tarantulaBean.getTypes().contains(Animal.class);
        assert tarantulaBean.getTypes().contains(DeadlyAnimal.class);
        assert tarantulaBean.getTypes().contains(Object.class);
    }

   @Test
    @SpecAssertion(section = PRODUCER_FIELD_TYPES, id = "a")
    public void testApiTypeForInterfaceReturn() {
        Set<Bean<Animal>> animalBeans = getBeans(Animal.class, new AnnotationLiteral<AsAnimal>() {
        });
        assert animalBeans.size() == 1;
        Bean<Animal> animalModel = animalBeans.iterator().next();
        assert animalModel.getTypes().size() == 2;
        assert animalModel.getTypes().contains(Animal.class);
        assert animalModel.getTypes().contains(Object.class);
    }

   @Test
    @SpecAssertion(section = PRODUCER_FIELD_TYPES, id = "ba")
    public void testApiTypeForPrimitiveReturn() {
        Set<Bean<?>> beans = getCurrentManager().getBeans("SpiderSize");
        assert beans.size() == 1;
        Bean<?> intModel = beans.iterator().next();
        assert intModel.getTypes().size() == 2;
        assert intModel.getTypes().contains(int.class);
        assert intModel.getTypes().contains(Object.class);
    }

   @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_FIELD_TYPES, id = "bb"), @SpecAssertion(section = LEGAL_BEAN_TYPES, id = "i") })
    public void testApiTypeForArrayTypeReturn() {
        Set<Bean<Spider[]>> spidersBeans = getBeans(Spider[].class);
        assert spidersBeans.size() == 1;
        Bean<Spider[]> spidersModel = spidersBeans.iterator().next();
        assert spidersModel.getTypes().size() == 2;
        assert spidersModel.getTypes().contains(Spider[].class);
        assert spidersModel.getTypes().contains(Object.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_PRODUCER_FIELD, id = "f"), @SpecAssertion(section = DECLARING_BEAN_QUALIFIERS, id = "c") })
    public void testBindingType() {
        Set<Bean<Tarantula>> tarantulaBeans = getBeans(Tarantula.class, TAME_LITERAL);
        assert tarantulaBeans.size() == 1;
        Bean<Tarantula> tarantulaModel = tarantulaBeans.iterator().next();
        assert tarantulaModel.getQualifiers().size() == 3;
        assert tarantulaModel.getQualifiers().contains(TAME_LITERAL);
    }

    @Test
    @SpecAssertion(section = DECLARING_PRODUCER_FIELD, id = "b")
    public void testScopeType() {
        Set<Bean<Tarantula>> tarantulaBeans = getBeans(Tarantula.class, TAME_LITERAL, FOO_LITERAL);
        assert !tarantulaBeans.isEmpty();
        Bean<Tarantula> tarantulaModel = tarantulaBeans.iterator().next();
        assert tarantulaModel.getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_PRODUCER_FIELD, id = "c"), @SpecAssertion(section = DECLARING_BEAN_NAME, id = "c") })
    public void testNamedField() {
        Set<Bean<?>> beans = getCurrentManager().getBeans("blackWidow");
        assert beans.size() == 1;

        @SuppressWarnings("unchecked")
        Bean<BlackWidow> blackWidowModel = (Bean<BlackWidow>) beans.iterator().next();
        assert blackWidowModel.getName().equals("blackWidow");
    }

   @Test
    @SpecAssertions({ @SpecAssertion(section = DEFAULT_NAME, id = "c"), @SpecAssertion(section = NAMED_STEREOTYPE, id = "aa"),
            @SpecAssertion(section = NAMED_STEREOTYPE, id = "ab"), @SpecAssertion(section = BEANS_WITH_NO_NAME, id = "a"),
            @SpecAssertion(section = PRODUCER_FIELD_NAME, id = "a"), @SpecAssertion(section = DECLARING_BEAN_NAME, id = "d") })
    public void testDefaultNamedByStereotype() {
        Bean<Tarantula> staticTarantulaBean = getUniqueBean(Tarantula.class, STATIC_LITERAL);
        assertEquals(staticTarantulaBean.getName(), "produceTarantula");
        // Any, Static
        assertTrue(annotationSetMatches(staticTarantulaBean.getQualifiers(), AnyLiteral.INSTANCE, STATIC_LITERAL));

    }

   @Test
    @SpecAssertions({ @SpecAssertion(section = DEFAULT_NAME, id = "fc") })
    public void testDefaultNamed() {
        Bean<Tarantula> tarantulaBean = getUniqueBean(Tarantula.class, PET_LITERAL);
        assertEquals(tarantulaBean.getName(), "producedPetTarantula");
        // Any, Pet, Named
        assertTrue(annotationSetMatches(tarantulaBean.getQualifiers(), AnyLiteral.INSTANCE, PET_LITERAL, new NamedLiteral(
                "producedPetTarantula")));
    }

    // review 2.2
    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_STEREOTYPES, id = "c"), @SpecAssertion(section = DECLARING_PRODUCER_FIELD, id = "e") })
    public void testStereotype() {
        Set<Bean<Tarantula>> tarantulaBeans = getBeans(Tarantula.class, STATIC_LITERAL);
        assert !tarantulaBeans.isEmpty();
        Bean<Tarantula> tarantulaModel = tarantulaBeans.iterator().next();
        assert tarantulaModel.getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "ea")
    public void testNonStaticProducerFieldNotInherited() {
        assert !(getContextualReference(Egg.class, FOO_LITERAL).getMother() instanceof InfertileChicken);
    }

    @Test
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "ec")
    public void testNonStaticProducerFieldNotIndirectlyInherited() {
        assert !(getContextualReference(Egg.class, FOO_LITERAL).getMother() instanceof LameInfertileChicken);
    }

    @Test
    @SpecAssertion(section = PRODUCER_FIELD, id = "fb")
    public void testProducerFieldWithTypeVariable() {
        assertNotNull(getContextualReference(new TypeLiteral<List<Spider>>() {
        }));
    }
}
