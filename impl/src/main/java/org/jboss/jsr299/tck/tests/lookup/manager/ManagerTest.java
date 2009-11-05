package org.jboss.jsr299.tck.tests.lookup.manager;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.DefaultLiteral;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091101")
public class ManagerTest extends AbstractJSR299Test
{
   @Test(groups={"manager", "injection", "deployment"}) 
   @SpecAssertion(section="11.3", id = "c")
   public void testInjectingManager()
   {
      FishFarmOffice fishFarmOffice = getInstanceByType(FishFarmOffice.class);
      assert fishFarmOffice.beanManager != null;
   }  
   
   @Test
   @SpecAssertion(section = "11.3", id = "aa")
   public void testContainerProvidesManagerBean()
   {
      assert getBeans(BeanManager.class).size() > 0;
   }
   
   @Test
   @SpecAssertion(section = "11.3", id = "ab")
   public void testManagerBeanIsDependentScoped()
   {
      Bean<BeanManager> beanManager = getBeans(BeanManager.class).iterator().next();
      assert beanManager.getScope().equals(Dependent.class);
   }

   @Test
   @SpecAssertion(section = "11.3", id = "ac")
   public void testManagerBeanHasCurrentBinding()
   {      
      Bean<BeanManager> beanManager = getBeans(BeanManager.class).iterator().next();
      assert beanManager.getQualifiers().contains(new DefaultLiteral());
   }
   
   @Test
   @SpecAssertion(section = "11.3", id = "b")
   public void testManagerBeanIsPassivationCapable()
   {
      assert isSerializable(getCurrentManager().getClass());
   }

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.3.1", id = "a"),
      @SpecAssertion(section = "11.3.1", id = "b")
   })
   public void testGetReferenceReturnsContextualInstance()
   {
      Bean<FishFarmOffice> bean = getBeans(FishFarmOffice.class).iterator().next();
      assert getCurrentManager().getReference(bean,FishFarmOffice.class, getCurrentManager().createCreationalContext(bean)) instanceof FishFarmOffice;      
   }
   
   private boolean isSerializable(Class<?> clazz)
   {
      return clazz.isPrimitive() || Serializable.class.isAssignableFrom(clazz);
   }

}
