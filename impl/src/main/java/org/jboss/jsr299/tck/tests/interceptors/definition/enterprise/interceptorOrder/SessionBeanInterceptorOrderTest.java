package org.jboss.jsr299.tck.tests.interceptors.definition.enterprise.interceptorOrder;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;


@IntegrationTest
@Artifact
@Packaging(PackagingType.EAR)
@SpecVersion(spec="cdi", version="20091101")
@BeansXml("beans.xml")
public class SessionBeanInterceptorOrderTest extends AbstractJSR299Test
{
   @Test(groups = "jboss-as-broken")
   @SpecAssertion(section = "9.4", id = "fb")
   public void testInterceptorsDeclaredUsingInterceptorsCalledBeforeInterceptorBinding()
   {
      MissileInterceptor.intercepted = false;
      RadarInterceptor.intercepted = false;
      RadarInterceptor.interceptedFirst = false;

      MissileLocal missile = getInstanceByType(MissileLocal.class);
      missile.fire();

      assert MissileInterceptor.intercepted;
      assert RadarInterceptor.intercepted;
      assert RadarInterceptor.interceptedFirst;
   }
}