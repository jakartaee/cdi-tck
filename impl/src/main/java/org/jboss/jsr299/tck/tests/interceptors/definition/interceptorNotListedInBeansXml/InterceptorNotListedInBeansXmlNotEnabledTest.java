package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorNotListedInBeansXml;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.jsr299.BeansXml;
import org.testng.annotations.Test;

@Artifact
@SpecVersion(spec="cdi", version="PFD2")
@BeansXml("beans.xml")
public class InterceptorNotListedInBeansXmlNotEnabledTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "9.4", id = "a")
   public void testInterceptorNotListedInBeansXmlNotInvoked()
   {
      TransactionInterceptor.invoked = false;
      
      AccountHolder accountHolder = getInstanceByType(AccountHolder.class);
      accountHolder.transfer(0);
      
      assert !TransactionInterceptor.invoked;
   }
}
