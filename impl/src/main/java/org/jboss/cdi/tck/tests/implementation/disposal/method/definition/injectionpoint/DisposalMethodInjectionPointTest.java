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
package org.jboss.cdi.tck.tests.implementation.disposal.method.definition.injectionpoint;

import static org.jboss.cdi.tck.TestGroups.DISPOSAL;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class DisposalMethodInjectionPointTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DisposalMethodInjectionPointTest.class).build();
    }

    /**
     * Test that the InjectionPoint injected into a disposer method represents the producer method for which the disposer method
     * is being invoked.
     */
    @Test(groups = { DISPOSAL })
    @SpecAssertion(section = "5.5.7", id = "h")
    public void test() {
        BarProducer.reset();
        Bean<BarConsumer> bean = getBeans(BarConsumer.class).iterator().next();
        CreationalContext<BarConsumer> creationalContext = getCurrentManager().createCreationalContext(bean);
        BarConsumer instance = bean.create(creationalContext);
        Assert.assertEquals(BarProducer.getProducedInjectionPointMember().getName(), "bar");
        Bar bar = instance.getBar();
        bean.destroy(instance, creationalContext);
        Assert.assertEquals(BarProducer.getDisposedBar(), bar);
        Assert.assertEquals(BarProducer.getDisposedInjectionPointMember().getName(), "bar");
        Assert.assertTrue(BarProducer.getDisposedInjectionPointMember().equals(BarProducer.getProducedInjectionPointMember()));
    }

}
