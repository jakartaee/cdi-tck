package org.jboss.jsr299.tck.tests.context;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class DestroyedInstanceReturnedByGetTest extends AbstractJSR299Test
{
   @Test(groups = { "contexts" })
   @SpecAssertions({
      @SpecAssertion(section = "6.2", id = "q"),
      @SpecAssertion(section = "11.1", id = "aa")
   })
   public void testDestroyedInstanceMustNotBeReturnedByGet()
   {
      assert getBeans(MySessionBean.class).size() == 1;
      Bean<MySessionBean> mySessionBean = getBeans(MySessionBean.class).iterator().next();
      CreationalContext<MySessionBean> sessionCreationalContext = getCurrentManager().createCreationalContext(mySessionBean);
      MySessionBean beanInstance = mySessionBean.create(sessionCreationalContext);
      assert beanInstance != null;
      destroyContext(getCurrentManager().getContext(SessionScoped.class));
      setContextActive(getCurrentManager().getContext(SessionScoped.class));
      
      beanInstance = getCurrentManager().getContext(SessionScoped.class).get(mySessionBean);
      assert beanInstance == null;
      
      Bean<MyApplicationBean> myApplicationBean = getBeans(MyApplicationBean.class).iterator().next();
      CreationalContext<MyApplicationBean> applicationCreationalContext = getCurrentManager().createCreationalContext(myApplicationBean);

      MyApplicationBean myApplicationBeanInstance = myApplicationBean.create(applicationCreationalContext);
      assert myApplicationBeanInstance != null;
      destroyContext(getCurrentManager().getContext(ApplicationScoped.class));
      setContextActive(getCurrentManager().getContext(ApplicationScoped.class));

      myApplicationBeanInstance = getCurrentManager().getContext(ApplicationScoped.class).get(myApplicationBean);
      assert myApplicationBeanInstance == null;
   }
   
}
