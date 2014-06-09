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
package org.jboss.cdi.tck.tests.implementation.simple.lifecycle;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.DependentInstance;
import org.jboss.cdi.tck.util.MockCreationalContext;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.CreationException;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Specializes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;


import java.lang.annotation.Annotation;
import static org.jboss.cdi.tck.cdi.Sections.*;
import static org.testng.Assert.*;
import static org.testng.Assert.assertFalse;

@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class SimpleBeanLifecycleTest extends AbstractTest {
    private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SimpleBeanLifecycleTest.class).withBeansXml("beans.xml").build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_BEAN_CONSTRUCTOR, id = "f"), @SpecAssertion(section = DECLARING_BEAN_CONSTRUCTOR, id = "g"),
            @SpecAssertion(section = METHOD_CONSTRUCTOR_PARAMETER_QUALIFIERS, id = "d") })
    public void testInjectionOfParametersIntoBeanConstructor() {
        assert getBeans(FishPond.class).size() == 1;
        FishPond fishPond = getContextualReference(FishPond.class);
        assert fishPond.goldfish != null;
        assert fishPond.goldfish instanceof Goldfish;
        assert fishPond.goose != null;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY, id = "b") })
    public void testSerializeRequestScoped() throws Exception {
        Cod codInstance = getContextualReference(Cod.class);

        byte[] bytes = passivate(codInstance);
        Object object = activate(bytes);
        codInstance = (Cod) object;
        assert getCurrentConfiguration().getBeans().isProxy(codInstance);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY, id = "b") })
    public void testSerializeSessionScoped() throws Exception {
        Bream instance = getContextualReference(Bream.class);

        byte[] bytes = passivate(instance);
        Object object = activate(bytes);
        instance = (Bream) object;
        assert getCurrentConfiguration().getBeans().isProxy(instance);
    }

    @Test
    @SpecAssertion(section = DECLARING_BEAN_CONSTRUCTOR, id = "g")
    public void testQualifierTypeAnnotatedConstructor() {
        getContextualReference(Duck.class);
        assert Duck.constructedCorrectly;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SPECIALIZE_MANAGED_BEAN, id = "ac") })
    public void testSpecializedBeanExtendsManagedBean() {
        assert MountainLion.class.getAnnotation(Specializes.class) != null;
        Bean<Lion> bean = null;
        Bean<Lion> specializedBean = null;
        for (Bean<Lion> lionBean : getBeans(Lion.class, TAME_LITERAL)) {
            if (lionBean.getBeanClass().equals(Lion.class)) {
                bean = lionBean;
            } else if (lionBean.getBeanClass().equals(MountainLion.class)) {
                specializedBean = lionBean;
            }
        }

        assert bean == null;
        assert specializedBean != null;
        assert specializedBean.getBeanClass().getSuperclass().equals(Lion.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = CREATIONAL_CONTEXT, id = "d"), @SpecAssertion(section = CREATIONAL_CONTEXT, id = "g") })
    public void testCreateReturnsSameBeanPushed() {
        final CreationalContext<ShoeFactory> creationalContext = new MockCreationalContext<ShoeFactory>();
        final Contextual<ShoeFactory> bean = getBeans(ShoeFactory.class).iterator().next();
        MockCreationalContext.reset();
        ShoeFactory instance = getCurrentManager().getContext(Dependent.class).get(bean, creationalContext);
        if (MockCreationalContext.isPushCalled()) {
            assert instance == MockCreationalContext.getLastBeanPushed();
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = MANAGED_BEAN_LIFECYCLE, id = "aa") })
    public void testBeanCreateInjectsDependenciesAndInvokesInitializerToInstantiateInstance() {
        MockCreationalContext.reset();
        final CreationalContext<FishPond> creationalContext = new MockCreationalContext<FishPond>();
        final Contextual<FishPond> bean = getBeans(FishPond.class).iterator().next();
        FishPond fishPond = bean.create(creationalContext);
        assert fishPond != null;
        assert fishPond.goldfish != null;
        assert fishPond.goldfish instanceof Goldfish;
        assert fishPond.goose != null;
        assert fishPond.salmon != null;
        assert fishPond.postConstructCalled; // required by Managed Bean specification
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = CONCEPTS, id = "g"), @SpecAssertion(section = LEGAL_BEAN_TYPES, id = "b"),
            @SpecAssertion(section = LEGAL_BEAN_TYPES, id = "k"), @SpecAssertion(section = INITIALIZATION, id = "ja") })
    public void testManagedBean() {
        assert getBeans(RedSnapper.class).size() == 1;
        assert getContextualReference(RedSnapper.class) instanceof RedSnapper;
        RedSnapper redSnapper = getContextualReference(RedSnapper.class);
        redSnapper.ping();
        assert redSnapper.isTouched();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = MANAGED_BEAN_LIFECYCLE, id = "aa"), @SpecAssertion(section = DECLARING_BEAN_CONSTRUCTOR, id = "aa"),
            @SpecAssertion(section = INJECTED_FIELD_QUALIFIERS, id = "a"), @SpecAssertion(section = INJECTED_FIELDS, id = "a"),
            @SpecAssertion(section = DECLARING_INJECTED_FIELD, id = "aa"), @SpecAssertion(section = BEAN_ARCHIVE, id = "jg") })
    public void testCreateInjectsFieldsDeclaredInJava() {
        assert getBeans(TunaFarm.class).size() == 1;
        TunaFarm tunaFarm = getContextualReference(TunaFarm.class);
        assert tunaFarm.tuna != null;
        assert tunaFarm.tuna.getName().equals("Ophir");

        assert tunaFarm.qualifiedTuna != null;
        assert tunaFarm.qualifiedTuna.getName().equals("qualifiedTuna");
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = CONTEXT, id = "l") })
    public void testContextCreatesNewInstanceForInjection() {
        Context requestContext = getCurrentManager().getContext(RequestScoped.class);
        Bean<Tuna> tunaBean = getBeans(Tuna.class).iterator().next();
        assert requestContext.get(tunaBean) == null;
        TunaFarm tunaFarm = getContextualReference(TunaFarm.class);
        assert tunaFarm.tuna != null;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = MANAGED_BEAN_LIFECYCLE, id = "aa"), @SpecAssertion(section = MANAGED_BEAN_LIFECYCLE, id = "ba") })
    public void testPostConstructPreDestroy() {
        assert getBeans(Farm.class).size() == 1;
        Bean<Farm> farmBean = getBeans(Farm.class).iterator().next();
        CreationalContext<Farm> creationalContext = getCurrentManager().createCreationalContext(farmBean);
        Farm farm = farmBean.create(creationalContext);
        assert farm.founded != null;
        assert farm.initialStaff == 20;
        assert farm.closed == null;
        farmBean.destroy(farm, creationalContext);
        assert farm.closed != null;
        assert farm.farmOffice.noOfStaff == 0;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = CONTEXTUAL_REFERENCE, id = "a0"), @SpecAssertion(section = MANAGED_BEAN_LIFECYCLE, id = "ba"),
            @SpecAssertion(section = CONTEXTUAL_REFERENCE, id = "c") })
    public void testContextualDestroyDisposesWhenNecessary() {
        final Bean<Goose> gooseBean = getBeans(Goose.class).iterator().next();
        final CreationalContext<Goose> gooseCc = getCurrentManager().createCreationalContext(gooseBean);
        final Goose goose = gooseBean.create(gooseCc);
        // If the bean has a pseudo-scope, the container must obtain a contextual instance
        assert !getCurrentConfiguration().getBeans().isProxy(goose);
        assert !EggProducer.isEggDisposed();
        assert !Egg.isEggDestroyed();
        gooseBean.destroy(goose, gooseCc);
        assert EggProducer.isEggDisposed();
        assert !Egg.isEggDestroyed();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = CONTEXTUAL, id = "a1") })
    public void testContextualDestroyCatchesException() {
        Bean<Cod> codBean = getBeans(Cod.class).iterator().next();
        CreationalContext<Cod> creationalContext = getCurrentManager().createCreationalContext(codBean);
        Cod codInstance = codBean.create(creationalContext);
        codInstance.ping();
        codBean.destroy(codInstance, creationalContext);
        assertTrue(Cod.isExpcetionThrown());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DEPENDENT_OBJECTS_DESTRUCTION, id = "a") })
    public void testDependentsDestroyedAfterPreDestroy() {
        Bean<FishPond> pondBean = getBeans(FishPond.class).iterator().next();
        CreationalContext<FishPond> creationalContext = getCurrentManager().createCreationalContext(pondBean);
        FishPond fishPond = pondBean.create(creationalContext);
        pondBean.destroy(fishPond, creationalContext);
        assert Salmon.isBeanDestroyed();
    }

    @Test(dataProvider=ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "baa")
    public void testSubClassInheritsPostConstructOnSuperclass(Instance<Object> instance) {
        OrderProcessor.postConstructCalled = false;
        assertEquals(getBeans(CdOrderProcessor.class).size(), 1);
        getContextualReference(CdOrderProcessor.class).order();
        assertTrue(OrderProcessor.postConstructCalled);

        assertNotNull(instance);
        OrderProcessor.postConstructCalled = false;
        instance.select(CdOrderProcessor.class).get().order();
        assertTrue(OrderProcessor.postConstructCalled);
    }

    @Test
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "bac")
    public void testIndirectSubClassInheritsPostConstructOnSuperclass() {
        OrderProcessor.postConstructCalled = false;
        assert getBeans(IndirectOrderProcessor.class).size() == 1;
        getContextualReference(IndirectOrderProcessor.class).order();
        assert OrderProcessor.postConstructCalled;
    }

    @Test
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "bba")
    public void testSubClassInheritsPreDestroyOnSuperclass() {
        OrderProcessor.preDestroyCalled = false;
        assert getBeans(CdOrderProcessor.class).size() == 1;

        DependentInstance<CdOrderProcessor> bean = newDependentInstance(CdOrderProcessor.class);
        CdOrderProcessor instance = bean.get();
        assertFalse(instance.equals(null));
        bean.destroy();
        assertTrue(OrderProcessor.preDestroyCalled);
    }

    @Test
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "bbc")
    public void testIndirectSubClassInheritsPreDestroyOnSuperclass() {
        OrderProcessor.preDestroyCalled = false;
        assert getBeans(IndirectOrderProcessor.class).size() == 1;
        DependentInstance<IndirectOrderProcessor> bean = newDependentInstance(IndirectOrderProcessor.class);
        assertFalse(bean.get().equals(null));
        bean.destroy();
        assertTrue(OrderProcessor.preDestroyCalled);
    }

    @Test
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "baa")
    public void testSubClassDoesNotInheritPostConstructOnSuperclassBlockedByIntermediateClass() {
        assert getBeans(NovelOrderProcessor.class).size() == 1;
        OrderProcessor.postConstructCalled = false;
        getContextualReference(NovelOrderProcessor.class).order();
        assert !OrderProcessor.postConstructCalled;
    }

    @Test
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "bba")
    public void testSubClassDoesNotInheritPreDestroyConstructOnSuperclassBlockedByIntermediateClass() {
        OrderProcessor.preDestroyCalled = false;
        assert getBeans(NovelOrderProcessor.class).size() == 1;
        DependentInstance<NovelOrderProcessor> bean = newDependentInstance(NovelOrderProcessor.class);
        NovelOrderProcessor instance = bean.get();
        assertFalse(instance.equals(null));
        bean.destroy();
        assertFalse(OrderProcessor.preDestroyCalled);
    }

    @Test(expectedExceptions = CreationException.class)
    @SpecAssertion(section = CONTEXTUAL, id = "a0")
    public void testCreationExceptionWrapsCheckedExceptionThrownFromCreate() {
        assert getBeans(Lorry_Broken.class).size() == 1;
        getContextualReference(Lorry_Broken.class);
    }

    @Test(expectedExceptions = FooException.class)
    @SpecAssertion(section = CONTEXTUAL, id = "a0")
    public void testUncheckedExceptionThrownFromCreateNotWrapped() {
        assert getBeans(Van_Broken.class).size() == 1;
        getContextualReference(Van_Broken.class);
    }

}
