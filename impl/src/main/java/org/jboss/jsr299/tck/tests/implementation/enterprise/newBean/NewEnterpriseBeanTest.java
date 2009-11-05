package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.New;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.jsr299.tck.literals.DefaultLiteral;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

@Artifact
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="20091101")
public class NewEnterpriseBeanTest extends AbstractJSR299Test
{
   private Bean<WrappedEnterpriseBeanLocal> newEnterpriseBean;
   
   private static final Annotation TAME_LITERAL = new AnnotationLiteral<Tame>() {};
   
   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "p")
   public void testNewBeanIsDependentScoped()
   {
      Set<Bean<WrappedEnterpriseBeanLocal>> beans = getBeans(WrappedEnterpriseBeanLocal.class, WrappedEnterpriseBeanLocal.NEW);
      assert beans.size() == 1;
      newEnterpriseBean = beans.iterator().next();
      assert Dependent.class.equals(newEnterpriseBean.getScope());
   }

   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "r")
   public void testNewBeanIsHasOnlyNewBinding()
   {
      Set<Bean<WrappedEnterpriseBeanLocal>> beans = getBeans(WrappedEnterpriseBeanLocal.class, WrappedEnterpriseBeanLocal.NEW);
      assert beans.size() == 1;
      newEnterpriseBean = beans.iterator().next();
      assert newEnterpriseBean.getQualifiers().size() == 1;
      assert newEnterpriseBean.getQualifiers().iterator().next().annotationType().equals(New.class);
   }

   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "s")
   public void testNewBeanHasNoBeanELName()
   {
      Set<Bean<WrappedEnterpriseBeanLocal>> beans = getBeans(WrappedEnterpriseBeanLocal.class, WrappedEnterpriseBeanLocal.NEW);
      assert beans.size() == 1;
      newEnterpriseBean = beans.iterator().next();
      assert newEnterpriseBean.getName() == null;
   }

   @Test(groups = { "new"})
   @SpecAssertion(section = "3.12", id = "t")
   public void testNewBeanHasNoStereotypes()
   {
      Bean<MonkeyLocal> monkeyBean = getBeans(MonkeyLocal.class).iterator().next();
      Bean<MonkeyLocal> newMonkeyBean = getBeans(MonkeyLocal.class, MonkeyLocal.NEW).iterator().next();
      assert monkeyBean.getScope().equals(RequestScoped.class);
      assert newMonkeyBean.getScope().equals(Dependent.class);
      assert monkeyBean.getName().equals("monkey");
      assert newMonkeyBean.getName() == null;
   }
   
   @Test(groups = {"new" })
   @SpecAssertion(section = "3.12", id = "u")
   public void testNewBeanHasNoObservers()
   {
      // Should just be 1 observer from bean, not new bean
      assert getCurrentManager().resolveObserverMethods("event").size() == 1;
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section="3.12", id = "j")
   })
   public void testForEachEnterpriseBeanANewBeanExists()
   {
      Bean<OrderLocal> orderBean = getBeans(OrderLocal.class).iterator().next();
      Bean<OrderLocal> newOrderBean = getBeans(OrderLocal.class, OrderLocal.NEW).iterator().next();
      assert orderBean.getQualifiers().size() == 2;
      assert orderBean.getQualifiers().contains(new DefaultLiteral());
      assert orderBean.getQualifiers().contains(new AnyLiteral());
      
      assert getBeans(OrderLocal.class, OrderLocal.NEW).size() == 1;
      assert newOrderBean.getQualifiers().size() == 1;
      assert newOrderBean.getQualifiers().iterator().next().annotationType().equals(New.class);
      
      assert orderBean.getTypes().equals(newOrderBean.getTypes());
      
      Bean<LionLocal> lionBean = getBeans(LionLocal.class, TAME_LITERAL).iterator().next();
      Bean<LionLocal> newLionBean = getBeans(LionLocal.class, LionLocal.NEW).iterator().next();
      assert getBeans(LionLocal.class, TAME_LITERAL).size() == 1;
      assert lionBean.getQualifiers().size() == 2;
      assert lionBean.getQualifiers().contains(TAME_LITERAL);
      assert lionBean.getQualifiers().contains(new AnyLiteral());
      
      assert getBeans(LionLocal.class, LionLocal.NEW).size() == 1;
      assert newLionBean.getQualifiers().size() == 1;
      assert newLionBean.getQualifiers().iterator().next().annotationType().equals(New.class);
      
      assert lionBean.getTypes().equals(newLionBean.getTypes());
   }
}
