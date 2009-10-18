package org.jboss.jsr299.tck.tests.context;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.impl.MockCreationalContext;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * 
 * @author Nicklas Karlsson
 * @author Pete Muir
 * @author David Allen
 */
@Artifact
@SpecVersion(spec="cdi", version="20091018")
public class DestroyForSameCreationalContextTest extends AbstractJSR299Test
{
   
   @Test
   @SpecAssertion(section = "6.2", id = "r")
   public void testDestroyForSameCreationalContextOnly()
   {
      // Check that the mock cc is called (via cc.release()) when we request a context destroyed
      // Note that this is an indirect effect
      Context sessionContext = getCurrentManager().getContext(SessionScoped.class);
      
      Bean<AnotherSessionBean> sessionBean = getBeans(AnotherSessionBean.class).iterator().next();
      
      MockCreationalContext.reset();
      CreationalContext<AnotherSessionBean> creationalContext = new MockCreationalContext<AnotherSessionBean>();
      AnotherSessionBean instance = sessionContext.get(sessionBean, creationalContext);
      instance.ping();
      
      destroyContext(sessionContext);
      assert MockCreationalContext.isReleaseCalled();
            
   }
   
}
