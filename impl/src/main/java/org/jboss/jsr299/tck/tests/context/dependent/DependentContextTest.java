package org.jboss.jsr299.tck.tests.context.dependent;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.impl.MockCreationalContext;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091101")
public class DependentContextTest extends AbstractJSR299Test
{
   
   private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame> () {};
   private static final Annotation PET_LITERAL = new AnnotationLiteral<Pet> () {};
   
   @Test(groups = { "contexts", "injection" })
   @SpecAssertions({
      @SpecAssertion(section = "6.4", id = "a"),
      @SpecAssertion(section = "6.4.1", id = "ga")
   })
   public void testInstanceNotSharedBetweenInjectionPoints()
   {
      Set<Bean<Fox>> foxBeans = getBeans(Fox.class);
      assert foxBeans.size() == 1;
      Set<Bean<FoxRun>> foxRunBeans = getBeans(FoxRun.class);
      assert foxRunBeans.size() == 1;
      Bean<FoxRun> foxRunBean = foxRunBeans.iterator().next();
      CreationalContext<FoxRun> creationalContext = getCurrentManager().createCreationalContext(foxRunBean);
      FoxRun foxRun = foxRunBean.create(creationalContext);
      assert !foxRun.fox.equals(foxRun.anotherFox);
   }
   
   @Test(groups = { "contexts", "injection" })
   @SpecAssertions({
      @SpecAssertion(section = "6.4.1", id = "ga"),
      @SpecAssertion(section = "6.4.1", id = "gb"),
      @SpecAssertion(section = "6.4.1", id = "gc")
   })
   public void testDependentBeanIsDependentObjectOfBeanInjectedInto()
   {
      FoxFarm foxFarm = getInstanceByType(FoxFarm.class);
      FoxHole foxHole = getInstanceByType(FoxHole.class);
      
      assert !foxFarm.fox.equals(foxHole.fox);
      assert !foxFarm.fox.equals(foxFarm.constructorFox);
      
      assert !foxFarm.constructorFox.equals(foxHole.initializerFox);
      assert !foxHole.fox.equals(foxHole.initializerFox);
   }

   @Test(groups = { "contexts", "el" })
   @SpecAssertion(section = "6.4", id = "ca")
   public void testInstanceUsedForElEvaluationNotShared() throws Exception
   {
      Set<Bean<Fox>> foxBeans = getBeans(Fox.class);
      assert foxBeans.size() == 1;

      Fox fox1 = getCurrentConfiguration().getEl().evaluateValueExpression("#{fox}", Fox.class);
      Fox fox2 = getCurrentConfiguration().getEl().evaluateValueExpression("#{fox}", Fox.class);
      assert !fox1.equals(fox2);
   }

