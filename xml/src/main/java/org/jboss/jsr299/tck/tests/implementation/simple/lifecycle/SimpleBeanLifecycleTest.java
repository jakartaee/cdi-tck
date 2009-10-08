package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import java.lang.annotation.Annotation;

import javax.context.Context;
import javax.context.Contextual;
import javax.context.CreationalContext;
import javax.context.Dependent;
import javax.context.RequestScoped;
import javax.inject.AnnotationLiteral;
import javax.inject.CreationException;
import javax.enterprise.inject.spi.Bean;

import org.hibernate.tck.annotations.SpecAssertion;
import org.hibernate.tck.annotations.SpecAssertions;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

/**
 * 
 * Spec version: PRD2
 * 
 */
@Artifact
@BeansXml("beans.xml")
public class SimpleBeanLifecycleTest extends AbstractJSR299Test
{
   private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>()
   {
   };

   @Test(groups = "beanConstruction")
   @SpecAssertions({
     @SpecAssertion(section = "3.2.6.3", id = "a"),
     @SpecAssertion(section = "2.3.6", id = "d"),
     @SpecAssertion(section = "6.4", id = "a")
   })
   public void testInjectionOfParametersIntoBeanConstructor()
   {
      assert getCurrentManager().resolveByType(FishPond.class).size() == 1;
      FishPond fishPond = getCurrentManager().getInstanceByType(FishPond.class);
      assert fishPond.goldfish != null;
   }
   
   @Test
   @SpecAssertion(section = "3.2.6.3", id = "a")
   public void testBindingTypeAnnotatedConstructor() throws Exception
   {
      new RunInDependentContext()
      {
         @Override
         protected void execute() throws Exception
         {
            getCurrentManager().getInstanceByType(Duck.class);
            assert Duck.constructedCorrectly;
         }
      }.run();
   }

   @Test(groups = { "specialization" })
   @SpecAssertion(section = "3.2.7", id = "c")
   public void testSpecializedBeanAlwaysUsed() throws Exception
   {
      new RunInDependentContext()
      {

         @Override
         protected void execute() throws Exception
         {
            assert getCurrentManager().getInstanceByType(Lion.class, TAME_LITERAL) instanceof MountainLion;
         }

      }.run();
   }

   @Test(groups = "beanLifecycle")
   @SpecAssertions({
      @SpecAssertion(section = "6.1", id = "b"),
      @SpecAssertion(section = "6.2", id = "d")
   })
   public void testCreateReturnsSameBeanPushed() throws Exception
   {
      final CreationalContext<Farm> farmCreationalContext = new MyCreationalContext<Farm>();
      final Contextual<Farm> farmBean = getCurrentManager().resolveByType(Farm.class).iterator().next();
      MyCreationalContext.setLastBeanPushed(null);
      MyCreationalContext.setPushCalled(false);
      new RunInDependentContext()
      {

         @Override
         protected void execute() throws Exception
         {
            Farm farmInstance = getCurrentManager().getContext(Dependent.class).get(farmBean, farmCreationalContext);
            if (MyCreationalContext.isPushCalled())
            {
               assert farmInstance.equals(MyCreationalContext.getLastBeanPushed());
            }
            assert farmInstance.farmOffice != null : "FarmOffice should be injected by Contextual.create()";
         }

      }.run();      
   }

   @Test(groups = { "ri-broken", "beanLifecycle" })
   @SpecAssertions({
      @SpecAssertion(section = "6.2", id = "e")
   })
   public void testCreateSetsInitialValuesFromXml() throws Exception
   {
      new RunInDependentContext()
      {

         @Override
         protected void execute() throws Exception
         {
            Farm farmInstance = getCurrentManager().getInstanceByType(Farm.class);
            assert farmInstance.location != null;
            assert farmInstance.location.equals("Indiana");
         }

      }.run();            
   }
      
   @Test(groups = "beanLifecycle")
   @SpecAssertions({
      @SpecAssertion(section = "6.2", id = "a"),
      @SpecAssertion(section="2", id="g"),
      @SpecAssertion(section="2.2", id="f"),
      @SpecAssertion(section="3.2.6", id="a"),
      @SpecAssertion(section = "6", id = "d")
   })
   public void testCreateReturnsInstanceOfBean()
   {
      assert getCurrentManager().resolveByType(RedSnapper.class).size() == 1;
      assert getCurrentManager().getInstanceByType(RedSnapper.class) instanceof RedSnapper;
   }

