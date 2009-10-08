package org.jboss.jsr299.tck.tests.implementation.simple.definition;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.tests.implementation.simple.definition.OuterClass.InnerClass_NotBean;
import org.jboss.jsr299.tck.tests.implementation.simple.definition.OuterClass.StaticInnerClass;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class SimpleBeanDefinitionTest extends AbstractJSR299Test
{

   @Test
   @SpecAssertion(section = "3.1.1", id = "ca")
   public void testAbstractClassDeclaredInJavaNotDiscovered()
   {
      assert getBeans(Cow_NotBean.class).size() == 0;
   }

   @Test(groups = "innerClass")
   @SpecAssertions({
      @SpecAssertion(section = "3.1.1", id = "ba")
   })
   public void testStaticInnerClassDeclaredInJavaAllowed()
   {
      assert getBeans(StaticInnerClass.class).size() == 1;
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.1.1", id = "b")
   })
   public void testNonStaticInnerClassDeclaredInJavaNotDiscovered()
   {
      assert getBeans(InnerClass_NotBean.class).size() == 0;
   }

   @Test
   @SpecAssertion(section = "3.1.1", id = "cb")
   public void testInterfaceNotDiscoveredAsSimpleBean()
   {
      assert getBeans(Car.class).size() == 0;
   }

   @Test
   @SpecAssertion(section="3.1.1", id="f")
   public void testClassesImplementingEnterpriseBeanInterfaceNotDiscoveredAsSimpleBean()
   {
      assert getBeans(MockEnterpriseBean.class).size() == 0;
   }

   @Test
   @SpecAssertion(section="3.1.1", id="p")
   public void testSimpleBeanOnlyIfConstructorParameterless()
   {
      assert getBeans(Antelope_NotBean.class).isEmpty();
      assert !getBeans(Donkey.class).isEmpty();
   }

   @Test
   @SpecAssertion(section="3.1.1", id="q")
   public void testSimpleBeanOnlyIfConstructorIsInitializer()
   {
      assert getBeans(Antelope_NotBean.class).isEmpty();
      assert !getBeans(Sheep.class).isEmpty();
   }

   @Test
   @SpecAssertion(section = "3.7.1", id = "aa")
   public void testInitializerAnnotatedConstructor() throws Exception
   {
      Sheep.constructedCorrectly = false;
      getInstanceByType(Sheep.class);
      assert Sheep.constructedCorrectly;
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "3.7.1", id = "ba"),
      @SpecAssertion(section = "3.1.3", id = "a"),
      @SpecAssertion(section = "3.7", id = "a")
   })
   public void testEmptyConstructorUsed()
   {
      Donkey.constructedCorrectly = false;
      getInstanceByType(Donkey.class);
      assert Donkey.constructedCorrectly;
   }

   @Test
   @SpecAssertion(section = "3.7.1", id = "aa")
   public void testInitializerAnnotatedConstructorUsedOverEmptyConstuctor() throws Exception
   {
      getInstanceByType(Turkey.class);
      assert Turkey.constructedCorrectly;
   }

   @Test
   @SpecAssertion(section = "3.1", id = "fa")
   public void testDependentScopedBeanCanHavePublicField() throws Exception
   {
      assert getInstanceByType(Tiger.class).name.equals("pete");
   }
}