   @Test(groups = { "contexts", "producerMethod" })
   @SpecAssertion(section = "6.4", id = "da")
   public void testInstanceUsedForProducerMethodNotShared() throws Exception
   {
      Bean<Tarantula> tarantulaBean = getBeans(Tarantula.class, PET_LITERAL).iterator().next();
      
      CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantulaBean);
      Tarantula tarantula = tarantulaBean.create(creationalContext);
      Tarantula tarantula2 = tarantulaBean.create(creationalContext);
      assert tarantula != null;
      assert tarantula2 != null;
      assert tarantula != tarantula2;
   }

   @Test(groups = { "contexts", "producerMethod" })
   @SpecAssertion(section = "6.4", id = "db")
   public void testInstanceUsedForProducerFieldNotShared() throws Exception
   {
      Bean<Tarantula> tarantulaBean = getBeans(Tarantula.class, TAME_LITERAL).iterator().next();
      CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantulaBean);
      Tarantula tarantula = tarantulaBean.create(creationalContext);
      Tarantula tarantula2 = tarantulaBean.create(creationalContext);
      assert tarantula != null;
      assert tarantula2 != null;
      assert tarantula != tarantula2;
   }

   @Test(groups = { "contexts", "disposalMethod" })
   @SpecAssertion(section = "6.4", id = "dc")
   public void testInstanceUsedForDisposalMethodNotShared()
   {
      SpiderProducer spiderProducer = getInstanceByType(SpiderProducer.class);
      Bean<Tarantula> tarantulaBean = getUniqueBean(Tarantula.class, PET_LITERAL);
      CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantulaBean);
      Tarantula tarantula = tarantulaBean.create(creationalContext);
      assert tarantula != null;
      tarantulaBean.destroy(tarantula, creationalContext);
      assert SpiderProducer.getInstanceUsedForDisposal() != null;
      assert SpiderProducer.getInstanceUsedForDisposal() != spiderProducer;
      SpiderProducer.reset();
   }

   @Test(groups = { "contexts", "observerMethod" })
   @SpecAssertion(section = "6.4", id = "dd")
   public void testInstanceUsedForObserverMethodNotShared()
   {
      HorseStable firstStableInstance = getInstanceByType(HorseStable.class);
      getCurrentManager().fireEvent(new HorseInStableEvent());
      assert HorseStable.getInstanceThatObservedEvent() != null;
      assert HorseStable.getInstanceThatObservedEvent() != firstStableInstance;
   }

   @Test(groups = "contexts")
   @SpecAssertion(section = "6.4", id = "e")
   public void testContextGetWithCreationalContextReturnsNewInstance()
   {
      Set<Bean<Fox>> foxBeans = getBeans(Fox.class);
      assert foxBeans.size() == 1;
      Bean<Fox> foxBean = foxBeans.iterator().next();
      Context context = getCurrentManager().getContext(Dependent.class);
      assert context.get(foxBean, new MockCreationalContext<Fox>()) != null;
      assert context.get(foxBean, new MockCreationalContext<Fox>()) instanceof Fox;
   }

   @Test(groups = "contexts")
   @SpecAssertion(section = "6.4", id = "f")
   public void testContextGetWithCreateFalseReturnsNull()
   {
      Set<Bean<Fox>> foxBeans = getBeans(Fox.class);
      assert foxBeans.size() == 1;
      Bean<Fox> foxBean = foxBeans.iterator().next();
      Context context = getCurrentManager().getContext(Dependent.class);
      assert context.get(foxBean, null) == null;
   }
      
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.2", id = "ab")
   public void testContextScopeType()
   {
      assert getCurrentManager().getContext(Dependent.class).getScope().equals(Dependent.class);
   }   

   @Test(groups = { "contexts" })
   @SpecAssertions({
      @SpecAssertion(section = "6.2", id = "ha"),
      @SpecAssertion(section = "6.4", id = "g")
   })
   public void testContextIsActive()
   {
      assert getCurrentManager().getContext(Dependent.class).isActive();
   }

   @Test(groups = { "contexts", "producerMethod" })
   @SpecAssertions({
      @SpecAssertion(section = "6.2", id = "ha"),
      @SpecAssertion(section = "6.4", id = "g") // Dependent context is now always active
   })
   public void testContextIsActiveWhenInvokingProducerMethod()
   {
      SpiderProducer.reset();
      Tarantula tarantula = getInstanceByType(Tarantula.class,PET_LITERAL);
      assert tarantula != null;
      assert SpiderProducer.isDependentContextActive();
      SpiderProducer.reset();
   }

   @Test(groups = { "contexts", "producerField"})
   @SpecAssertion(section = "6.4", id = "g") // Dependent context is now always active
   public void testContextIsActiveWhenInvokingProducerField()
   {
      // Reset test class
      Tarantula.reset();
      getInstanceByType(Tarantula.class,TAME_LITERAL);
      assert Tarantula.isDependentContextActive();
      SpiderProducer.reset();
   }

   @Test(groups = { "contexts", "disposalMethod" })
   @SpecAssertions({
      @SpecAssertion(section = "6.4", id = "g"),
      @SpecAssertion(section = "11.1", id = "aa")
   })
   public void testContextIsActiveWhenInvokingDisposalMethod()
   {
      Bean<Tarantula> tarantulaBean = getBeans(Tarantula.class, PET_LITERAL).iterator().next();
      CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantulaBean);
      Tarantula tarantula = tarantulaBean.create(creationalContext);
      assert tarantula != null;
      SpiderProducer.reset();
      tarantulaBean.destroy(tarantula, creationalContext);
      assert SpiderProducer.isDependentContextActive();
      SpiderProducer.reset();
   }

   @Test(groups = { "contexts", "observerMethod" })
   @SpecAssertion(section = "6.4", id = "g") // Dependent context is now always active
   public void testContextIsActiveWhenCreatingObserverMethodInstance()
   {
      getCurrentManager().fireEvent(new HorseInStableEvent());
      assert HorseStable.isDependentContextActive();
   }

   @Test(groups = { "contexts", "el" })
   @SpecAssertion(section = "6.4", id = "g") // Dependent context is now always active
   public void testContextIsActiveWhenEvaluatingElExpression()
   {
      String foxName = getCurrentConfiguration().getEl().evaluateMethodExpression("#{sensitiveFox.getName}", String.class, new Class[0], new Object[0]);
      assert foxName != null;
      assert SensitiveFox.isDependentContextActiveDuringEval();
   }

   @Test(groups = { "contexts", "beanLifecycle" })
   @SpecAssertion(section = "6.4", id = "g") // Dependent context is now always active
   public void testContextIsActiveDuringBeanCreation()
   {
      SensitiveFox fox1 = getInstanceByType(SensitiveFox.class);
      assert fox1 != null;
      assert fox1.isDependentContextActiveDuringCreate();
   }

   @Test(groups = { "contexts", "injection" })
   @SpecAssertion(section = "6.4", id = "g") // Dependent context is now always active
   public void testContextIsActiveDuringInjection()
   {
      Bean<FoxRun> foxRunBean = getBeans(FoxRun.class).iterator().next();
      FoxRun foxRun = foxRunBean.create(new MockCreationalContext<FoxRun>());
      assert foxRun.fox != null;
   }

   @Test(groups = { "contexts", "beanDestruction"})
   @SpecAssertions({
      @SpecAssertion(section = "6.4.2", id = "aaaa"),
      @SpecAssertion(section = "6.4", id = "b")
   })
   public void testDestroyingSimpleParentDestroysDependents()
   {
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
   
   @Test(groups = { "contexts", "beanDestruction"})
   @SpecAssertions({
      @SpecAssertion(section = "6.1.1", id = "e")
   })
   public void testCallingCreationalContextReleaseDestroysDependents()
   {
      assert getBeans(Farm.class).size() == 1;
      Bean<Farm> farmBean = getBeans(Farm.class).iterator().next();
      CreationalContext<Farm> creationalContext = getCurrentManager().createCreationalContext(farmBean);
      Farm farm = farmBean.create(creationalContext);
      farm.open();
      Stable.destroyed = false;
      Horse.destroyed = false;
      creationalContext.release();
      assert Stable.destroyed;
      assert Horse.destroyed;
   }
   
   @Test(groups = { "contexts", "beanDestruction"})
   @SpecAssertions({
      @SpecAssertion(section = "6.4.2", id = "aaaa"),
      @SpecAssertion(section = "6.4", id = "b")
   })
   public void testDestroyingManagedParentDestroysDependentsOfSameBean()
   {
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

   @Test(groups = { "contexts", "el"})
   @SpecAssertion(section = "6.4.2", id = "eee")
   public void testDependentsDestroyedWhenElEvaluationCompletes() throws Exception
   {
      // Reset test class
      Fox.reset();
      FoxRun.setDestroyed(false);

      getCurrentConfiguration().getEl().evaluateValueExpression("#{foxRun}", FoxRun.class);
      assert FoxRun.isDestroyed();
      assert Fox.isDestroyed();
   }

   @Test(groups = { "contexts", "producerMethod" })
   @SpecAssertions({
      @SpecAssertion(section = "6.4.2", id = "ddd"),
      @SpecAssertion(section = "6.4.1", id="h")
   })
   
   public void testDependentsDestroyedWhenProducerMethodCompletes()
   {
      // Reset the test class
      SpiderProducer.reset();
      Tarantula.reset();
      Tarantula spiderInstance = getInstanceByType(Tarantula.class, PET_LITERAL);
      spiderInstance.ping();
      assert SpiderProducer.isDestroyed();
      assert Tarantula.isDestroyed();
      SpiderProducer.reset();
   }

   @Test(groups = { "contexts", "producerField" })
   @SpecAssertion(section = "6.4.2", id = "dde")
   public void testDependentsDestroyedWhenProducerFieldCompletes()
   {
      // Reset the test class
      OtherSpiderProducer.setDestroyed(false);

      Tarantula spiderInstance = getInstanceByType(Tarantula.class,TAME_LITERAL);
      assert spiderInstance != null;
      assert OtherSpiderProducer.isDestroyed();
   }

   @Test(groups = { "contexts", "disposalMethod" })
   @SpecAssertions({
      @SpecAssertion(section = "6.4.2", id = "ddf"),
      @SpecAssertion(section = "6.4.2", id ="ccc")
   })
   public void testDependentsDestroyedWhenDisposerMethodCompletes()
   {
      Bean<Tarantula> tarantulaBean = getBeans(Tarantula.class, PET_LITERAL).iterator().next();
      CreationalContext<Tarantula> creationalContext = getCurrentManager().createCreationalContext(tarantulaBean);
      Tarantula tarantula = tarantulaBean.create(creationalContext);
      assert tarantula != null;

      // Reset test class state
      SpiderProducer.reset();
      Fox.reset();

      tarantulaBean.destroy(tarantula, creationalContext);
      assert SpiderProducer.isDestroyed();
      assert Fox.isDestroyed();
      SpiderProducer.reset();
   }

   @Test(groups = { "contexts", "observerMethod" })
   @SpecAssertions({
      @SpecAssertion(section = "6.4.2", id = "ddg"),
      @SpecAssertion(section = "6.4.2", id = "ccd")
   })
   public void testDependentsDestroyedWhenObserverMethodEvaluationCompletes()
   {
      // Reset test class state...
      HorseStable.reset();
      Fox.reset();
      getCurrentManager().fireEvent(new HorseInStableEvent());
      assert HorseStable.getInstanceThatObservedEvent() != null;
      assert HorseStable.isDestroyed();
      assert Fox.isDestroyed();
   }
   
   @Test(groups = { "contexts" })
   @SpecAssertion(section = "6.4.1", id = "ab")
   public void testDependentScopedDecoratorsAreDependentObjectsOfBean()
   {
      Bean<Interior> roomBean = getBeans(Interior.class, new RoomBinding()).iterator().next();
      
      CreationalContext<Interior> roomCreationalContext = getCurrentManager().createCreationalContext(roomBean);
      
      Interior room = roomBean.create(roomCreationalContext);
      
      InteriorDecorator.reset();
      
      room.foo();
      
      assert InteriorDecorator.getInstances().size() == 1;
      
      roomBean.destroy(room, roomCreationalContext);
      assert InteriorDecorator.isDestroyed();
   }
   
   @Test
   @SpecAssertion(section = "6.4.1", id = "aa")
   public void testDependentScopedInterceptorsAreDependentObjectsOfBean()
   {
      TransactionalInterceptor.destroyed = false;
      TransactionalInterceptor.intercepted = false;
      
      Bean<AccountTransaction> bean = getBeans(AccountTransaction.class).iterator().next();
      CreationalContext<AccountTransaction> ctx = getCurrentManager().createCreationalContext(bean);
            
      AccountTransaction trans = bean.create(ctx);
      trans.execute();
      
      assert TransactionalInterceptor.intercepted;
      
      bean.destroy(trans, ctx);
      
      assert TransactionalInterceptor.destroyed;      
   }
}
