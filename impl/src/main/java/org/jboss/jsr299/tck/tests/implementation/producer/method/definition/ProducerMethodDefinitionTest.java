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
package org.jboss.jsr299.tck.tests.implementation.producer.method.definition;

import static org.jboss.jsr299.tck.TestGroups.PRODUCER_METHOD;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.IllegalProductException;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.jsr299.tck.literals.DefaultLiteral;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class ProducerMethodDefinitionTest extends AbstractJSR299Test {

    private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {
    };
    private static final Annotation DEADLIEST_LITERAL = new AnnotationLiteral<Deadliest>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProducerMethodDefinitionTest.class).build();
    }

    @Test(groups = PRODUCER_METHOD)
    @SpecAssertions({ @SpecAssertion(section = "3.3", id = "b"), @SpecAssertion(section = "5.5.4", id = "a") })
    public void testStaticMethod() throws Exception {
        assert getBeans(String.class).size() == 1;
        assert getInstanceByType(String.class).equals(BeanWithStaticProducerMethod.getString());
    }

    @Test(groups = { PRODUCER_METHOD })
    @SpecAssertions({ @SpecAssertion(section = "3.3", id = "aa") })
    public void testProducerOnNonBean() throws Exception {
        assert getBeans(Cherry.class).isEmpty();
    }

    @Test(groups = PRODUCER_METHOD)
    @SpecAssertions({ @SpecAssertion(section = "3.5", id = "b") })
    public void testStaticDisposerMethod() throws Exception {
        assert getBeans(String.class).size() == 1;
        String aString = getInstanceByType(String.class);
        Bean<String> stringBean = getBeans(String.class).iterator().next();
        CreationalContext<String> creationalContext = getCurrentManager().createCreationalContext(stringBean);
        stringBean.destroy(aString, creationalContext);
        assert BeanWithStaticProducerMethod.stringDestroyed;
    }

    @Test(groups = PRODUCER_METHOD)
    @SpecAssertion(section = "3.3", id = "ga")
    public void testParameterizedReturnType() throws Exception {
        assert getBeans(new TypeLiteral<FunnelWeaver<Spider>>() {
        }).size() == 1;
    }

    @Test(groups = PRODUCER_METHOD)
    @SpecAssertions({ @SpecAssertion(section = "3.3", id = "c"), @SpecAssertion(section = "3.3.2", id = "a"),
            @SpecAssertion(section = "2.3.1", id = "a0"), @SpecAssertion(section = "2.3.1", id = "aa") })
    public void testDefaultBindingType() throws Exception {
        assert getCurrentManager().getBeans(Tarantula.class).size() == 1;
        assert getCurrentManager().getBeans(Tarantula.class).iterator().next().getQualifiers().size() == 2;
        assert getCurrentManager().getBeans(Tarantula.class).iterator().next().getQualifiers().contains(new DefaultLiteral());
        assert getCurrentManager().getBeans(Tarantula.class).iterator().next().getQualifiers().contains(new AnyLiteral());
    }

    @Test(groups = PRODUCER_METHOD)
    @SpecAssertions({ @SpecAssertion(section = "3.3.1", id = "c"), @SpecAssertion(section = "2.2", id = "l") })
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

    @Test(groups = PRODUCER_METHOD)
    @SpecAssertion(section = "3.3.1", id = "a")
    public void testApiTypeForInterfaceReturn() throws Exception {
        assert getBeans(Bite.class).size() == 1;
        Bean<Bite> animal = getBeans(Bite.class).iterator().next();
        assert animal.getTypes().size() == 2;
        assert animal.getTypes().contains(Bite.class);
        assert animal.getTypes().contains(Object.class);
    }

    @Test(groups = PRODUCER_METHOD)
    @SpecAssertion(section = "3.3.1", id = "ba")
    public void testApiTypeForPrimitiveReturn() throws Exception {
        assert getBeans(Integer.class).size() == 1;
        Bean<Integer> integer = getBeans(Integer.class).iterator().next();
        assert integer.getTypes().size() == 2;
        assert integer.getTypes().contains(int.class);
        assert integer.getTypes().contains(Object.class);
    }

    @Test(groups = PRODUCER_METHOD)
    @SpecAssertions({ @SpecAssertion(section = "3.3.1", id = "bb"), @SpecAssertion(section = "2.2.1", id = "i") })
    public void testApiTypeForArrayTypeReturn() throws Exception {
        assert getBeans(Spider[].class).size() == 1;
        Bean<Spider[]> spiders = getBeans(Spider[].class).iterator().next();
        assert spiders.getTypes().size() == 2;
        assert spiders.getTypes().contains(Spider[].class);
        assert spiders.getTypes().contains(Object.class);
    }

    @Test(groups = PRODUCER_METHOD)
    @SpecAssertions({ @SpecAssertion(section = "3.3.2", id = "be"), @SpecAssertion(section = "3.3", id = "k"),
            @SpecAssertion(section = "2.3.3", id = "b") })
    public void testBindingType() throws Exception {
        assert getBeans(Tarantula.class, TAME_LITERAL).size() == 1;
        Bean<Tarantula> tarantula = getBeans(Tarantula.class, TAME_LITERAL).iterator().next();
        assert tarantula.getQualifiers().size() == 2;
        assert tarantula.getQualifiers().contains(TAME_LITERAL);
    }

    @Test(groups = PRODUCER_METHOD)
    @SpecAssertions({ @SpecAssertion(section = "3.3.2", id = "ba"), @SpecAssertion(section = "3.3", id = "k") })
    public void testScopeType() throws Exception {
        assert getBeans(DaddyLongLegs.class, TAME_LITERAL).size() == 1;
        Bean<DaddyLongLegs> daddyLongLegs = getBeans(DaddyLongLegs.class, TAME_LITERAL).iterator().next();
        assert daddyLongLegs.getScope().equals(RequestScoped.class);
    }

    @Test(groups = PRODUCER_METHOD)
    @SpecAssertions({ @SpecAssertion(section = "3.3.2", id = "bb"), @SpecAssertion(section = "2.5.1", id = "b") })
    public void testNamedMethod() throws Exception {
        assert getBeans(BlackWidow.class, TAME_LITERAL).size() == 1;
        Bean<BlackWidow> blackWidowSpider = getBeans(BlackWidow.class, TAME_LITERAL).iterator().next();
        assert blackWidowSpider.getName().equals("blackWidow");
    }

    @Test(groups = PRODUCER_METHOD)
    @SpecAssertions({ @SpecAssertion(section = "3.3.2", id = "bb"), @SpecAssertion(section = "2.5.2", id = "b"),
            @SpecAssertion(section = "2.5.1", id = "d"), @SpecAssertion(section = "3.3.8", id = "a") })
    public void testDefaultNamedMethod() throws Exception {
        assert getBeans(DaddyLongLegs.class, TAME_LITERAL).size() == 1;
        Bean<DaddyLongLegs> daddyLongLegsSpider = getBeans(DaddyLongLegs.class, TAME_LITERAL).iterator().next();
        assert daddyLongLegsSpider.getName().equals("produceDaddyLongLegs");
    }

    // Review 2.2
    @Test(groups = PRODUCER_METHOD)
    @SpecAssertions({ @SpecAssertion(section = "2.7.2", id = "b"), @SpecAssertion(section = "3.3.2", id = "ba"),
            @SpecAssertion(section = "2.4.4", id = "c"), @SpecAssertion(section = "3.3.2", id = "bd") })
    public void testStereotypeSpecifiesScope() throws Exception {
        assert getBeans(WolfSpider.class, TAME_LITERAL).size() == 1;
        Bean<WolfSpider> wolfSpider = getBeans(WolfSpider.class, TAME_LITERAL).iterator().next();
        assert wolfSpider.getScope().equals(RequestScoped.class);
    }

    @Test(expectedExceptions = UnsatisfiedResolutionException.class)
    @SpecAssertions({ @SpecAssertion(section = "4.2", id = "da"), @SpecAssertion(section = "4.3", id = "cb") })
    public void testNonStaticProducerMethodNotInheritedBySpecializingSubclass() {
        assert getBeans(Egg.class, new AnnotationLiteral<Yummy>() {
        }).size() == 0;
        getInstanceByType(Egg.class, new AnnotationLiteral<Yummy>() {
        });
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "4.2", id = "da"), @SpecAssertion(section = "4.2", id = "dg") })
    public void testNonStaticProducerMethodNotInherited() {
        assert getBeans(Apple.class, new AnnotationLiteral<Yummy>() {
        }).size() == 1;
        assert getInstanceByType(Apple.class, new AnnotationLiteral<Yummy>() {
        }).getTree().getClass().equals(AppleTree.class);
    }

    /**
     * Note on the "3.3.2 h" assertion related to CDI section "3.3.2. Declaring a producer method" statement
     * "A producer method may have any number of parameters." JVM spec allows max 255 params. This test works with two producer
     * method params - {@link SpiderProducer#producesDeadliestTarantula(Tarantula, Tarantula)}. To fulfill the assertion
     * requirements we would need to test 255 producer methods with 1 to 255 parameter injection points.
     */
    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.3.5", id = "a"), @SpecAssertion(section = "3.3.2", id = "i"),
            @SpecAssertion(section = "3.3.2", id = "h") })
    public void testBindingTypesAppliedToProducerMethodParameters() {
        Bean<Tarantula> tarantula = getBeans(Tarantula.class, DEADLIEST_LITERAL).iterator().next();
        CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantula);
        Tarantula instance = tarantula.create(creationalContext);
        assert instance.getDeathsCaused() == 1;
    }

    @Test
    @SpecAssertion(section = "3.3", id = "e")
    public void testDependentProducerReturnsNullValue() {
        assert getInstanceByType(Acorn.class) == null;
    }

    @Test(expectedExceptions = { IllegalProductException.class })
    @SpecAssertion(section = "3.3", id = "f")
    public void testNonDependentProducerReturnsNullValue() {
        getInstanceByType(Pollen.class, new AnnotationLiteral<Yummy>() {
        }).ping();
        assert false;
    }

    @Test
    @SpecAssertion(section = "3.3", id = "iaa")
    public void testTypeVariableReturnType() {
        // should be created by SpiderListProducer
        assert getBeans(new TypeLiteral<List<Spider>>() {
        }).size() == 1;
    }
}
