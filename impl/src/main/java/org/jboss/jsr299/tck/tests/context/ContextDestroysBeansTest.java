package org.jboss.jsr299.tck.tests.context;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.testng.annotations.Test;

/**
 * 
 * @author Nicklas Karlsson
 * @author Pete Muir
 * @author David Allen
 */
@Artifact
@IntegrationTest
@SpecVersion(spec="cdi", version="20091018")
@Resource(source = "javax.enterprise.inject.spi.Extension", destination = "WEB-INF/classes/META-INF/services/javax.enterprise.inject.spi.Extension")
public class ContextDestroysBeansTest extends AbstractJSR299Test
{

   @Test(groups = { "contexts" })
   @SpecAssertions( {
      @SpecAssertion(section = "6.2", id = "p"),
      @SpecAssertion(section = "6.3", id = "d")
   })
   public void testContextDestroysBeansWhenDestroyed()
   {
      MyContextual bean = AfterBeanDiscoveryObserver.getBean();
      bean.setShouldReturnNullInstances(false);

      Context sessionContext = getCurrentManager().getContext(SessionScoped.class);
      CreationalContext<MySessionBean> creationalContext = getCurrentManager().createCreationalContext(bean);
      MySessionBean instance = sessionContext.get(bean, creationalContext);
      instance.ping();
      assert instance != null;
      assert bean.isCreateCalled();
      
      destroyContext(sessionContext);
      assert bean.isDestroyCalled();
   }
   
}