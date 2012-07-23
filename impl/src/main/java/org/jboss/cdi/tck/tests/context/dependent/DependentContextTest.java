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
package org.jboss.cdi.tck.tests.context.dependent;

import static org.jboss.cdi.tck.TestGroups.CONTEXTS;
import static org.jboss.cdi.tck.TestGroups.DISPOSAL;
import static org.jboss.cdi.tck.TestGroups.EL;
import static org.jboss.cdi.tck.TestGroups.INJECTION;
import static org.jboss.cdi.tck.TestGroups.LIFECYCLE;
import static org.jboss.cdi.tck.TestGroups.OBSERVER_METHOD;
import static org.jboss.cdi.tck.TestGroups.PRODUCER_FIELD;
import static org.jboss.cdi.tck.TestGroups.PRODUCER_METHOD;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.impl.MockCreationalContext;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class DependentContextTest extends AbstractTest {

    private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {
    };
    private static final Annotation PET_LITERAL = new AnnotationLiteral<Pet>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DependentContextTest.class).withBeansXml("beans.xml").build();
    }

    @Test(groups = { CONTEXTS, INJECTION })
    @SpecAssertions({ @SpecAssertion(section = "6.4", id = "a"), @SpecAssertion(section = "6.4.1", id = "ga") })
    public void testInstanceNotSharedBetweenInjectionPoints() {
        Set<Bean<Fox>> foxBeans = getBeans(Fox.class);
        assert foxBeans.size() == 1;
        Set<Bean<FoxRun>> foxRunBeans = getBeans(FoxRun.class);
        assert foxRunBeans.size() == 1;
        Bean<FoxRun> foxRunBean = foxRunBeans.iterator().next();
        CreationalContext<FoxRun> creationalContext = getCurrentManager().createCreationalContext(foxRunBean);
        FoxRun foxRun = foxRunBean.create(creationalContext);
        assert !foxRun.fox.equals(foxRun.anotherFox);
        assert !foxRun.fox.equals(foxRun.petFox);
        assert !foxRun.anotherFox.equals(foxRun.petFox);
    }

    @Test(groups = { CONTEXTS, INJECTION })
    @SpecAssertions({ @SpecAssertion(section = "6.4.1", id = "ga"), @SpecAssertion(section = "6.4.1", id = "gb"),
            @SpecAssertion(section = "6.4.1", id = "gc") })
    public void testDependentBeanIsDependentObjectOfBeanInjectedInto() {
        FoxFarm foxFarm = getInstanceByType(FoxFarm.class);
        FoxHole foxHole = getInstanceByType(FoxHole.class);

        assert !foxFarm.fox.equals(foxHole.fox);
        assert !foxFarm.fox.equals(foxFarm.constructorFox);

        assert !foxFarm.constructorFox.equals(foxHole.initializerFox);
        assert !foxHole.fox.equals(foxHole.initializerFox);
    }

    @Test(groups = { CONTEXTS, EL })
    @SpecAssertion(section = "6.4", id = "ca")
    public void testInstanceUsedForElEvaluationNotShared() throws Exception {
        Set<Bean<Fox>> foxBeans = getBeans(Fox.class);
        assert foxBeans.size() == 1;

        Fox fox1 = getCurrentConfiguration().getEl().evaluateValueExpression(getCurrentManager(), "#{fox}", Fox.class);
        Fox fox2 = getCurrentConfiguration().getEl().evaluateValueExpression(getCurrentManager(), "#{fox}", Fox.class);
        assert !fox1.equals(fox2);
    }

    @Test(groups = { CONTEXTS, PRODUCER_METHOD })
    @SpecAssertion(section = "6.4", id = "da")
    public void testInstanceUsedForProducerMethodNotShared() throws Exception {

        SpiderProducer.reset();
        getInstanceByType(Tarantula.class, PET_LITERAL);
        Integer firstInstanceHash = SpiderProducer.getInstanceUsedForProducerHashcode();
        SpiderProducer.reset();
        getInstanceByType(Tarantula.class, PET_LITERAL);
        Integer secondInstanceHash = SpiderProducer.getInstanceUsedForProducerHashcode();

        assertFalse(firstInstanceHash.equals(secondInstanceHash));
    }

    @Test(groups = { CONTEXTS, PRODUCER_METHOD })
    @SpecAssertion(section = "6.4", id = "db")
    public void testInstanceUsedForProducerFieldNotShared() throws Exception {

        Tarantula firstIntance = getInstanceByType(Tarantula.class, TAME_LITERAL);
        Tarantula secondIntance = getInstanceByType(Tarantula.class, TAME_LITERAL);

        assertNotNull(firstIntance.getProducerInstanceHashcode());
        assertNotNull(secondIntance.getProducerInstanceHashcode());
        assertNotEquals(firstIntance.getProducerInstanceHashcode(), secondIntance.getProducerInstanceHashcode());
    }

    @Test(groups = { CONTEXTS, DISPOSAL })
    @SpecAssertions({ @SpecAssertion(section = "6.4", id = "dc"), @SpecAssertion(section = "6.4", id = "dg") })
    public void testInstanceUsedForDisposalMethodNotShared() {

        Integer firstFoxHash = getInstanceByType(Fox.class).hashCode();

        SpiderProducer.reset();
        SpiderProducer spiderProducer = getInstanceByType(SpiderProducer.class);
        Bean<Tarantula> tarantulaBean = getUniqueBean(Tarantula.class, PET_LITERAL);
        CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantulaBean);
        Tarantula tarantula = tarantulaBean.create(creationalContext);
        assert tarantula != null;

        tarantulaBean.destroy(tarantula, creationalContext);
        Integer secondFoxHash = SpiderProducer.getFoxUsedForDisposalHashcode();

        assert SpiderProducer.getInstanceUsedForDisposalHashcode() != null;
        assert !SpiderProducer.getInstanceUsedForDisposalHashcode().equals(spiderProducer.hashCode());

        CreationalContext<Tarantula> nextCreationalContext = getCurrentManager().createCreationalContext(tarantulaBean);
        Tarantula nextTarantula = tarantulaBean.create(nextCreationalContext);
        assert nextTarantula != null;

        tarantulaBean.destroy(nextTarantula, creationalContext);
        Integer thirdFoxHash = SpiderProducer.getFoxUsedForDisposalHashcode();

        assertNotEquals(firstFoxHash, secondFoxHash);
        assertNotEquals(firstFoxHash, thirdFoxHash);
        assertNotEquals(thirdFoxHash, secondFoxHash);

        SpiderProducer.reset();
    }

    @Test(groups = { CONTEXTS, OBSERVER_METHOD })
    @SpecAssertions({ @SpecAssertion(section = "6.4", id = "dd"), @SpecAssertion(section = "6.4", id = "dg") })
    public void testInstanceUsedForObserverMethodNotShared() {

        HorseStable.reset();
        HorseStable firstStableInstance = getInstanceByType(HorseStable.class);

        getCurrentManager().fireEvent(new HorseInStableEvent());
        Integer firstFoxHash = HorseStable.getFoxUsedForObservedEventHashcode();
        Integer firstObserverHash = HorseStable.getInstanceThatObservedEventHashcode();

        getCurrentManager().fireEvent(new HorseInStableEvent());
        Integer secondFoxHash = HorseStable.getFoxUsedForObservedEventHashcode();
        Integer secondObserverHash = HorseStable.getInstanceThatObservedEventHashcode();

        assertNotNull(firstObserverHash);
        assertNotNull(secondObserverHash);
        assertNotNull(firstFoxHash);
        assertNotNull(secondFoxHash);
        assertNotEquals(firstStableInstance.hashCode(), firstObserverHash);
        assertNotEquals(firstStableInstance.hashCode(), secondObserverHash);
        assertNotEquals(firstObserverHash, secondObserverHash);
        assertNotEquals(firstFoxHash, secondFoxHash);
    }

    @Test(groups = CONTEXTS)
    @SpecAssertion(section = "6.4", id = "e")
    public void testContextGetWithCreationalContextReturnsNewInstance() {
        Set<Bean<Fox>> foxBeans = getBeans(Fox.class);
        assert foxBeans.size() == 1;
        Bean<Fox> foxBean = foxBeans.iterator().next();
        Context context = getCurrentManager().getContext(Dependent.class);
        assert context.get(foxBean, new MockCreationalContext<Fox>()) != null;
        assert context.get(foxBean, new MockCreationalContext<Fox>()) instanceof Fox;
    }

    @Test(groups = CONTEXTS)
    @SpecAssertion(section = "6.4", id = "f")
    public void testContextGetWithCreateFalseReturnsNull() {
        Set<Bean<Fox>> foxBeans = getBeans(Fox.class);
        assert foxBeans.size() == 1;
        Bean<Fox> foxBean = foxBeans.iterator().next();
        Context context = getCurrentManager().getContext(Dependent.class);
        assert context.get(foxBean, null) == null;
    }

    @Test(groups = CONTEXTS)
    @SpecAssertion(section = "6.2", id = "ab")
    public void testContextScopeType() {
        assert getCurrentManager().getContext(Dependent.class).getScope().equals(Dependent.class);
    }

    @Test(groups = CONTEXTS)
    @SpecAssertions({ @SpecAssertion(section = "6.2", id = "ha"), @SpecAssertion(section = "6.4", id = "g") })
    public void testContextIsActive() {
        assert getCurrentManager().getContext(Dependent.class).isActive();
    }

    @Test(groups = { CONTEXTS, PRODUCER_METHOD })
    @SpecAssertions({ @SpecAssertion(section = "6.2", id = "ha"), @SpecAssertion(section = "6.4", id = "g") // Dependent context
                                                                                                            // is now always
                                                                                                            // active
    })
    public void testContextIsActiveWhenInvokingProducerMethod() {
        SpiderProducer.reset();
        Tarantula tarantula = getInstanceByType(Tarantula.class, PET_LITERAL);
        assert tarantula != null;
        assert SpiderProducer.isDependentContextActive();
        SpiderProducer.reset();
    }

    @Test(groups = { CONTEXTS, PRODUCER_FIELD })
    @SpecAssertion(section = "6.4", id = "g")
    // Dependent context is now always active
    public void testContextIsActiveWhenInvokingProducerField() {
        // Reset test class
        Tarantula.reset();
        getInstanceByType(Tarantula.class, TAME_LITERAL);
        assert Tarantula.isDependentContextActive();
        SpiderProducer.reset();
    }

    @Test(groups = { CONTEXTS, DISPOSAL })
    @SpecAssertions({ @SpecAssertion(section = "6.4", id = "g"), @SpecAssertion(section = "11.1", id = "aa") })
    public void testContextIsActiveWhenInvokingDisposalMethod() {
        Bean<Tarantula> tarantulaBean = getBeans(Tarantula.class, PET_LITERAL).iterator().next();
        CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantulaBean);
        Tarantula tarantula = tarantulaBean.create(creationalContext);
        assert tarantula != null;
        SpiderProducer.reset();
        tarantulaBean.destroy(tarantula, creationalContext);
        assert SpiderProducer.isDependentContextActive();
        SpiderProducer.reset();
    }

    @Test(groups = { CONTEXTS, OBSERVER_METHOD })
    @SpecAssertion(section = "6.4", id = "g")
    // Dependent context is now always active
    public void testContextIsActiveWhenCreatingObserverMethodInstance() {
        getCurrentManager().fireEvent(new HorseInStableEvent());
        assert HorseStable.isDependentContextActive();
    }

    @Test(groups = { CONTEXTS, EL })
    @SpecAssertion(section = "6.4", id = "g")
    // Dependent context is now always active
    public void testContextIsActiveWhenEvaluatingElExpression() {
        String foxName = getCurrentConfiguration().getEl().evaluateMethodExpression(getCurrentManager(),
                "#{sensitiveFox.getName}", String.class, new Class[0], new Object[0]);
        assert foxName != null;
        assert SensitiveFox.isDependentContextActiveDuringEval();
    }

    @Test(groups = { CONTEXTS, LIFECYCLE })
    @SpecAssertion(section = "6.4", id = "g")
    // Dependent context is now always active
    public void testContextIsActiveDuringBeanCreation() {
        SensitiveFox fox1 = getInstanceByType(SensitiveFox.class);
        assert fox1 != null;
        assert fox1.isDependentContextActiveDuringCreate();
    }

    @Test(groups = { CONTEXTS, INJECTION })
    @SpecAssertion(section = "6.4", id = "g")
    // Dependent context is now always active
    public void testContextIsActiveDuringInjection() {
        Bean<FoxRun> foxRunBean = getBeans(FoxRun.class).iterator().next();
        FoxRun foxRun = foxRunBean.create(new MockCreationalContext<FoxRun>());
        assert foxRun.fox != null;
    }

    @Test(groups = { CONTEXTS, LIFECYCLE })
    @SpecAssertions({ @SpecAssertion(section = "6.4.2", id = "aaaa"), @SpecAssertion(section = "6.4", id = "b") })
    public void testDestroyingSimpleParentDestroysDependents() {
        assert getBeans(Farm.class).size() == 1;
        Bean<Farm> farmBean = getBeans(Farm.class).iterator().next();
        CreationalContext<Farm> creationalContext = getCurrentManager().createCreationalContext(farmBean);
        Farm farm = farmBean.create(creationalContext);
        farm.open();
        Stable.destroyed = false;
        Horse.destroyed = false;
        farmBean.destroy(farm, creationalContext);
        assert Stable.destroyed;
        assert Horse.destroyed;
    }

    @Test(groups = { CONTEXTS, LIFECYCLE })
    @SpecAssertions({ @SpecAssertion(section = "6.1.1", id = "e") })
    public void testCallingCreationalContextReleaseDestroysDependents() {
        assert getBeans(Farm.class).size() == 1;

        // Unmanaged instance
        Bean<Farm> farmBean = getBeans(Farm.class).iterator().next();
        CreationalContext<Farm> creationalContext = getCurrentManager().createCreationalContext(farmBean);
        Farm farm = farmBean.create(creationalContext);
        farm.open();
        Stable.destroyed = false;
        Horse.destroyed = false;
        creationalContext.release();
        assert Stable.destroyed;
        assert Horse.destroyed;

        // Contextual reference
        creationalContext = getCurrentManager().createCreationalContext(farmBean);
        farm = (Farm) getCurrentManager().getReference(farmBean, Farm.class, creationalContext);
        farm.open();
        Stable.destroyed = false;
        Horse.destroyed = false;
        creationalContext.release();
        assert Stable.destroyed;
        assert Horse.destroyed;
    }

    @Test(groups = { CONTEXTS, LIFECYCLE })
    @SpecAssertions({ @SpecAssertion(section = "6.4.2", id = "aaaa"), @SpecAssertion(section = "6.4", id = "b") })
    public void testDestroyingManagedParentDestroysDependentsOfSameBean() {
        // Reset test class
        Fox.reset();

        assert getCurrentManager().getBeans(FoxRun.class).size() == 1;
        Bean<FoxRun> bean = getBeans(FoxRun.class).iterator().next();
        CreationalContext<FoxRun> creationalContext = getCurrentManager().createCreationalContext(bean);
        FoxRun instance = bean.create(creationalContext);
        assert instance.fox != instance.anotherFox;
        bean.destroy(instance, creationalContext);
        assert Fox.isDestroyed();
        assert Fox.getDestroyCount() == 2;
    }

    @Test(groups = { CONTEXTS, EL })
    @SpecAssertion(section = "6.4.2", id = "eee")
    public void testDependentsDestroyedWhenElEvaluationCompletes() throws Exception {
        // Reset test class
        Fox.reset();
        FoxRun.setDestroyed(false);

        getCurrentConfiguration().getEl().evaluateValueExpression(getCurrentManager(), "#{foxRun}", FoxRun.class);
        assert FoxRun.isDestroyed();
        assert Fox.isDestroyed();
    }

    @Test(groups = { CONTEXTS, PRODUCER_METHOD })
    @SpecAssertions({ @SpecAssertion(section = "6.4.2", id = "ddd"), @SpecAssertion(section = "6.4.1", id = "h"),
            @SpecAssertion(section = "5.5.4", id = "f") })
    public void testDependentsDestroyedWhenProducerMethodCompletes() {
        // Reset the test classes
        SpiderProducer.reset();
        Tarantula.reset();
        DomesticationKit.reset();

        Bean<Tarantula> tarantulaBean = getUniqueBean(Tarantula.class, PET_LITERAL);
        CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantulaBean);
        Tarantula tarantula = (Tarantula) getCurrentManager().getReference(tarantulaBean, Tarantula.class, creationalContext);
        tarantula.ping();
        // contextual instance created to receive a producer method invocation
        // is destroyed when the invocation completes
        assert SpiderProducer.isDestroyed();
        // DomesticationKit instance is a dependent object of the Tarantula instance
        tarantulaBean.destroy(tarantula, creationalContext);
        assert DomesticationKit.isDestroyed();
        SpiderProducer.reset();
    }

    @Test(groups = { CONTEXTS, PRODUCER_FIELD })
    @SpecAssertion(section = "6.4.2", id = "dde")
    public void testDependentsDestroyedWhenProducerFieldCompletes() {
        // Reset the test class
        OtherSpiderProducer.setDestroyed(false);

        Tarantula spiderInstance = getInstanceByType(Tarantula.class, TAME_LITERAL);
        assert spiderInstance != null;
        assert OtherSpiderProducer.isDestroyed();
    }

    @Test(groups = { CONTEXTS, DISPOSAL })
    @SpecAssertions({ @SpecAssertion(section = "6.4.2", id = "ddf"), @SpecAssertion(section = "6.4.2", id = "ccc"),
            @SpecAssertion(section = "5.5.4", id = "d"), @SpecAssertion(section = "5.5.4", id = "f") })
    public void testDependentsDestroyedWhenDisposerMethodCompletes() {
        Bean<Tarantula> tarantulaBean = getBeans(Tarantula.class, PET_LITERAL).iterator().next();
        CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantulaBean);
        Tarantula tarantula = tarantulaBean.create(creationalContext);
        assertNotNull(tarantula);

        // Reset test class state
        SpiderProducer.reset();
        Fox.reset();

        tarantulaBean.destroy(tarantula, creationalContext);
        assertTrue(SpiderProducer.isDestroyed());
        assertNotNull(SpiderProducer.getFoxUsedForDisposalHashcode());
        assertTrue(Fox.isDestroyed());

        SpiderProducer.reset();
        Fox.reset();
    }

    @Test(groups = { CONTEXTS, OBSERVER_METHOD })
    @SpecAssertions({ @SpecAssertion(section = "6.4.2", id = "ddg"), @SpecAssertion(section = "6.4.2", id = "ccd"),
            @SpecAssertion(section = "5.5.6", id = "d") })
    public void testDependentsDestroyedWhenObserverMethodEvaluationCompletes() {
        // Reset test class state...
        HorseStable.reset();
        Fox.reset();
        getCurrentManager().fireEvent(new HorseInStableEvent());
        assert HorseStable.getInstanceThatObservedEventHashcode() != null;
        assert HorseStable.isDestroyed();
        assert Fox.isDestroyed();
    }

    @Test(groups = { CONTEXTS })
    @SpecAssertion(section = "6.4.1", id = "ab")
    public void testDependentScopedDecoratorsAreDependentObjectsOfBean() {
        Bean<Interior> roomBean = getBeans(Interior.class, new RoomBinding()).iterator().next();

        CreationalContext<Interior> roomCreationalContext = getCurrentManager().createCreationalContext(roomBean);
        Interior room = (Interior) getCurrentManager().getReference(roomBean, Interior.class, roomCreationalContext);

        InteriorDecorator.reset();

        room.foo();

        assert InteriorDecorator.getInstances().size() == 1;
        roomCreationalContext.release();
        assert InteriorDecorator.isDestroyed();
    }

    @Test
    @SpecAssertion(section = "6.4.1", id = "aa")
    public void testDependentScopedInterceptorsAreDependentObjectsOfBean() {
        TransactionalInterceptor.destroyed = false;
        TransactionalInterceptor.intercepted = false;

        Bean<AccountTransaction> bean = getBeans(AccountTransaction.class).iterator().next();
        CreationalContext<AccountTransaction> ctx = getCurrentManager().createCreationalContext(bean);

        AccountTransaction trans = (AccountTransaction) getCurrentManager().getReference(bean, AccountTransaction.class, ctx);
        trans.execute();

        assert TransactionalInterceptor.intercepted;

        ctx.release();

        assert TransactionalInterceptor.destroyed;
    }
}