   @Test(groups = { "stub", "beanLifecycle", "interceptors" })
   @SpecAssertion(section = "6.2", id = "b")
   public void testCreateBindsInterceptorStack()
   {
      assert false;
   }

   @Test(groups = { "stub", "beanLifecycle", "decorators" })
   @SpecAssertion(section = "6.2", id = "c")
   public void testCreateBindsDecoratorStack()
   {
      assert false;
   }

   @Test(groups = "injection")
   @SpecAssertions({
      @SpecAssertion(section = "6.4", id = "e"),
      @SpecAssertion(section = "3.8.1", id = "a")
   })
   public void testCreateInjectsFieldsDeclaredInJava()
   {
      assert getCurrentManager().resolveByType(TunaFarm.class).size() == 1;
      TunaFarm tunaFarm = getCurrentManager().getInstanceByType(TunaFarm.class);
      assert tunaFarm.tuna != null;
   }

   @Test(groups = "beanLifecycle")
   @SpecAssertions({
      @SpecAssertion(section = "6", id = "g")
   })
   public void testContextCreatesNewInstanceForInjection()
   {
      Context requestContext = getCurrentManager().getContext(RequestScoped.class);
      Bean<Tuna> tunaBean = getCurrentManager().resolveByType(Tuna.class).iterator().next();
      assert requestContext.get(tunaBean) == null;
      TunaFarm tunaFarm = getCurrentManager().getInstanceByType(TunaFarm.class);
      assert tunaFarm.tuna != null;
   }

   @Test(groups = { "beanLifecycle", "lifecycleCallbacks" })
   @SpecAssertions({
     @SpecAssertion(section = "6.2", id = "f"),
     @SpecAssertion(section = "6.3", id = "b"),
     @SpecAssertion(section = "6.4", id = "j"),
     @SpecAssertion(section = "6.4", id = "k")
   })
   public void testPostConstructPreDestroy() throws Exception
   {
      assert getCurrentManager().resolveByType(Farm.class).size() == 1;
      Bean<Farm> farmBean = getCurrentManager().resolveByType(Farm.class).iterator().next();
      Farm farm = getCurrentManager().getInstanceByType(Farm.class);
      assert farm.founded != null;
      assert farm.initialStaff == 20;
      assert farm.closed == null;
      farmBean.destroy(farm);
      assert farm.closed != null;
   }

   @Test(groups = { "beanLifecycle", "lifecycleCallbacks" })
   @SpecAssertions({
     @SpecAssertion(section = "6.3", id = "a"),
     @SpecAssertion(section = "6.3", id = "c")
   })
   public void testContextualDestroyDisposesWhenNecessary() throws Exception
   {
      final Contextual<Goose> gooseBean = getCurrentManager().resolveByType(Goose.class).iterator().next();
      final Goose goose = getCurrentManager().getInstanceByType(Goose.class);
      
      assert !EggProducer.isEggDisposed();
      assert !Egg.isEggDestroyed();
      gooseBean.destroy(goose);
      assert EggProducer.isEggDisposed();
      //TODO Apparently Dependent scoped injected objects do not have their PreDestroy method called
      //assert Egg.isEggDestroyed();
   }

   @Test(groups = "beanLifecycle")
   @SpecAssertions({
      @SpecAssertion(section = "6.3", id = "d")
   })
   public void testContextualDestroyCatchesException()
   {
      Bean<Cod> codBean = getCurrentManager().resolveByType(Cod.class).iterator().next();
      Cod codInstance = getCurrentManager().getInstanceByType(Cod.class);
      codBean.destroy(codInstance);
   }

   @Test(groups = "beanLifecycle")
   @SpecAssertions({
      @SpecAssertion(section = "6.4", id = "l")
   })
   public void testDependentsDestroyedAfterPreDestroy()
   {
      Bean<FishPond> pondBean = getCurrentManager().resolveByType(FishPond.class).iterator().next();
      FishPond fishPond = getCurrentManager().getInstanceByType(FishPond.class);
      pondBean.destroy(fishPond);
      assert Salmon.isBeanDestroyed();
   }

   @Test
   @SpecAssertion(section = "4.2", id = "baa")
   public void testSubClassInheritsPostConstructOnSuperclass() throws Exception
   {
      OrderProcessor.postConstructCalled = false;
      assert getCurrentManager().resolveByType(CdOrderProcessor.class).size() == 1;
      new RunInDependentContext()
      {
         @Override
         protected void execute() throws Exception
         {
            getCurrentManager().getInstanceByType(CdOrderProcessor.class).order();
         }
      }.run();
      assert OrderProcessor.postConstructCalled;
   }
   
