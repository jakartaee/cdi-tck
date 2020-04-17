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
package org.jboss.cdi.tck.tests.implementation.producer.method.definition;

import static org.jboss.cdi.tck.cdi.Sections.BEAN_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.BUILTIN_QUALIFIERS;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_BEAN_NAME;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_BEAN_QUALIFIERS;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_PRODUCER_METHOD;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_STEREOTYPES;
import static org.jboss.cdi.tck.cdi.Sections.DEFAULT_NAME;
import static org.jboss.cdi.tck.cdi.Sections.DEFAULT_SCOPE;
import static org.jboss.cdi.tck.cdi.Sections.DISPOSER_METHOD;
import static org.jboss.cdi.tck.cdi.Sections.LEGAL_BEAN_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.MEMBER_LEVEL_INHERITANCE;
import static org.jboss.cdi.tck.cdi.Sections.METHOD_CONSTRUCTOR_PARAMETER_QUALIFIERS;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_METHOD;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_METHOD_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_OR_DISPOSER_METHODS_INVOCATION;
import static org.jboss.cdi.tck.cdi.Sections.SPECIALIZATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.IllegalProductException;
import jakarta.enterprise.inject.UnsatisfiedResolutionException;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.Assert;
import org.jboss.cdi.tck.util.DependentInstance;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SuppressWarnings("serial")
@SpecVersion(spec = "cdi", version = "2.0")
public class ProducerMethodDefinitionTest extends AbstractTest {

