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
package org.jboss.cdi.tck.tests.extensions.alternative.metadata;

import static org.jboss.cdi.tck.cdi.Sections.ALTERNATIVE_METADATA_SOURCES;
import static org.jboss.cdi.tck.cdi.Sections.ALTERNATIVE_METADATA_SOURCES_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test class contains tests for adding meta data using extensions.
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 * @author Tomas Remes
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class AlternativeMetadataTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AlternativeMetadataTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).getOrCreateInterceptors()
                                .clazz(GroceryInterceptor.class.getName()).up()
                )
                .withExtension(ProcessAnnotatedTypeObserver.class).build();
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "ha")
    public void testGetBaseTypeUsedToDetermineTypeOfInjectionPoint() {
        // The base type of the fruit injection point is overridden to
        // TropicalFruit
        assertTrue(GroceryWrapper.isGetBaseTypeOfFruitFieldUsed());
        assertEquals(getContextualReference(Grocery.class, AnyLiteral.INSTANCE).getFruit().getMetadata().getType(),
                TropicalFruit.class);
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "hb")
    public void testGetBaseTypeUsedToDetermineTypeOfInitializerInjectionPoint() {
        assertEquals(getContextualReference(Grocery.class, AnyLiteral.INSTANCE).getInitializerFruit().getMetadata().getType(),
                TropicalFruit.class);
        assertTrue(GroceryWrapper.isGetBaseTypeOfInitializerTropicalFruitParameterUsed());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "hc"),
            @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "s") })
    public void testGetBaseTypeUsedToDetermineTypeOfConstructorInjectionPoint() {
        assertEquals(getContextualReference(Market.class).getConstructorFruit().getMetadata().getType(),
                TropicalFruit.class);
        assertTrue(MarketWrapper.isGetBaseTypeOfMarketConstructorParameterUsed());
        // s assertion
        assertTrue(getContextualReference(Market.class).getConstructorFruit().getMetadata().getQualifiers().contains(AnyLiteral.INSTANCE));
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "hd")
    public void testGetBaseTypeUsedToDetermineTypeOfProducerInjectionPoint() {
        assertEquals(getContextualReference(Bill.class, new ExpensiveLiteral()).getFruit().getMetadata().getType(), TropicalFruit.class);
        assertTrue(MarketWrapper.isGetBaseTypeOfBillProducerParameterUsed());
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "he")
    public void testGetBaseTypeUsedToDetermineTypeOfObserverInjectionPoint() {
        getCurrentManager().fireEvent(new Milk(false));
        assertEquals(getContextualReference(Grocery.class, AnyLiteral.INSTANCE).getObserverFruit().getMetadata().getType(),
                TropicalFruit.class);

        assertTrue(GroceryWrapper.isGetBaseTypeOfObserverInjectionPointUsed());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "hf"),
            @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "ad") })
    public void testGetBaseTypeUsedToDetermineTypeOfDisposerInjectionPoint() {
        Bean<Bill> bill = getBeans(Bill.class, new CheapLiteral()).iterator().next();
        CreationalContext<Bill> context = getCurrentManager().createCreationalContext(bill);
        Bill instance = getCurrentManager().getContext(bill.getScope()).get(bill);
        bill.destroy(instance, context);
        assertEquals(getContextualReference(Grocery.class, AnyLiteral.INSTANCE).getDisposerFruit().getMetadata().getType(),
                TropicalFruit.class);
        assertTrue(GroceryWrapper.isGetBaseTypeOfBillDisposerParameterUsed());
        // ad assertion
        assertTrue(getContextualReference(Grocery.class, AnyLiteral.INSTANCE).getDisposerFruit().getMetadata().getQualifiers().contains(AnyLiteral.INSTANCE));
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "i")
    public void testGetBaseTypeUsedToDetermineTypeOfEventParameter() {
        getCurrentManager().fireEvent(new Carrot());
        assertEquals(getContextualReference(Grocery.class, AnyLiteral.INSTANCE).getWrappedEventParameter().getClass(),
                Carrot.class);
        assertTrue(GroceryWrapper.isGetBaseTypeOfObserverParameterUsed());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "j"),
            @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "ac") })
    public void testGetBaseTypeUsedToDetermineTypeOfDisposalParameter() throws NoSuchMethodException {
        Bean<Carrot> carrot = getBeans(Carrot.class, new CheapLiteral()).iterator().next();
        CreationalContext<Carrot> context = getCurrentManager().createCreationalContext(carrot);
        Carrot instance = getCurrentManager().getContext(carrot.getScope()).get(carrot, context);
        carrot.destroy(instance, context);
        // ac assertion - disposal method called after adding extra qualifier to disposer parameter
        assertNotNull(getContextualReference(Grocery.class, AnyLiteral.INSTANCE).getWrappedDisposalParameter());
        assertEquals(getContextualReference(Grocery.class, AnyLiteral.INSTANCE).getWrappedDisposalParameter().getClass(),
                Carrot.class);

    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "ka")
    public void testGetTypeClosureUsed() {
        assertTrue(GroceryWrapper.isGetTypeClosureUsed());
        // should be [Object, Grocery] instead of [Object, Shop, Grocery]
        assertEquals(getBeans(Grocery.class, AnyLiteral.INSTANCE).iterator().next().getTypes().size(), 2);
        assertEquals(getBeans(Shop.class, AnyLiteral.INSTANCE).size(), 0);
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES_EE, id = "kb")
    public void testGetTypeClosureUsedToDetermineTypeOfSessionBean() {
        Bean<Pasta> pasta = getBeans(Pasta.class).iterator().next();
        assertEquals(pasta.getTypes().size(), 2);
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "kc")
    public void testGetTypeClosureUsedToDetermineTypeOfProducerField() {
        //produced by Market.carrot
        Bean<Carrot> carrot = getBeans(Carrot.class, new ExpensiveLiteral()).iterator().next();
        // should be [Carrot] instead of [Carrot, Vegetables]
        assertEquals(carrot.getTypes().size(), 1);
        assertEquals(carrot.getTypes().iterator().next(), Carrot.class);
        assertTrue(MarketWrapper.isGetTypeCLosureOfProducerFieldUsed());
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "kd")
    public void testGetTypeClosureUsedToDetermineTypeOfProducerMethod() {
        // produced by Grocery.createVegetable()
        Bean<Carrot> carrot = getBeans(Carrot.class, new CheapLiteral()).iterator().next();
        // should be [Object, Carrot] instead of [Object, Carrot, Vegetables]
        assertEquals(carrot.getTypes().size(), 2);
        assertFalse(carrot.getTypes().contains(Vegetables.class));
        assertTrue(GroceryWrapper.isGetTypeClosureOfProducerMethodUsed());
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "l")
    public void testGetAnnotationUsedForGettingScopeInformation() {
        // @ApplicationScoped is overridden by @RequestScoped
        assertEquals(getBeans(Grocery.class, AnyLiteral.INSTANCE).iterator().next().getScope(), RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "m")
    public void testGetAnnotationUsedForGettingQualifierInformation() {
        // @Expensive is overridden by @Cheap
        assertEquals(getBeans(Grocery.class, new CheapLiteral()).size(), 1);
        assertEquals(getBeans(Grocery.class, new ExpensiveLiteral()).size(), 0);
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "n")
    public void testGetAnnotationUsedForGettingStereotypeInformation() {
        // The extension adds a stereotype with @Named qualifier
        assertNotNull(getContextualReference("grocery", Grocery.class));
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "p")
    public void testGetAnnotationUsedForGettingInterceptorInformation() {
        // The extension adds the GroceryInterceptorBinding
        Grocery grocery = getContextualReference(Grocery.class, AnyLiteral.INSTANCE);
        assertEquals(grocery.foo(), "foo");
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "r")
    public void testPreviouslyNonInjectAnnotatedConstructorIsUsed() {
        assertTrue(getContextualReference(Grocery.class, AnyLiteral.INSTANCE).isConstructorWithParameterUsed());
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "t")
    public void testPreviouslyNonInjectAnnotatedFieldIsInjected() {
        assertTrue(getContextualReference(Grocery.class, AnyLiteral.INSTANCE).isVegetablesInjected());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "u")
    public void testExtraQualifierIsAppliedToInjectedField() {
        assertNotNull(getContextualReference(Grocery.class, AnyLiteral.INSTANCE).getFruit());
        Set<Annotation> qualifiers = getContextualReference(Grocery.class, AnyLiteral.INSTANCE).getFruit().getMetadata()
                .getQualifiers();
        assertEquals(qualifiers.size(), 1);
        assertTrue(annotationSetMatches(qualifiers, Cheap.class));
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "v")
    public void testProducesCreatesProducerField() {
        // The extension adds @Producer to the bread field
        assertEquals(getBeans(Bread.class, AnyLiteral.INSTANCE).size(), 1);
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "w")
    public void testInjectCreatesInitializerMethod() {
        // The extension adds @Inject to the nonInjectAnnotatedInitializer()
        // method
        assertTrue(getContextualReference(Grocery.class, AnyLiteral.INSTANCE).isWaterInjected());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "x")
    public void testQualifierAddedToInitializerParameter() {
        // The @Cheap qualifier is added to the method parameter
        Set<Annotation> qualifiers = getContextualReference(Grocery.class, AnyLiteral.INSTANCE).getInitializerFruit().getMetadata()
                .getQualifiers();
        assertTrue(annotationSetMatches(qualifiers, Cheap.class));
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "y")
    public void testProducesCreatesProducerMethod() {
        // The extension adds @Producer to the getMilk() method
        assertEquals(getBeans(Milk.class, AnyLiteral.INSTANCE).size(), 1);
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "z")
    public void testQualifierIsAppliedToProducerMethod() {
        // The extension adds @Expensive to the getMilk() method
        assertEquals(getBeans(Yogurt.class, new ExpensiveLiteral()).size(), 1);
        assertEquals(getBeans(Yogurt.class, new CheapLiteral()).size(), 0);
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "aa")
    public void testQualifierIsAppliedToProducerMethodParameter() {
        // The @Cheap qualifier is added to the method parameter
        Set<Annotation> qualifiers = getContextualReference(Yogurt.class, AnyLiteral.INSTANCE).getFruit().getMetadata()
                .getQualifiers();
        assertEquals(qualifiers.size(), 1);
        assertTrue(annotationSetMatches(qualifiers, Cheap.class));
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "ab")
    public void testDisposesIsAppliedToMethodParameter() {
        Bean<Yogurt> yogurt = getBeans(Yogurt.class, new ExpensiveLiteral()).iterator().next();
        CreationalContext<Yogurt> context = getCurrentManager().createCreationalContext(yogurt);
        Yogurt instance = getCurrentManager().getContext(yogurt.getScope()).get(yogurt, context);
        yogurt.destroy(instance, context);
        assertTrue(Grocery.isDisposerMethodCalled());
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "ae"), @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "ag") })
    public void testObserverMethod() {
        getCurrentManager().fireEvent(new Milk(true));
        Milk event = getContextualReference(Grocery.class, AnyLiteral.INSTANCE).getObserverEvent();
        TropicalFruit parameter = getContextualReference(Grocery.class, AnyLiteral.INSTANCE).getObserverParameter();
        assertNotNull(event);
        assertNotNull(parameter);
        assertEquals(parameter.getMetadata().getQualifiers().size(), 1);
        assertTrue(annotationSetMatches(parameter.getMetadata().getQualifiers(), Cheap.class));
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "af")
    public void testExtraQualifierAppliedToObservesMethodParameter() {
        getCurrentManager().fireEvent(new Bread(true));
        // normally, the event would be observer, however the extension adds the
        // @Expensive qualifier to the method parameter
        assertFalse(getContextualReference(Grocery.class, AnyLiteral.INSTANCE).isObserver2Used());
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "h")
    public void testContainerUsesOperationsOfAnnotatedNotReflectionApi() {
        assertEquals(getBeans(Sausage.class, AnyLiteral.INSTANCE).size(), 1);
        // Overriding annotated type has no methods and fields and thus there are no cheap and expensive sausages
        assertTrue(getBeans(Sausage.class, new AnnotationLiteral<Expensive>() {
        }).isEmpty());
        assertTrue(getBeans(Sausage.class, new AnnotationLiteral<Cheap>() {
        }).isEmpty());
    }

}
