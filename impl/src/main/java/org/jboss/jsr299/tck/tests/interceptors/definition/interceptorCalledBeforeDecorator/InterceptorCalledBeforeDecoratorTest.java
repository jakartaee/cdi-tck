package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorCalledBeforeDecorator;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;

import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091101")
@BeansXml("beans.xml")
public class InterceptorCalledBeforeDecoratorTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "9.4", id = "g")
   public void testInterceptorCalledBeforeDecorator()
   {
      Foo.interceptorCalledFirst = false;
      Foo.decoratorCalledFirst = false;
      
      Foo foo = getInstanceByType(Foo.class);
      foo.bar();
      
      assert Foo.interceptorCalledFirst;
      assert !Foo.decoratorCalledFirst;
   }
}
