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
package org.jboss.cdi.tck.tests.extensions.configurators.observerMethod;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.event.Reception;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.interceptor.Interceptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@Test
@SpecVersion(spec = "cdi", version = "2.0-EDR1")
public class ObserverMethodConfiguratorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ObserverMethodConfiguratorTest.class)
                .withExtension(ProcessObserverMethodObserver.class).build();
    }

    @Test
    // FIXME remove or enable assertion "ba" in the future
    @SpecAssertions({ //@SpecAssertion(section = Sections.OBSERVER_METHOD_CONFIGURATOR, id = "ba"),
            @SpecAssertion(section = Sections.OBSERVER_METHOD_CONFIGURATOR, id = "bb") })
    public void changeBeanClassAndObservedType() {
        Set<ObserverMethod<? super Apple>> appleEventObservers = getCurrentManager().resolveObserverMethods(new Apple(), Any.Literal.INSTANCE);
        Assert.assertEquals(appleEventObservers.size(), 1);
        Assert.assertEquals(appleEventObservers.iterator().next().getBeanClass(), FoodObserver.class);
        Assert.assertEquals(appleEventObservers.iterator().next().getObservedType(), Apple.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = Sections.OBSERVER_METHOD_CONFIGURATOR, id = "bg"),
            @SpecAssertion(section = Sections.OBSERVER_METHOD_CONFIGURATOR, id = "bd"),
            @SpecAssertion(section = Sections.OBSERVER_METHOD_CONFIGURATOR, id = "bi") })
    public void addQualifiersAndSetPriority() {
        Set<ObserverMethod<? super Pear>> pearEventObservers = getCurrentManager()
                .resolveObserverMethods(new Pear(), Any.Literal.INSTANCE, Ripe.RipeLiteral.INSTANCE, Delicious.DeliciousLiteral.INSTANCE);
        Assert.assertEquals(pearEventObservers.size(), 1);
        Assert.assertEquals(pearEventObservers.iterator().next().getPriority(), Interceptor.Priority.APPLICATION + 100);
        Assert.assertEquals(pearEventObservers.iterator().next().isAsync(), true);
        Assert.assertEquals(pearEventObservers.iterator().next().getObservedQualifiers(),
                Stream.of(Ripe.RipeLiteral.INSTANCE, Delicious.DeliciousLiteral.INSTANCE).collect(
                        Collectors.toSet()));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = Sections.OBSERVER_METHOD_CONFIGURATOR, id = "be"),
            @SpecAssertion(section = Sections.OBSERVER_METHOD_CONFIGURATOR, id = "bf") })
    public void setReceptionAndTransactionPhase() {
        Set<ObserverMethod<? super Orange>> orangeEventObservers = getCurrentManager()
                .resolveObserverMethods(new Orange(), Any.Literal.INSTANCE, Delicious.DeliciousLiteral.INSTANCE);
        Assert.assertEquals(orangeEventObservers.size(), 1);
        Assert.assertEquals(orangeEventObservers.iterator().next().getReception(), Reception.IF_EXISTS);
        Assert.assertEquals(orangeEventObservers.iterator().next().getTransactionPhase(), TransactionPhase.AFTER_SUCCESS);
        Assert.assertEquals(orangeEventObservers.iterator().next().getObservedQualifiers(), Collections.singleton(Delicious.DeliciousLiteral.INSTANCE));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = Sections.OBSERVER_METHOD_CONFIGURATOR, id = "bc"),
            @SpecAssertion(section = Sections.OBSERVER_METHOD_CONFIGURATOR, id = "bh") })
    public void notifyAcceptingConsumerNotified() {
        getCurrentManager().fireEvent(new Pineapple(), Delicious.DeliciousLiteral.INSTANCE);
        Assert.assertTrue(ProcessObserverMethodObserver.consumerNotified.get());
        Assert.assertEquals(ProcessObserverMethodObserver.pineAppleQualifiers, Arrays.asList(Any.Literal.INSTANCE, Delicious.DeliciousLiteral.INSTANCE));
    }
}
