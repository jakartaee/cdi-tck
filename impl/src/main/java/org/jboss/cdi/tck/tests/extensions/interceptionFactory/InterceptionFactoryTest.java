/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.interceptionFactory;

import static org.jboss.cdi.tck.cdi.Sections.INTERCEPTION_FACTORY;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InterceptionFactory;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class InterceptionFactoryTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InterceptionFactoryTest.class)
                .build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = INTERCEPTION_FACTORY, id = "b"), @SpecAssertion(section = INTERCEPTION_FACTORY, id = "c") })
    public void producedInstanceIsIntercepted(Product product) {
        ActionSequence.reset();
        Assert.assertEquals(product.ping(), 4);
        ActionSequence.assertSequenceDataEquals(ProductInterceptor1.class, ProductInterceptor2.class, ProductInterceptor3.class);
    }

    @Test
    @SpecAssertion(section = INTERCEPTION_FACTORY, id = "g")
    public void interceptionFactoryBeanIsAvailable() {
        Bean<?> interceptionFactoryBean = getCurrentManager().resolve(getCurrentManager().getBeans(InterceptionFactory.class));
        Assert.assertEquals(Dependent.class, interceptionFactoryBean.getScope());
        Assert.assertEquals(Stream.of(Default.Literal.INSTANCE, Any.Literal.INSTANCE).collect(Collectors.toSet()), interceptionFactoryBean.getQualifiers());
    }
}