   @Test
   @SpecAssertion(section = "4.2", id = "bac")
   public void testIndirectSubClassInheritsPostConstructOnSuperclass() throws Exception
   {
      OrderProcessor.postConstructCalled = false;
      assert getCurrentManager().resolveByType(IndirectOrderProcessor.class).size() == 1;
      new RunInDependentContext()
      {
         @Override
         protected void execute() throws Exception
         {
            getCurrentManager().getInstanceByType(IndirectOrderProcessor.class).order();
         }
      }.run();
      assert OrderProcessor.postConstructCalled;
   }   

   @Test
   @SpecAssertion(section = "4.2", id = "bba")
   public void testSubClassInheritsPreDestroyOnSuperclass() throws Exception
   {
      OrderProcessor.preDestroyCalled = false;
      assert getCurrentManager().resolveByType(CdOrderProcessor.class).size() == 1;
      new RunInDependentContext()
      {
         @Override
         protected void execute() throws Exception
         {  
            Bean<CdOrderProcessor> bean = getCurrentManager().resolveByType(CdOrderProcessor.class).iterator().next();
            CdOrderProcessor instance = getCurrentManager().getInstanceByType(CdOrderProcessor.class);
            bean.destroy(instance);
         }
      }.run();
      assert OrderProcessor.preDestroyCalled;
   }
   
   @Test
   @SpecAssertion(section = "4.2", id = "bbc")
   public void testIndirectSubClassInheritsPreDestroyOnSuperclass() throws Exception
   {
      OrderProcessor.preDestroyCalled = false;
      assert getCurrentManager().resolveByType(IndirectOrderProcessor.class).size() == 1;
      new RunInDependentContext()
      {
         @Override
         protected void execute() throws Exception
         {  
            Bean<IndirectOrderProcessor> bean = getCurrentManager().resolveByType(IndirectOrderProcessor.class).iterator().next();
            IndirectOrderProcessor instance = getCurrentManager().getInstanceByType(IndirectOrderProcessor.class);
            bean.destroy(instance);
         }
      }.run();
      assert OrderProcessor.preDestroyCalled;
   }   

   @Test
   @SpecAssertion(section = "4.2", id = "baa")
   public void testSubClassDoesNotInheritPostConstructOnSuperclassBlockedByIntermediateClass() throws Exception
   {
      assert getCurrentManager().resolveByType(NovelOrderProcessor.class).size() == 1;
      OrderProcessor.postConstructCalled = false;
      new RunInDependentContext()
      {
         @Override
         protected void execute() throws Exception
         {
            getCurrentManager().getInstanceByType(NovelOrderProcessor.class).order();
         }
      }.run();
      assert !OrderProcessor.postConstructCalled;
   }

   @Test
   @SpecAssertion(section = "4.2", id = "bba")
   public void testSubClassDoesNotInheritPreDestroyConstructOnSuperclassBlockedByIntermediateClass() throws Exception
   {
      OrderProcessor.preDestroyCalled = false;
      assert getCurrentManager().resolveByType(NovelOrderProcessor.class).size() == 1;
      new RunInDependentContext()
      {
         @Override
         protected void execute() throws Exception
         {
            Bean<NovelOrderProcessor> bean = getCurrentManager().resolveByType(NovelOrderProcessor.class).iterator().next();
            NovelOrderProcessor instance = getCurrentManager().getInstanceByType(NovelOrderProcessor.class);
            bean.destroy(instance);
         }
      }.run();
      assert !OrderProcessor.preDestroyCalled;

   }

   @Test(expectedExceptions = CreationException.class)
   @SpecAssertion(section = "6.2", id = "h")
   public void testCreationExceptionWrapsCheckedExceptionThrownFromCreate() throws Exception
   {
      assert getCurrentManager().resolveByType(Lorry_Broken.class).size() == 1;
      new RunInDependentContext()
      {

         @Override
         protected void execute() throws Exception
         {
            getCurrentManager().getInstanceByType(Lorry_Broken.class);
         }

      }.run();
   }

   @Test(expectedExceptions = FooException.class)
   @SpecAssertion(section = "6.2", id = "g")
   public void testUncheckedExceptionThrownFromCreateNotWrapped() throws Exception
   {
      assert getCurrentManager().resolveByType(Van_Broken.class).size() == 1;
      new RunInDependentContext()
      {

         @Override
         protected void execute() throws Exception
         {
            getCurrentManager().getInstanceByType(Van_Broken.class);
         }

      }.run();
   }

}
