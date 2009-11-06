package org.jboss.jsr299.tck.tests.context;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Context;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091101")
@Resources({
   @Resource(source="javax.enterprise.inject.spi.Extension", destination="WEB-INF/classes/META-INF/services/javax.enterprise.inject.spi.Extension")
})
@IntegrationTest
public class ContextTest extends AbstractJSR299Test
{
   @Test(expectedExceptions = { IllegalStateException.class }, groups = { "manager" })
   @SpecAssertion(section = "6.5.1", id = "b")
   public void testGetContextWithTooManyActiveContextsFails()
   {
      getCurrentManager().getContext(DummyScoped.class);
   }

   @Test(expectedExceptions = { ContextNotActiveException.class }, groups = { "manager" })
   @SpecAssertion(section = "6.5.1", id = "a")
   public void testGetContextWithNoRegisteredContextsFails()
   {
      getCurrentManager().getContext(Unregistered.class);
   }

   @Test(groups = { "contexts" })
   @SpecAssertions({
      @SpecAssertion(section = "2.4.1", id = "aa"),
      @SpecAssertion(section = "2.4.1", id = "ab"),
      @SpecAssertion(section = "2.4.1", id = "ac"),
      @SpecAssertion(section = "2.4.1", id = "ca"),
      @SpecAssertion(section = "11.3.14", id = "a")
   })
   public void testBuiltInContexts()
   {
      Context context = getCurrentManager().getContext(Dependent.class);
      assert context != null;
      context = getCurrentManager().getContext(RequestScoped.class);
      assert context != null;
      context = getCurrentManager().getContext(SessionScoped.class);
      assert context != null;
      context = getCurrentManager().getContext(ApplicationScoped.class);
      assert context != null;
      // Can't test conversations here, they are only available for a JSF 
      // request. Standalone container only simulates a servlet request 
   }
}
