package org.jboss.jsr299.tck.tests.implementation.enterprise.definition;

import java.lang.annotation.Annotation;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

/**
 * @author Nicklas Karlsson
 * 
 */
@Artifact
@Packaging(PackagingType.EAR)
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091101")
public class EnterpriseBeanDefinitionTest extends AbstractJSR299Test
{
   @Test(groups = { "enterpriseBeans" })
   @SpecAssertion(section = "3.2", id = "b")
   public void testStatelessMustBeDependentScoped()
   {
      assert getBeans(GiraffeLocal.class).size() == 1;
      assert getBeans(GiraffeLocal.class).iterator().next().getScope().equals(Dependent.class);
   }
   
   
   @Test(groups = { "new" })
   @SpecAssertion(section = "3.7.1", id = "ab")
   public void testConstructorAnnotatedInjectCalled()
   {
      ExplicitConstructor bean = getInstanceByType(ExplicitConstructor.class);
      assert bean.getConstructorCalls() == 1;
      assert bean.getInjectedSimpleBean() instanceof SimpleBean;
   }

   @Test(groups = { "enterpriseBeans" })
   @SpecAssertion(section = "3.2", id = "c")
   public void testSingletonWithDependentScopeOK()
   {
      assert getBeans(Labrador.class).size() == 1;
   }

   @Test(groups = { "enterpriseBeans" })
   @SpecAssertion(section = "3.2", id = "c")
   public void testSingletonWithApplicationScopeOK()
   {
      assert getBeans(Laika.class).size() == 1;
   }

   @Test(groups = { "enterpriseBeans" })
   @SpecAssertions( {
      @SpecAssertion(section = "3.2.2", id = "aa"),
      @SpecAssertion(section = "3.2.3", id = "c") } )
   public void testBeanTypesAreLocalInterfacesWithoutWildcardTypesOrTypeVariablesWithSuperInterfaces()
   {
      Bean<DogLocal> dogBean = getBeans(DogLocal.class).iterator().next();
      assert dogBean.getTypes().contains(DogLocal.class);
      assert dogBean.getTypes().contains(PitbullLocal.class);
      assert !dogBean.getTypes().contains(Pitbull.class);
   }

   @Test(groups = { "ejb 3.1" })
   @SpecAssertion(section = "3.2.2", id = "ba")
   public void testEnterpriseBeanClassLocalView()
   {
      //TODO We need a 3.1 compliant container for this test
      Bean<Retriever> dogBean = getBeans(Retriever.class).iterator().next();
      assert dogBean.getTypes().contains(Retriever.class);
   }

   @Test(groups = "enterpriseBeans")
   @SpecAssertions({
      @SpecAssertion(section = "3.2.2", id = "c"),
      @SpecAssertion(section = "3.2.3", id = "aa"),
      @SpecAssertion(section = "2.2", id = "l")
   })
   public void testObjectIsInAPITypes()
   {
      assert getBeans(GiraffeLocal.class).size() == 1;
      assert getBeans(GiraffeLocal.class).iterator().next().getTypes().contains(Object.class);
   }

   @Test(groups = { "enterpriseBeans" })
   @SpecAssertion(section = "3.2.2", id = "d")
   public void testRemoteInterfacesAreNotInAPITypes()
   {
      Bean<DogLocal> dogBean = getBeans(DogLocal.class).iterator().next();
      assert !dogBean.getTypes().contains(DogRemote.class);
   }

   @Test(groups = "enterpriseBeans")
   @SpecAssertions({
      @SpecAssertion(section = "3.2.3", id = "ba"),
      @SpecAssertion(section = "3.2", id = "e")
   })
   public void testBeanWithScopeAnnotation()
   {
      Bean<LionLocal> lionBean = getBeans(LionLocal.class).iterator().next();
      assert lionBean.getScope().equals(RequestScoped.class);
   }

   @Test(groups = "enterpriseBeans")
   @SpecAssertion(section = "3.2.3", id = "bb")
   public void testBeanWithNamedAnnotation()
   {
      Bean<MonkeyLocal> monkeyBean = getBeans(MonkeyLocal.class).iterator().next();
      assert monkeyBean.getName().equals("Monkey");
   }


   @Test(groups = "enterpriseBeans")
   @SpecAssertion(section = "3.2.3", id = "bd")
   public void testBeanWithStereotype()
   {
      Bean<PolarBearLocal> polarBearBean = getBeans(PolarBearLocal.class).iterator().next();
      assert polarBearBean.getScope().equals(RequestScoped.class);
      assert polarBearBean.getName().equals("polarBear");
   }

   @Test(groups = "enterpriseBeans")
   @SpecAssertion(section = "3.2.3", id = "be")
   public void testBeanWithQualifiers()
   {
      Annotation tame = new AnnotationLiteral<Tame>(){};
      Bean<ApeLocal> apeBean = getBeans(ApeLocal.class, tame).iterator().next();
      assert apeBean.getQualifiers().contains(tame);
   }

   @Test(groups = "enterpriseBeans")
   @SpecAssertion(section = "3.2.5", id = "a")
   public void testDefaultName()
   {
      assert getBeans(PitbullLocal.class).size() == 1;
      assert getBeans(PitbullLocal.class).iterator().next().getName().equals("pitbull");
   }

}
