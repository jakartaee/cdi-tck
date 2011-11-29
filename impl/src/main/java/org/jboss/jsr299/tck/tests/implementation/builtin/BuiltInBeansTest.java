/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jsr299.tck.tests.implementation.builtin;

import static org.jboss.jsr299.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.jsr299.tck.TestGroups.REWRITE;

import java.security.Principal;

import javax.security.auth.login.LoginException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Pete Muir
 * @author Martin Kouba
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "cdi", version = "20091101")
public class BuiltInBeansTest extends AbstractJSR299Test {

    @Deployment
    public static EnterpriseArchive createTestArchive() {
        return new EnterpriseArchiveBuilder().withTestClassPackage(BuiltInBeansTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.7", id = "a") })
    public void testUserTransactionBean() throws SystemException {
        UserTransaction userTransaction = getInstanceByType(UserTransactionInjectedBeanLocal.class).getUserTransaction();
        assert userTransaction != null;
        // Check that the UserTransaction is at least queryable
        userTransaction.getStatus();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.7", id = "c") })
    public void testDefaultValidatorFactoryBean() throws SystemException {
        ValidatorFactory defaultValidatorFactory = getInstanceByType(DefaultValidatorFactoryInjectedBeanLocal.class)
                .getDefaultValidatorFactory();
        assert defaultValidatorFactory != null;
        // Check that the ValidatorFactory is at least queryable
        defaultValidatorFactory.getValidator();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.7", id = "d") })
    public void testDefaultValidatorBean() throws SystemException {
        Validator defaultValidator = getInstanceByType(DefaultValidatorInjectedBeanLocal.class).getDefaultValidator();
        assert defaultValidator != null;
        // Check that the ValidatorFactory is at least queryable
        defaultValidator.getConstraintsForClass(BuiltInBeansTest.class);
    }

    /**
     * FIXME in jbossas7 web profile login-module is disabled
     * 
     * @throws SystemException
     * @throws LoginException
     */
    @Test(groups = REWRITE)
    // PLM We should check the Principal somehow
    @SpecAssertions({ @SpecAssertion(section = "3.7", id = "b") })
    public void testPrincipalBean() throws SystemException, LoginException {
        PrincipalInjectedBeanLocal instance = getInstanceByType(PrincipalInjectedBeanLocal.class);
        instance.login();
        Principal principal = instance.getPrincipal();
        // Not much we can check on the Princiapl easily
    }

}
