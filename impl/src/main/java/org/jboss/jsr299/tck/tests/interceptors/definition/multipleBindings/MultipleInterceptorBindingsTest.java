package org.jboss.jsr299.tck.tests.interceptors.definition.multipleBindings;

import javax.enterprise.util.AnnotationLiteral;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="20091018")
@BeansXml("beans.xml")
public class MultipleInterceptorBindingsTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "9.5.1", id = "a"),
      @SpecAssertion(section = "9.5", id = "ca")
   })
   public void testInterceptorAppliedToBeanWithAllBindings()
   {
      MissileInterceptor.intercepted = false;
      
      Missile missile = getInstanceByType(FastAndDeadlyMissile.class);
      missile.fire();
      
      assert MissileInterceptor.intercepted;
   }
   
   @Test
   @SpecAssertion(section = "9.5.1", id = "b")
   public void testInterceptorNotAppliedToBeanWithSomeBindings()
   {
      MissileInterceptor.intercepted = false;
      
      Missile missile = getInstanceByType(SlowMissile.class);      
      missile.fire();
      
      assert !MissileInterceptor.intercepted;
   }
}
