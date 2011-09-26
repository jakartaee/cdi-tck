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
package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import java.lang.annotation.Annotation;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.CreationException;
import javax.enterprise.inject.Specializes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.impl.MockCreationalContext;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class SimpleBeanLifecycleTest extends AbstractJSR299Test {
    private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SimpleBeanLifecycleTest.class).withBeansXml("beans.xml").build();
    }

    @Test(groups = "beanConstruction")
    @SpecAssertions({ @SpecAssertion(section = "3.7.1", id = "f"), @SpecAssertion(section = "3.7.1", id = "g"),
            @SpecAssertion(section = "2.3.5", id = "d") })
    public void testInjectionOfParametersIntoBeanConstructor() {
        assert getBeans(FishPond.class).size() == 1;
        FishPond fishPond = getInstanceByType(FishPond.class);
        assert fishPond.goldfish != null;
        assert fishPond.goldfish instanceof Goldfish;
        assert fishPond.goose != null;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6.6.2", id = "b") })
    public void testSerializeRequestScoped() throws Exception {
        Cod codInstance = getInstanceByType(Cod.class);

        byte[] bytes = serialize(codInstance);
        Object object = deserialize(bytes);
        codInstance = (Cod) object;
        assert getCurrentConfiguration().getBeans().isProxy(codInstance);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "6.6.2", id = "b") })
    public void testSerializeSessionScoped() throws Exception {
        Bream instance = getInstanceByType(Bream.class);

        byte[] bytes = serialize(instance);
        Object object = deserialize(bytes);
        instance = (Bream) object;
        assert getCurrentConfiguration().getBeans().isProxy(instance);
    }

    @Test
    @SpecAssertion(section = "3.7.1", id = "g")
    public void testQualifierTypeAnnotatedConstructor() {
        getInstanceByType(Duck.class);
        assert Duck.constructedCorrectly;
    }

    @Test(groups = { "specialization" })
    @SpecAssertions({ @SpecAssertion(section = "3.1.4", id = "ac") })
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

    @Test(groups = "beanLifecycle")
    @SpecAssertions({ @SpecAssertion(section = "6.1.1", id = "d"), @SpecAssertion(section = "6.1.1", id = "g") })
    public void testCreateReturnsSameBeanPushed() {
        final CreationalContext<ShoeFactory> creationalContext = new MockCreationalContext<ShoeFactory>();
        final Contextual<ShoeFactory> bean = getBeans(ShoeFactory.class).iterator().next();
        MockCreationalContext.reset();
        ShoeFactory instance = getCurrentManager().getContext(Dependent.class).get(bean, creationalContext);
        if (MockCreationalContext.isPushCalled()) {
            assert instance == MockCreationalContext.getLastBeanPushed();
        }
    }

    @Test(groups = "beanLifecycle")
    @SpecAssertions({ @SpecAssertion(section = "7.3.1", id = "aa") })
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

    @Test(groups = { "beanLifecycle" })
    @SpecAssertions({ @SpecAssertion(section = "2", id = "g"), @SpecAssertion(section = "2.2.1", id = "b"),
            @SpecAssertion(section = "2.2.1", id = "k"), @SpecAssertion(section = "12.2", id = "da") })
    public void testManagedBean() {
        assert getBeans(RedSnapper.class).size() == 1;
        assert getInstanceByType(RedSnapper.class) instanceof RedSnapper;
        RedSnapper redSnapper = getInstanceByType(RedSnapper.class);
        redSnapper.ping();
        assert redSnapper.isTouched();
    }

    @Test(groups = "injection")
    @SpecAssertions({ @SpecAssertion(section = "7.3.1", id = "aa"), @SpecAssertion(section = "3.8.1", id = "aa"),
            @SpecAssertion(section = "2.3.4", id = "a"), @SpecAssertion(section = "3.8", id = "a"),
            @SpecAssertion(section = "12.1", id = "bca") })
    public void testCreateInjectsFieldsDeclaredInJava() {
        assert getBeans(TunaFarm.class).size() == 1;
        TunaFarm tunaFarm = getInstanceByType(TunaFarm.class);
        assert tunaFarm.tuna != null;
        assert tunaFarm.tuna.getName().equals("Ophir");

        assert tunaFarm.qualifiedTuna != null;
        assert tunaFarm.qualifiedTuna.getName().equals("qualifiedTuna");
    }

    @Test(groups = "beanLifecycle")
    @SpecAssertions({ @SpecAssertion(section = "6.2", id = "l") })
    public void testContextCreatesNewInstanceForInjection() {
        Context requestContext = getCurrentManager().getContext(RequestScoped.class);
        Bean<Tuna> tunaBean = getBeans(Tuna.class).iterator().next();
        assert requestContext.get(tunaBean) == null;
        TunaFarm tunaFarm = getInstanceByType(TunaFarm.class);
        assert tunaFarm.tuna != null;
    }

    @Test(groups = { "beanLifecycle", "lifecycleCallbacks" })
    @SpecAssertions({ @SpecAssertion(section = "7.3.1", id = "aa"), @SpecAssertion(section = "7.3.1", id = "ba") })
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

    @Test(groups = { "beanLifecycle", "lifecycleCallbacks" })
    @SpecAssertions({ @SpecAssertion(section = "6.5.3", id = "a0"), @SpecAssertion(section = "7.3.1", id = "ba") })
    public void testContextualDestroyDisposesWhenNecessary() {
        final Bean<Goose> gooseBean = getBeans(Goose.class).iterator().next();
        final CreationalContext<Goose> gooseCc = getCurrentManager().createCreationalContext(gooseBean);
        final Goose goose = gooseBean.create(gooseCc);
        assert !EggProducer.isEggDisposed();
        assert !Egg.isEggDestroyed();
        gooseBean.destroy(goose, gooseCc);
        assert EggProducer.isEggDisposed();
        assert !Egg.isEggDestroyed();
    }

    @Test(groups = "beanLifecycle")
    @SpecAssertions({ @SpecAssertion(section = "6.1", id = "a1") })
    public void testContextualDestroyCatchesException() {
        Bean<Cod> codBean = getBeans(Cod.class).iterator().next();
        CreationalContext<Cod> creationalContext = getCurrentManager().createCreationalContext(codBean);
        Cod codInstance = getInstanceByType(Cod.class);
        codInstance.ping();
        codBean.destroy(codInstance, creationalContext);
    }

    @Test(groups = "beanLifecycle")
    @SpecAssertions({ @SpecAssertion(section = "5.5.3", id = "a") })
    public void testDependentsDestroyedAfterPreDestroy() {
        Bean<FishPond> pondBean = getBeans(FishPond.class).iterator().next();
        CreationalContext<FishPond> creationalContext = getCurrentManager().createCreationalContext(pondBean);
        FishPond fishPond = pondBean.create(creationalContext);
        pondBean.destroy(fishPond, creationalContext);
        assert Salmon.isBeanDestroyed();
    }

    @Test
    @SpecAssertion(section = "4.2", id = "baa")
    public void testSubClassInheritsPostConstructOnSuperclass() {
        OrderProcessor.postConstructCalled = false;
        assert getBeans(CdOrderProcessor.class).size() == 1;
        getInstanceByType(CdOrderProcessor.class).order();
        assert OrderProcessor.postConstructCalled;
    }

    @Test
    @SpecAssertion(section = "4.2", id = "bac")
    public void testIndirectSubClassInheritsPostConstructOnSuperclass() {
        OrderProcessor.postConstructCalled = false;
        assert getBeans(IndirectOrderProcessor.class).size() == 1;
        getInstanceByType(IndirectOrderProcessor.class).order();
        assert OrderProcessor.postConstructCalled;
    }

    @Test
    @SpecAssertion(section = "4.2", id = "bba")
    public void testSubClassInheritsPreDestroyOnSuperclass() {
        OrderProcessor.preDestroyCalled = false;
        assert getBeans(CdOrderProcessor.class).size() == 1;
        Bean<CdOrderProcessor> bean = getBeans(CdOrderProcessor.class).iterator().next();
        CreationalContext<CdOrderProcessor> creationalContext = getCurrentManager().createCreationalContext(bean);
        CdOrderProcessor instance = getInstanceByType(CdOrderProcessor.class);
        bean.destroy(instance, creationalContext);
        assert OrderProcessor.preDestroyCalled;
    }

    @Test
    @SpecAssertion(section = "4.2", id = "bbc")
    public void testIndirectSubClassInheritsPreDestroyOnSuperclass() {
        OrderProcessor.preDestroyCalled = false;
        assert getBeans(IndirectOrderProcessor.class).size() == 1;
        Bean<IndirectOrderProcessor> bean = getBeans(IndirectOrderProcessor.class).iterator().next();
        CreationalContext<IndirectOrderProcessor> creationalContext = getCurrentManager().createCreationalContext(bean);
        IndirectOrderProcessor instance = getInstanceByType(IndirectOrderProcessor.class);
        bean.destroy(instance, creationalContext);
        assert OrderProcessor.preDestroyCalled;
    }

    @Test
    @SpecAssertion(section = "4.2", id = "baa")
    public void testSubClassDoesNotInheritPostConstructOnSuperclassBlockedByIntermediateClass() {
        assert getBeans(NovelOrderProcessor.class).size() == 1;
        OrderProcessor.postConstructCalled = false;
        getInstanceByType(NovelOrderProcessor.class).order();
        assert !OrderProcessor.postConstructCalled;
    }

    @Test
    @SpecAssertion(section = "4.2", id = "bba")
    public void testSubClassDoesNotInheritPreDestroyConstructOnSuperclassBlockedByIntermediateClass() {
        OrderProcessor.preDestroyCalled = false;
        assert getBeans(NovelOrderProcessor.class).size() == 1;
        Bean<NovelOrderProcessor> bean = getBeans(NovelOrderProcessor.class).iterator().next();
        CreationalContext<NovelOrderProcessor> creationalContext = getCurrentManager().createCreationalContext(bean);
        NovelOrderProcessor instance = getInstanceByType(NovelOrderProcessor.class);
        bean.destroy(instance, creationalContext);
        assert !OrderProcessor.preDestroyCalled;
    }

    @Test(expectedExceptions = CreationException.class)
    @SpecAssertion(section = "6.1", id = "a0")
    public void testCreationExceptionWrapsCheckedExceptionThrownFromCreate() {
        assert getBeans(Lorry_Broken.class).size() == 1;
        getInstanceByType(Lorry_Broken.class);
    }

    @Test(expectedExceptions = FooException.class)
    @SpecAssertion(section = "6.1", id = "a0")
    public void testUncheckedExceptionThrownFromCreateNotWrapped() {
        assert getBeans(Van_Broken.class).size() == 1;
        getInstanceByType(Van_Broken.class);
    }

}
