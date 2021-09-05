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
package org.jboss.cdi.tck.tests.full.interceptors.definition.interceptorOrder;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.util.List;

import static org.jboss.cdi.tck.cdi.Sections.ENABLED_INTERCEPTORS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@SpecVersion(spec = "cdi", version = "2.0")
public class InterceptorOrderTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InterceptorOrderTest.class)
                .withBeansXml(new BeansXml().interceptors(TransactionalInterceptor.class))
                .build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "g")
    public void testInterceptorsInvocationOrder(AccountTransaction transaction) {

        assertNotNull(transaction);
        ActionSequence.reset();

        transaction.execute();

        List<String> sequence = ActionSequence.getSequenceData();
        assertEquals(sequence.size(), 4);
        assertEquals(sequence.get(0), AnotherInterceptor.class.getName());
        assertEquals(sequence.get(1), TransactionalInterceptor.class.getName());
        assertEquals(sequence.get(2), Transaction.class.getName());
        assertEquals(sequence.get(3), AccountTransaction.class.getName());
    }
}
