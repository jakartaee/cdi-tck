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
package org.jboss.cdi.tck.tests.interceptors.definition.lifecycle.order;

import static org.jboss.cdi.tck.cdi.Sections.ENABLED_INTERCEPTORS;
import static org.testng.Assert.assertEquals;

import java.util.List;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class LifecycleInterceptorOrderTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(LifecycleInterceptorOrderTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).getOrCreateInterceptors()
                                .clazz(TransactionalInterceptor.class.getName()).up()).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "g"),
            @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "h") })
    public void testLifecycleCallbackInvocationOrder() {

        ActionSequence.reset();

        Bean<AccountTransaction> bean = getUniqueBean(AccountTransaction.class);
        CreationalContext<AccountTransaction> ctx = getCurrentManager().createCreationalContext(bean);
        AccountTransaction transaction = bean.create(ctx);
        transaction.execute();
        bean.destroy(transaction, ctx);

        List<String> postConstruct = ActionSequence.getSequenceData("postConstruct");
        assertEquals(postConstruct.size(), 4);
        assertEquals(postConstruct.get(0), AnotherInterceptor.class.getName());
        assertEquals(postConstruct.get(1), TransactionalInterceptor.class.getName());
        assertEquals(postConstruct.get(2), Transaction.class.getName());
        assertEquals(postConstruct.get(3), AccountTransaction.class.getName());

        List<String> preDestroy = ActionSequence.getSequenceData("preDestroy");
        assertEquals(preDestroy.size(), 4);
        assertEquals(postConstruct.get(0), AnotherInterceptor.class.getName());
        assertEquals(postConstruct.get(1), TransactionalInterceptor.class.getName());
        assertEquals(postConstruct.get(2), Transaction.class.getName());
        assertEquals(postConstruct.get(3), AccountTransaction.class.getName());
    }
}
