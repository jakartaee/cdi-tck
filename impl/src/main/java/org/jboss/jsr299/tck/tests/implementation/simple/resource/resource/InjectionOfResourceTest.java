package org.jboss.jsr299.tck.tests.implementation.simple.resource.resource;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@Packaging(PackagingType.WAR)
@IntegrationTest
@BeansXml("beans.xml")
@SpecVersion(spec="cdi", version="20091018")
public class InjectionOfResourceTest extends AbstractJSR299Test
{
   @Test(groups = { "beanLifecycle", "commonAnnotations", "integration" })
   @SpecAssertion(section = "3.5.1", id = "bb")
   public void testInjectionOfResource()
   {
      Bean<ManagedBean> managedBeanBean = getBeans(ManagedBean.class).iterator().next();
      CreationalContext<ManagedBean> managedBeanCc = getCurrentManager().createCreationalContext(managedBeanBean);
      ManagedBean managedBean = managedBeanBean.create(managedBeanCc);
      assert managedBean.getBeanManager() != null : "@Another Manager not found";
      assert managedBean.getBeanManager().equals(getCurrentManager()): "Wrong manager found";
   }
   
   @Test(groups = { "beanLifecycle", "commonsAnnotations", "integration" })
   @SpecAssertions({
      @SpecAssertion(section = "7.3.6", id = "la"),
      @SpecAssertion(section = "7.3.6", id = "ma"),
      @SpecAssertion(section = "7.3.6", id = "o")
   })
   public void testProduceResourceProxy()
   {
      Bean<BeanManager> beanManagerBean = getBeans(BeanManager.class, new AnnotationLiteral<Another>() {}).iterator().next();
      CreationalContext<BeanManager> beanManagerCc = getCurrentManager().createCreationalContext(beanManagerBean);
      BeanManager beanManager = beanManagerBean.create(beanManagerCc);
      assert beanManager != null;
      assert beanManager.equals(getCurrentManager());
   }
   
   @Test(groups = { "beanLifecycle", "commonsAnnotations", "integration" })
   @SpecAssertions({
      @SpecAssertion(section = "7.3.6", id = "mb")
   })
   public void testPassivatingResource() throws Exception
   {
      Bean<ManagedBean> managedBeanBean = getBeans(ManagedBean.class).iterator().next();
      CreationalContext<ManagedBean> managedBeanCc = getCurrentManager().createCreationalContext(managedBeanBean);
      ManagedBean managedBean = managedBeanBean.create(managedBeanCc);
      managedBean = (ManagedBean) deserialize(serialize(managedBean));
      assert managedBean.getBeanManager() != null;
   }
}
