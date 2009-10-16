package org.jboss.jsr299.tck.tests.definition.scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.NormalScope;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class ScopeDefinitionTest extends AbstractJSR299Test
{
   
   @Test @SpecAssertion(section="2.4", id = "c")
   public void testScopeTypesAreExtensible()
   {
      assert getBeans(Mullet.class).size() == 1; 
      Bean<Mullet> bean = getBeans(Mullet.class).iterator().next();
      assert bean.getScope().equals(AnotherScopeType.class);
   }
   
   @Test(groups={"annotationDefinition"}) 
   @SpecAssertion(section="2.4.2", id = "aa")
   public void testScopeTypeHasCorrectTarget()
   {
      assert getBeans(Mullet.class).size() == 1; 
      Bean<Mullet> bean = getBeans(Mullet.class).iterator().next();
      Target target = bean.getScope().getAnnotation(Target.class);
      List<ElementType> elements = Arrays.asList(target.value());
      assert elements.contains(ElementType.TYPE);
      assert elements.contains(ElementType.METHOD);
      assert elements.contains(ElementType.FIELD);      
   }

   @Test(groups={"annotationDefinition"}) 
   @SpecAssertion(section="2.4.2", id = "ba")
   public void testScopeTypeDeclaresScopeTypeAnnotation()
   {
      assert getBeans(Mullet.class).size() == 1; 
      Bean<Mullet> bean = getBeans(Mullet.class).iterator().next();
      assert bean.getScope().getAnnotation(NormalScope.class) != null;
   }
   
   @Test @SpecAssertion(section="2.4.3", id = "a")
   public void testScopeDeclaredInJava()
   {
      assert getBeans(SeaBass.class).size() == 1; 
      Bean<SeaBass> bean = getBeans(SeaBass.class).iterator().next();
      assert bean.getScope().equals(RequestScoped.class);
   }
   
   @Test @SpecAssertion(section="2.4.4", id = "aa")
   public void testDefaultScope()
   {
      assert getBeans(Order.class).size() == 1; 
      Bean<Order> bean = getBeans(Order.class).iterator().next();
      assert bean.getScope().equals(Dependent.class);
   }
   
   @Test
   @SpecAssertions({
     @SpecAssertion(section = "2.4.4", id = "e"),
     @SpecAssertion(section = "2.7.2", id = "a")
   })
   public void testScopeSpecifiedAndStereotyped()
   {
      assert getBeans(Minnow.class).size() == 1; 
      Bean<Minnow> bean = getBeans(Minnow.class).iterator().next();
      assert bean.getScope().equals(RequestScoped.class);
   }
   
   @Test @SpecAssertion(section="2.4.4", id = "da")
   public void testMultipleIncompatibleScopeStereotypesWithScopeSpecified()
   {
      assert getBeans(Pollock.class).size() == 1; 
      Bean<Pollock> bean = getBeans(Pollock.class).iterator().next();
      assert bean.getScope().equals(Dependent.class);
   }
   
   @Test @SpecAssertion(section="2.4.4", id = "c")
   public void testMultipleCompatibleScopeStereotypes()
   {
      assert getBeans(Grayling.class).size() == 1; 
      Bean<Grayling> bean = getBeans(Grayling.class).iterator().next();
      assert bean.getScope().equals(ApplicationScoped.class);
   }
   
   @Test
   @SpecAssertions({
     @SpecAssertion(section = "2.7.2", id = "db"),
     @SpecAssertion(section = "4.1", id = "ab")
   })
   public void testWebBeanScopeTypeOverridesStereotype()
   {
      assert getBeans(RedSnapper.class).size() == 1; 
      Bean<RedSnapper> bean = getBeans(RedSnapper.class).iterator().next();
      assert bean.getScope().equals(RequestScoped.class);
   }
   
   @Test @SpecAssertion(section="4.1", id = "ba")
   public void testScopeTypeDeclaredInheritedIsInherited() throws Exception
   {
      assert getBeans(BorderCollie.class).iterator().next().getScope().equals(RequestScoped.class);
   }
   
   @Test @SpecAssertion(section="4.1", id = "baa")
   public void testScopeTypeNotDeclaredInheritedIsNotInherited()
   {
      assert getBeans(ShetlandPony.class).size() == 1; 
      assert getBeans(ShetlandPony.class).iterator().next().getScope().equals(Dependent.class);
   }
   
   @Test @SpecAssertion(section="4.1", id = "ba")
   public void testScopeTypeDeclaredInheritedIsBlockedByIntermediateScopeTypeMarkedInherited()
   {
      assert getBeans(GoldenRetriever.class).size() == 1; 
   }
   
   @Test @SpecAssertion(section="4.1", id = "ba")
   public void testScopeTypeDeclaredInheritedIsBlockedByIntermediateScopeTypeNotMarkedInherited()
   {
      assert getBeans(GoldenLabrador.class).size() == 1; 
      assert getBeans(GoldenLabrador.class).iterator().next().getScope().equals(Dependent.class);
   }
   
   @Test
   @SpecAssertion(section = "4.1", id = "bc")
   public void testScopeTypeDeclaredInheritedIsIndirectlyInherited()
   {
      assert getBeans(EnglishBorderCollie.class).iterator().next().getScope().equals(RequestScoped.class);
   }
   
   @Test 
   @SpecAssertion(section="4.1", id = "bca")
   public void testScopeTypeNotDeclaredInheritedIsNotIndirectlyInherited()
   {
      assert getBeans(MiniatureClydesdale.class).size() == 1; 
      assert getBeans(MiniatureClydesdale.class).iterator().next().getScope().equals(Dependent.class);
   }
   
}