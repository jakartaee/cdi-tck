package org.jboss.jsr299.tck.tests.implementation.builtin;

import java.security.Principal;

import javax.security.auth.login.LoginException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

/**
 * Section 3.6
 * 
 * @author Pete Muir
 * 
 */
@Artifact
@Packaging(PackagingType.EAR)
@IntegrationTest
@SpecVersion(spec="cdi", version="20091101")
public class BuiltInBeansTest extends AbstractJSR299Test
{

   @Test
   @SpecAssertions({
      @SpecAssertion(section="3.6", id="a")
   })
   public void testUserTransactionBean() throws SystemException
   {
      UserTransaction userTransaction = getInstanceByType(UserTransactionInjectedBeanLocal.class).getUserTransaction(); 
      assert userTransaction != null;
      // Check that the UserTransaction is at least queryable
      userTransaction.getStatus();
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section="3.6", id="c")
   })
   public void testDefaultValidatorFactoryBean() throws SystemException
   {
      ValidatorFactory defaultValidatorFactory = getInstanceByType(DefaultValidatorFactoryInjectedBeanLocal.class).getDefaultValidatorFactory(); 
      assert defaultValidatorFactory != null;
      // Check that the ValidatorFactory is at least queryable
      defaultValidatorFactory.getValidator();
   }
   
   @Test
   @SpecAssertions({
      @SpecAssertion(section="3.6", id="d")
   })
   public void testDefaultValidatorBean() throws SystemException
   {
      Validator defaultValidator = getInstanceByType(DefaultValidatorInjectedBeanLocal.class).getDefaultValidator(); 
      assert defaultValidator != null;
      // Check that the ValidatorFactory is at least queryable
      defaultValidator.getConstraintsForClass(BuiltInBeansTest.class);
   }
   
   @Test(groups="rewrite")
   // PLM We should check the Principal somehow
   @SpecAssertions({
      @SpecAssertion(section="3.6", id="b")
   })
   public void testPrincipalBean() throws SystemException, LoginException
   {
      PrincipalInjectedBeanLocal instance = getInstanceByType(PrincipalInjectedBeanLocal.class);
      instance.login();
      Principal principal = instance.getPrincipal();
      // Not much we can check on the Princiapl easily
   }
   
}
