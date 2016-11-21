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

import static org.jboss.cdi.tck.cdi.Sections.AFTER_BEAN_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_METHOD_CONFIGURATOR;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_OBSERVER_METHOD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.event.Event;
import javax.enterprise.event.Reception;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.ObservesLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@Test
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class ObserverMethodConfiguratorTest extends AbstractTest {

    @Inject
    Event<Pear> pearEvent;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ObserverMethodConfiguratorTest.class)
                .withClass(ObservesLiteral.class)
                .withExtensions(ProcessObserverMethodObserver.class, AfterBeanDiscoveryObserver.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_METHOD_CONFIGURATOR, id = "bg"),
            @SpecAssertion(section = OBSERVER_METHOD_CONFIGURATOR, id = "bd"),
            @SpecAssertion(section = OBSERVER_METHOD_CONFIGURATOR, id = "bi") })
    public void addQualifiersAndSetPriorityAndChangeToAsync() throws InterruptedException {
        Set<ObserverMethod<? super Pear>> pearEventObservers = getCurrentManager()
                .resolveObserverMethods(new Pear(), Any.Literal.INSTANCE, Ripe.RipeLiteral.INSTANCE, Delicious.DeliciousLiteral.INSTANCE);
        assertEquals(pearEventObservers.size(), 1);
        assertEquals(pearEventObservers.iterator().next().getPriority(), ObserverMethod.DEFAULT_PRIORITY + 100);
        assertEquals(pearEventObservers.iterator().next().isAsync(), true);
        assertEquals(pearEventObservers.iterator().next().getObservedQualifiers(),
                Stream.of(Ripe.RipeLiteral.INSTANCE, Delicious.DeliciousLiteral.INSTANCE).collect(
                        Collectors.toSet()));

        BlockingQueue<Pear> queue = new LinkedBlockingQueue<>();
        pearEvent.select(Any.Literal.INSTANCE, Ripe.RipeLiteral.INSTANCE, Delicious.DeliciousLiteral.INSTANCE).fireAsync(new Pear()).thenAccept(queue::offer);
        Pear pear = queue.poll(2, TimeUnit.SECONDS);
        assertNotNull(pear);
        assertTrue(FruitObserver.pearObserverNotified.get());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_METHOD_CONFIGURATOR, id = "be"),
            @SpecAssertion(section = OBSERVER_METHOD_CONFIGURATOR, id = "bf") })
    public void setReceptionAndTransactionPhase() {
        Set<ObserverMethod<? super Orange>> orangeEventObservers = getCurrentManager()
                .resolveObserverMethods(new Orange(), Any.Literal.INSTANCE, Delicious.DeliciousLiteral.INSTANCE);
        assertEquals(orangeEventObservers.size(), 1);
        assertEquals(orangeEventObservers.iterator().next().getReception(), Reception.IF_EXISTS);
        assertEquals(orangeEventObservers.iterator().next().getTransactionPhase(), TransactionPhase.AFTER_SUCCESS);
        assertEquals(orangeEventObservers.iterator().next().getObservedQualifiers(), Collections.singleton(Delicious.DeliciousLiteral.INSTANCE));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_METHOD_CONFIGURATOR, id = "bc"),
            @SpecAssertion(section = OBSERVER_METHOD_CONFIGURATOR, id = "bh") })
    public void notifyAcceptingConsumerNotified() {
        getCurrentManager().fireEvent(new Pineapple(), Any.Literal.INSTANCE, Delicious.DeliciousLiteral.INSTANCE);
        assertTrue(ProcessObserverMethodObserver.consumerNotified.get());
        assertEquals(ProcessObserverMethodObserver.pineAppleQualifiers, Arrays.asList(Any.Literal.INSTANCE, Delicious.DeliciousLiteral.INSTANCE));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_METHOD_CONFIGURATOR, id = "aa"),
            @SpecAssertion(section = OBSERVER_METHOD_CONFIGURATOR, id = "ab"),
            @SpecAssertion(section = OBSERVER_METHOD_CONFIGURATOR, id = "ac"),
            @SpecAssertion(section = OBSERVER_METHOD_CONFIGURATOR, id = "ba"),
            @SpecAssertion(section = OBSERVER_METHOD_CONFIGURATOR, id = "bb"),
            @SpecAssertion(section = AFTER_BEAN_DISCOVERY, id = "ed")})
    public void addNewObserverMethodFromReadingExistingOne() {
        AfterBeanDiscoveryObserver.reset();
        getCurrentManager().fireEvent(new Banana(), Any.Literal.INSTANCE, Ripe.RipeLiteral.INSTANCE);
        getCurrentManager().fireEvent(new Melon(), Any.Literal.INSTANCE);
        getCurrentManager().fireEvent(new Peach(), Any.Literal.INSTANCE);
        Set<ObserverMethod<? super Peach>> peachEventObservers = getCurrentManager().resolveObserverMethods(new Peach(), Any.Literal.INSTANCE);
        Set<ObserverMethod<? super Banana>> bananaEventObservers = getCurrentManager()
                .resolveObserverMethods(new Banana(), Any.Literal.INSTANCE, Ripe.RipeLiteral.INSTANCE);
        // one in FruitObserver and second one added in AfterBeanDiscoveryObserver
        assertEquals(peachEventObservers.size(), 2);
        assertEquals(bananaEventObservers.size(), 2);
        assertTrue(AfterBeanDiscoveryObserver.newBananaObserverNotified.get());
        assertTrue(AfterBeanDiscoveryObserver.newMelonObserverNotified.get());
        assertTrue(AfterBeanDiscoveryObserver.newPeachObserverNotified.get());

        assertTrue(FruitObserver.melonObserverNotified.get());
        assertTrue(FruitObserver.peachObserverNotified.get());
        assertTrue(FruitObserver.bananaObserverNotified.get());
    }

    @Test
    @SpecAssertion(section = PROCESS_OBSERVER_METHOD, id = "dab")
    public void configuratorInitializedWithOriginalObserverMethod() {
        ObserverMethod<? super Kiwi> configuredOne = getCurrentManager().resolveObserverMethods(new Kiwi(), Ripe.RipeLiteral.INSTANCE).iterator().next();
        ObserverMethod<Kiwi> originalOne = getCurrentManager().getExtension(ProcessObserverMethodObserver.class).getOriginalOM();
        assertEquals(configuredOne.getObservedType(), originalOne.getObservedType());
        assertEquals(configuredOne.getObservedQualifiers(), originalOne.getObservedQualifiers());
        assertEquals(configuredOne.getPriority(), originalOne.getPriority());
    }
}
