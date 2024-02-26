/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.ENABLED_INTERCEPTORS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class InterceptorOrderTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InterceptorOrderTest.class)
                .withBeansXml(new BeansXml().interceptors(SecondInterceptor.class, FirstInterceptor.class,
                        TransactionalInterceptor.class))
                .build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "b"),
            @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "c") })
    public void testInterceptorsCalledInOrderDefinedByBeansXml(Foo foo) {

        assertNotNull(foo);
        ActionSequence.reset();

        foo.bar();

        List<String> sequence = ActionSequence.getSequenceData();
        assertEquals(sequence.size(), 2);
        assertEquals(sequence.get(0), SecondInterceptor.class.getName());
        assertEquals(sequence.get(1), FirstInterceptor.class.getName());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "g")
    public void testInterceptorsInvocationOrder(AccountTransaction transaction) {

        assertNotNull(transaction);
        ActionSequence.reset();

        transaction.execute();

        List<String> sequence = ActionSequence.getSequenceData();
        assertEquals(sequence.size(), 4, sequence.toString());
        assertEquals(sequence.get(0), AnotherInterceptor.class.getName());
        assertEquals(sequence.get(1), TransactionalInterceptor.class.getName());
        assertEquals(sequence.get(2), Transaction.class.getName());
        assertEquals(sequence.get(3), AccountTransaction.class.getName());
    }
}
