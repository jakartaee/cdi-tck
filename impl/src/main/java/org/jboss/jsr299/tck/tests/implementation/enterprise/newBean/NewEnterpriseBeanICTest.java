package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

@Artifact
@Packaging(PackagingType.EAR)
@IntegrationTest
@SpecVersion(spec="cdi", version="20091101")
public class NewEnterpriseBeanICTest extends AbstractJSR299Test
{
   
   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "l")
   public void testNewBeanHasSameConstructor()
   {
      ExplicitConstructor bean = getInstanceByType(ExplicitConstructor.class);
      bean.setConstructorCalls(0);
      ExplicitConstructor newBean = getInstanceByType(ExplicitConstructor.class, ExplicitConstructorSessionBean.NEW);
      assert bean != newBean;
      assert bean.getConstructorCalls() == 1;
      assert bean.getInjectedSimpleBean() != null;
   }

   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "m")
   public void testNewBeanHasSameInitializers()
   {
      InitializerSimpleBeanLocal bean = getInstanceByType(InitializerSimpleBeanLocal.class);
      InitializerSimpleBeanLocal newBean = getInstanceByType(InitializerSimpleBeanLocal.class, InitializerSimpleBeanLocal.NEW);
      assert bean != newBean;
      assert bean.getInitializerCalls() == 2;
   }

   /**
    * Sets up both the bean and the @New bean with different configurations
    * so that the correct producer method used can be determined.
    * 
    * @throws Exception 
    */
   @Test(groups = { "new" })
   @SpecAssertion(section = "3.12", id = "v")
   public void testNewBeanHasNoProducerMethods() throws Exception
   {
      FoxLocal fox = getInstanceByType(FoxLocal.class);
      FoxLocal newFox = getInstanceByType(FoxLocal.class, FoxLocal.NEW);
      fox.setNextLitterSize(3);
      newFox.setNextLitterSize(5);
      Litter theOnlyLitter = getInstanceByType(Litter.class,new AnnotationLiteral<Tame>(){});
      assert theOnlyLitter.getQuantity() == fox.getNextLitterSize();
   }

   @Test(groups = { "new", "disposal" })
   @SpecAssertion(section = "3.12", id = "x")
   public void testNewBeanHasNoDisposalMethods() throws Exception
   {
      FoxLocal fox = getInstanceByType(FoxLocal.class);
      FoxLocal newFox = getInstanceByType(FoxLocal.class, FoxLocal.NEW);
      Set<Bean<Litter>> beans = getBeans(Litter.class, new AnnotationLiteral<Tame>() {});
      assert beans.size() == 1;
      Bean<Litter> litterBean = beans.iterator().next();
      CreationalContext<Litter> creationalContext = getCurrentManager().createCreationalContext(litterBean);
      Litter litter = litterBean.create(creationalContext);
      litterBean.destroy(litter, creationalContext);
      assert fox.isLitterDisposed();
      assert !newFox.isLitterDisposed();
   }

}
