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

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.IllegalProductException;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.literals.DefaultLiteral;
import org.jboss.cdi.tck.literals.NamedLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.Assert;
import org.jboss.cdi.tck.util.DependentInstance;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SuppressWarnings("serial")
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class ProducerMethodDefinitionTest extends AbstractTest {

    private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {
    };
    private static final Annotation DEADLIEST_LITERAL = new AnnotationLiteral<Deadliest>() {
    };
    private static final Annotation NUMBER_LITERAL = new AnnotationLiteral<Number>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProducerMethodDefinitionTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD, id = "b"), @SpecAssertion(section = PRODUCER_OR_DISPOSER_METHODS_INVOCATION, id = "a") })
    public void testStaticMethod() throws Exception {
        assert getBeans(String.class, TAME_LITERAL).size() == 1;
        assert getContextualReference(String.class, TAME_LITERAL).equals(BeanWithStaticProducerMethod.getString());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD, id = "aa") })
    public void testProducerOnNonBean() throws Exception {
        assert getBeans(Cherry.class).isEmpty();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DISPOSER_METHOD, id = "b") })
    public void testStaticDisposerMethod() throws Exception {
        assert getBeans(String.class, TAME_LITERAL).size() == 1;
        DependentInstance<String> stringBean =  newDependentInstance(String.class, TAME_LITERAL);
        assertTrue(stringBean.get().equals("Pete"));
        stringBean.destroy();
        assertTrue(BeanWithStaticProducerMethod.stringDestroyed);
    }

    @Test
    @SpecAssertion(section = PRODUCER_METHOD, id = "ga")
    public void testParameterizedReturnType() throws Exception {
        assert getBeans(new TypeLiteral<FunnelWeaver<Spider>>() {
        }).size() == 1;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD, id = "c"), @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "a"),
            @SpecAssertion(section = BUILTIN_QUALIFIERS, id = "a0"), @SpecAssertion(section = BUILTIN_QUALIFIERS, id = "aa") })
    public void testDefaultBindingType() throws Exception {
        assert getCurrentManager().getBeans(Tarantula.class).size() == 1;
        assert getCurrentManager().getBeans(Tarantula.class).iterator().next().getQualifiers().size() == 2;
        assert getCurrentManager().getBeans(Tarantula.class).iterator().next().getQualifiers().contains(new DefaultLiteral());
        assert getCurrentManager().getBeans(Tarantula.class).iterator().next().getQualifiers().contains(AnyLiteral.INSTANCE);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD_TYPES, id = "c"), @SpecAssertion(section = BEAN_TYPES, id = "l") })
    public void testApiTypeForClassReturn() throws Exception {
        assert getBeans(Tarantula.class).size() == 1;
        Bean<Tarantula> tarantula = getBeans(Tarantula.class).iterator().next();

        assert tarantula.getTypes().size() == 6;
        assert tarantula.getTypes().contains(Tarantula.class);
        assert tarantula.getTypes().contains(DeadlySpider.class);
        assert tarantula.getTypes().contains(Spider.class);
        assert tarantula.getTypes().contains(Animal.class);
        assert tarantula.getTypes().contains(DeadlyAnimal.class);
        assert tarantula.getTypes().contains(Object.class);
    }

    @Test
    @SpecAssertion(section = PRODUCER_METHOD_TYPES, id = "a")
    public void testApiTypeForInterfaceReturn() throws Exception {
        assert getBeans(Bite.class).size() == 1;
        Bean<Bite> animal = getBeans(Bite.class).iterator().next();
        assert animal.getTypes().size() == 2;
        assert animal.getTypes().contains(Bite.class);
        assert animal.getTypes().contains(Object.class);
    }

    @Test
    @SpecAssertion(section = PRODUCER_METHOD_TYPES, id = "ba")
    public void testApiTypeForPrimitiveReturn() throws Exception {
        Set<Bean<Integer>> beans = getBeans(Integer.class,NUMBER_LITERAL);
        assertEquals(beans.size(), 1);
        Bean<Integer> bean = beans.iterator().next();
        Assert.assertTypeSetMatches(bean.getTypes(), Object.class, int.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD_TYPES, id = "bb"), @SpecAssertion(section = LEGAL_BEAN_TYPES, id = "i") })
    public void testApiTypeForArrayTypeReturn() throws Exception {
        assert getBeans(Spider[].class).size() == 1;
        Bean<Spider[]> spiders = getBeans(Spider[].class).iterator().next();
        assert spiders.getTypes().size() == 2;
        assert spiders.getTypes().contains(Spider[].class);
        assert spiders.getTypes().contains(Object.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "be"), @SpecAssertion(section = PRODUCER_METHOD, id = "k"),
            @SpecAssertion(section = DECLARING_BEAN_QUALIFIERS, id = "b") })
    public void testBindingType() throws Exception {
        assert getBeans(Tarantula.class, TAME_LITERAL).size() == 1;
        Bean<Tarantula> tarantula = getBeans(Tarantula.class, TAME_LITERAL).iterator().next();
        assert tarantula.getQualifiers().size() == 2;
        assert tarantula.getQualifiers().contains(TAME_LITERAL);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "ba"), @SpecAssertion(section = PRODUCER_METHOD, id = "k") })
    public void testScopeType() throws Exception {
        assert getBeans(DaddyLongLegs.class, TAME_LITERAL).size() == 1;
        Bean<DaddyLongLegs> daddyLongLegs = getBeans(DaddyLongLegs.class, TAME_LITERAL).iterator().next();
        assert daddyLongLegs.getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "bb"), @SpecAssertion(section = DECLARING_BEAN_NAME, id = "b") })
    public void testNamedMethod() throws Exception {
        assert getBeans(BlackWidow.class, TAME_LITERAL).size() == 1;
        Bean<BlackWidow> blackWidowSpider = getBeans(BlackWidow.class, TAME_LITERAL).iterator().next();
        assert blackWidowSpider.getName().equals("blackWidow");
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "bb"), @SpecAssertion(section = DEFAULT_NAME, id = "b"),
            @SpecAssertion(section = DEFAULT_NAME, id = "fb"), @SpecAssertion(section = DECLARING_BEAN_NAME, id = "d") })
    public void testDefaultNamedMethod() throws Exception {
        String name = "produceDaddyLongLegs";
        Bean<DaddyLongLegs> daddyLongLegs = getUniqueBean(DaddyLongLegs.class, TAME_LITERAL);
        assertEquals(daddyLongLegs.getName(), name);
        // Any, Tame, Named
        assertTrue(annotationSetMatches(daddyLongLegs.getQualifiers(), AnyLiteral.INSTANCE, TAME_LITERAL,
                new NamedLiteral(name)));
    }

    // Review 2.2
    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_STEREOTYPES, id = "b"), @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "ba"),
            @SpecAssertion(section = DEFAULT_SCOPE, id = "c"), @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "bd") })
    public void testStereotypeSpecifiesScope() throws Exception {
        assert getBeans(WolfSpider.class, TAME_LITERAL).size() == 1;
        Bean<WolfSpider> wolfSpider = getBeans(WolfSpider.class, TAME_LITERAL).iterator().next();
        assert wolfSpider.getScope().equals(RequestScoped.class);
    }

    @Test(expectedExceptions = UnsatisfiedResolutionException.class)
    @SpecAssertions({ @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "da"), @SpecAssertion(section = SPECIALIZATION, id = "cb") })
    public void testNonStaticProducerMethodNotInheritedBySpecializingSubclass() {
        assert getBeans(Egg.class, new AnnotationLiteral<Yummy>() {
        }).size() == 0;
        getContextualReference(Egg.class, new AnnotationLiteral<Yummy>() {
        });
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "da"), @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "dg") })
    public void testNonStaticProducerMethodNotInherited() {
        assert getBeans(Apple.class, new AnnotationLiteral<Yummy>() {
        }).size() == 1;
        assert getContextualReference(Apple.class, new AnnotationLiteral<Yummy>() {
        }).getTree().getClass().equals(AppleTree.class);
    }

    /**
     * Note on the "3.3.2 h" assertion related to CDI section "3.3.2. Declaring a producer method" statement
     * "A producer method may have any number of parameters." JVM spec allows max 255 params. This test works with two producer
     * method params - {@link SpiderProducer#producesDeadliestTarantula(Tarantula, Tarantula)}. To fulfill the assertion
     * requirements we would need to test 255 producer methods with 1 to 255 parameter injection points.
     */
    @Test
    @SpecAssertions({ @SpecAssertion(section = METHOD_CONSTRUCTOR_PARAMETER_QUALIFIERS, id = "a"), @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "i"),
            @SpecAssertion(section = DECLARING_PRODUCER_METHOD, id = "h"), @SpecAssertion(section = PRODUCER_OR_DISPOSER_METHODS_INVOCATION, id = "e") })
    public void testBindingTypesAppliedToProducerMethodParameters() {
        Bean<Tarantula> tarantula = getBeans(Tarantula.class, DEADLIEST_LITERAL).iterator().next();
        CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantula);
        Tarantula instance = tarantula.create(creationalContext);
        assert instance.getDeathsCaused() == 1;
    }

    @Test
    @SpecAssertion(section = PRODUCER_METHOD, id = "e")
    public void testDependentProducerReturnsNullValue() {
        assert getContextualReference(Acorn.class) == null;
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
        assert getBeans(new TypeLiteral<List<Spider>>() {
        }).size() == 1;
    }
}