    private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {
    };
    private static final Annotation DEADLIEST_LITERAL = new AnnotationLiteral<Deadliest>() {
    };
    private static final Annotation NUMBER_LITERAL = new AnnotationLiteral<Number>() {
    };
    private static final Annotation YUMMY_LITERAL = new AnnotationLiteral<Yummy>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProducerMethodDefinitionTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD, id = "b"), @SpecAssertion(section = PRODUCER_OR_DISPOSER_METHODS_INVOCATION, id = "a") })
    public void testStaticMethod() throws Exception {
        assertEquals(getBeans(String.class, TAME_LITERAL).size(), 1);
        assertEquals(getContextualReference(String.class, TAME_LITERAL), BeanWithStaticProducerMethod.getString());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD, id = "aa") })
    public void testProducerOnNonBean() throws Exception {
        assertTrue(getBeans(Cherry.class).isEmpty());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DISPOSER_METHOD, id = "b") })
    public void testStaticDisposerMethod() throws Exception {
        assertEquals(getBeans(String.class, TAME_LITERAL).size(), 1);
        DependentInstance<String> stringBean = newDependentInstance(String.class, TAME_LITERAL);
        assertTrue(stringBean.get().equals("Pete"));
        stringBean.destroy();
        assertTrue(BeanWithStaticProducerMethod.stringDestroyed);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DISPOSER_METHOD, id = "b") })
    public void testStaticDisposerMethodWithNonStaticProducer() throws Exception {
        assertEquals(getBeans(String.class, NUMBER_LITERAL).size(), 1);
        DependentInstance<String> stringBean = newDependentInstance(String.class, NUMBER_LITERAL);
        assertTrue(stringBean.get().equals("number"));
        stringBean.destroy();
        assertTrue(BeanWithStaticProducerMethod.numberDestroyed);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DISPOSER_METHOD, id = "b") })
    public void testNonStaticDisposerMethodWithStaticProducer() throws Exception {
        assertEquals(getBeans(String.class, YUMMY_LITERAL).size(), 1);
        DependentInstance<String> stringBean = newDependentInstance(String.class, YUMMY_LITERAL);
        assertTrue(stringBean.get().equals("yummy"));
        stringBean.destroy();
        assertTrue(BeanWithStaticProducerMethod.yummyDestroyed);
    }

    @Test
    @SpecAssertion(section = PRODUCER_METHOD, id = "ga")
    public void testParameterizedReturnType() throws Exception {
        assertEquals(getBeans(new TypeLiteral<FunnelWeaver<Spider>>() {
        }).size(), 1);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD, id = "c"), @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "a"),
            @SpecAssertion(section = BUILTIN_QUALIFIERS, id = "aa"), @SpecAssertion(section = BUILTIN_QUALIFIERS, id = "ab") })
    public void testDefaultBindingType() throws Exception {
        assertEquals(getCurrentManager().getBeans(Tarantula.class).size(), 1);
        assertEquals(getCurrentManager().getBeans(Tarantula.class).iterator().next().getQualifiers().size(), 2);
        assertTrue(getCurrentManager().getBeans(Tarantula.class).iterator().next().getQualifiers().contains(Default.Literal.INSTANCE));
        assertTrue(getCurrentManager().getBeans(Tarantula.class).iterator().next().getQualifiers().contains(Any.Literal.INSTANCE));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD_TYPES, id = "c"), @SpecAssertion(section = BEAN_TYPES, id = "l") })
    public void testApiTypeForClassReturn() throws Exception {
        assertEquals(getBeans(Tarantula.class).size(), 1);
        Bean<Tarantula> tarantula = getBeans(Tarantula.class).iterator().next();

        assertEquals(tarantula.getTypes().size(), 6);
        assertTrue(tarantula.getTypes().contains(Tarantula.class));
        assertTrue(tarantula.getTypes().contains(DeadlySpider.class));
        assertTrue(tarantula.getTypes().contains(Spider.class));
        assertTrue(tarantula.getTypes().contains(Animal.class));
        assertTrue(tarantula.getTypes().contains(DeadlyAnimal.class));
        assertTrue(tarantula.getTypes().contains(Object.class));
    }

    @Test
    @SpecAssertion(section = PRODUCER_METHOD_TYPES, id = "a")
    public void testApiTypeForInterfaceReturn() throws Exception {
        assertEquals(getBeans(Bite.class).size(), 1);
        Bean<Bite> animal = getBeans(Bite.class).iterator().next();
        assertEquals(animal.getTypes().size(), 2);
        assertTrue(animal.getTypes().contains(Bite.class));
        assertTrue(animal.getTypes().contains(Object.class));
    }

    @Test
    @SpecAssertion(section = PRODUCER_METHOD_TYPES, id = "ba")
    public void testApiTypeForPrimitiveReturn() throws Exception {
        Set<Bean<Integer>> beans = getBeans(Integer.class, NUMBER_LITERAL);
        assertEquals(beans.size(), 1);
        Bean<Integer> bean = beans.iterator().next();
        Assert.assertTypeSetMatches(bean.getTypes(), Object.class, int.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD_TYPES, id = "bb"), @SpecAssertion(section = LEGAL_BEAN_TYPES, id = "i") })
    public void testApiTypeForArrayTypeReturn() throws Exception {
        assertEquals(getBeans(Spider[].class).size(), 1);
        Bean<Spider[]> spiders = getBeans(Spider[].class).iterator().next();
        assertEquals(spiders.getTypes().size(), 2);
        assertTrue(spiders.getTypes().contains(Spider[].class));
        assertTrue(spiders.getTypes().contains(Object.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "be"), @SpecAssertion(section = PRODUCER_METHOD, id = "k"),
            @SpecAssertion(section = DECLARING_BEAN_QUALIFIERS, id = "b") })
    public void testBindingType() throws Exception {
        assertEquals(getBeans(Tarantula.class, TAME_LITERAL).size(), 1);
        Bean<Tarantula> tarantula = getBeans(Tarantula.class, TAME_LITERAL).iterator().next();
        assertEquals(tarantula.getQualifiers().size(), 2);
        assertTrue(tarantula.getQualifiers().contains(TAME_LITERAL));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "ba"), @SpecAssertion(section = PRODUCER_METHOD, id = "k") })
    public void testScopeType() throws Exception {
        assertEquals(getBeans(DaddyLongLegs.class, TAME_LITERAL).size(), 1);
        Bean<DaddyLongLegs> daddyLongLegs = getBeans(DaddyLongLegs.class, TAME_LITERAL).iterator().next();
        assertEquals(daddyLongLegs.getScope(), RequestScoped.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "bb"), @SpecAssertion(section = DECLARING_BEAN_NAME, id = "b") })
    public void testNamedMethod() throws Exception {
        assertEquals(getBeans(BlackWidow.class, TAME_LITERAL).size(), 1);
        Bean<BlackWidow> blackWidowSpider = getBeans(BlackWidow.class, TAME_LITERAL).iterator().next();
        assertEquals(blackWidowSpider.getName(), "blackWidow");
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "bb"), @SpecAssertion(section = DEFAULT_NAME, id = "b"),
            @SpecAssertion(section = DEFAULT_NAME, id = "fb") })
    public void testDefaultNamedMethod() throws Exception {
        String name = "produceDaddyLongLegs";
        Bean<DaddyLongLegs> daddyLongLegs = getUniqueBean(DaddyLongLegs.class, TAME_LITERAL);
        assertEquals(daddyLongLegs.getName(), name);
        // Any, Tame, Named
        assertTrue(annotationSetMatches(daddyLongLegs.getQualifiers(), Any.Literal.INSTANCE, TAME_LITERAL,
                NamedLiteral.of(name)));
    }

    // Review 2.2
    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_STEREOTYPES, id = "b"), @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "ba"),
            @SpecAssertion(section = DEFAULT_SCOPE, id = "c"), @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "bd") })
    public void testStereotypeSpecifiesScope() throws Exception {
        assertEquals(getBeans(WolfSpider.class, TAME_LITERAL).size(), 1);
        Bean<WolfSpider> wolfSpider = getBeans(WolfSpider.class, TAME_LITERAL).iterator().next();
        assertEquals(wolfSpider.getScope(), RequestScoped.class);
    }

    @Test(expectedExceptions = UnsatisfiedResolutionException.class)
    @SpecAssertions({ @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "da"), @SpecAssertion(section = SPECIALIZATION, id = "cb") })
    public void testNonStaticProducerMethodNotInheritedBySpecializingSubclass() {
        assertEquals(getBeans(Egg.class, new AnnotationLiteral<Yummy>() {
        }).size(), 0);
        getContextualReference(Egg.class, new AnnotationLiteral<Yummy>() {
        });
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "da"), @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "dg") })
    public void testNonStaticProducerMethodNotInherited() {
        assertEquals(getBeans(Apple.class, new AnnotationLiteral<Yummy>() {
        }).size(), 1);
        assertEquals(getContextualReference(Apple.class, new AnnotationLiteral<Yummy>() {
        }).getTree().getClass(), AppleTree.class);
    }

    /**
     * Note on the "3.3.2 h" assertion related to CDI section "3.3.2. Declaring a producer method" statement
     * "A producer method may have any number of parameters." JVM spec allows max 255 params. This test works with two producer
     * method params - {@link SpiderProducer#producesDeadliestTarantula(Tarantula, Tarantula)}. To fulfill the assertion
     * requirements we would need to test 255 producer methods with 1 to 255 parameter injection points.
     */
    @Test
    @SpecAssertions({ @SpecAssertion(section = METHOD_CONSTRUCTOR_PARAMETER_QUALIFIERS, id = "a"),
            @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "i"),
            @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "h"), @SpecAssertion(section = PRODUCER_OR_DISPOSER_METHODS_INVOCATION, id = "e") })
    public void testBindingTypesAppliedToProducerMethodParameters() {
        Bean<Tarantula> tarantula = getBeans(Tarantula.class, DEADLIEST_LITERAL).iterator().next();
        CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantula);
        Tarantula instance = tarantula.create(creationalContext);
        assertEquals(instance.getDeathsCaused(), 1);
    }

    @Test
    @SpecAssertion(section = PRODUCER_METHOD, id = "e")
    public void testDependentProducerReturnsNullValue() {
        assertEquals(getContextualReference(Acorn.class), null);
    }

    @Test(expectedExceptions = { IllegalProductException.class })
    @SpecAssertion(section = PRODUCER_METHOD, id = "f")
    public void testNonDependentProducerReturnsNullValue() {
        getContextualReference(Pollen.class, new AnnotationLiteral<Yummy>() {
        }).ping();
        fail("IllegalProductException not thrown");
    }

    @Test
    @SpecAssertion(section = PRODUCER_METHOD, id = "iaa")
    public void testTypeVariableReturnType() {
        // should be created by SpiderListProducer
        assertEquals(getBeans(new TypeLiteral<List<Spider>>() {
        }).size(), 1);
    }
}
