package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorCalledBeforeDecorator;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="PFD2")
public class InterceptorCalledBeforeDecoratorTest extends AbstractJSR299Test
{
   @Test(groups = "ri-broken")
   @SpecAssertion(section = "9.4", id = "g")
   public void testInterceptorCalledBeforeDecorator()
   {
      Foo.interceptorCalledFirst = false;
      Foo.decoratorCalledFirst = false;
      
      Foo foo = getInstanceByType(Foo.class);
      foo.bar();
      
      assert Foo.interceptorCalledFirst;
   }
}
