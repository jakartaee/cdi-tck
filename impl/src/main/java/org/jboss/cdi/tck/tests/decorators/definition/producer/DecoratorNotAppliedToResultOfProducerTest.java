/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.decorators.definition.producer;

import static org.jboss.cdi.tck.cdi.Sections.DECORATORS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class DecoratorNotAppliedToResultOfProducerTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(DecoratorNotAppliedToResultOfProducerTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).getOrCreateDecorators().clazz(ChargeDecorator.class.getName())
                                .up()).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = DECORATORS, id = "b")
    public void testDecoratorNotAppliedToResultOfProducerMethod(ShortTermAccount account,
            @Synthetic ShortTermAccount producedAccount) {
        assertNotNull(account);
        assertNotNull(producedAccount);

        account.deposit(10);
        account.withdraw(5);
        // ChargeDecorator is applied
        assertEquals(account.getBalance(), 0);

        producedAccount.deposit(10);
        producedAccount.withdraw(5);
        // ChargeDecorator is not applied
        assertEquals(producedAccount.getBalance(), 5);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = DECORATORS, id = "b")
    public void testDecoratorNotAppliedToResultOfProducerField(DurableAccount account, @Synthetic DurableAccount producedAccount) {
        assertNotNull(account);
        assertNotNull(producedAccount);

        account.deposit(20);
        account.withdraw(25);
        // ChargeDecorator is applied
        assertEquals(account.getBalance(), -10);

        producedAccount.deposit(20);
        producedAccount.withdraw(25);
        // ChargeDecorator is not applied
        assertEquals(producedAccount.getBalance(), -5);
    }

}
