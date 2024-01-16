/*
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
package org.jboss.cdi.tck.tests.full.decorators.invocation.producer.method;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BIZ_METHOD;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author pmuir
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class DecoratorInvocationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DecoratorInvocationTest.class).withBeansXml("beans.xml").build();
    }

    @Test(groups = CDI_FULL)
    @SpecAssertions({ @SpecAssertion(section = BIZ_METHOD, id = "ib"), @SpecAssertion(section = BIZ_METHOD, id = "id") })
    public void testDecoratorInvocation() {
        ProducerDecorator.reset();
        ProducerImpl.reset();
        Bean<Foo> bean = (Bean<Foo>) getCurrentManager().resolve(getCurrentManager().getBeans(Foo.class));
        CreationalContext<Foo> creationalContext = getCurrentManager().createCreationalContext(bean);
        Foo foo = bean.create(creationalContext);
        assert foo.getFoo().equals("foo!!!");
        bean.destroy(foo, creationalContext);
        assert ProducerImpl.isDisposedCorrectly();
    }

}
