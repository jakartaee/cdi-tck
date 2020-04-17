/*
 * JBoss, Home of Professional Open Source
 * Copyright 2018, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.lifecycle.atd.massOperations;

import static org.jboss.arquillian.testng.Arquillian.ARQUILLIAN_DATA_PROVIDER;
import static org.jboss.cdi.tck.cdi.Sections.AFTER_TYPE_DISCOVERY;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.extensions.lifecycle.atd.Alternatives;
import org.jboss.cdi.tck.tests.extensions.lifecycle.atd.Logger;
import org.jboss.cdi.tck.tests.extensions.lifecycle.atd.Monitored;
import org.jboss.cdi.tck.tests.extensions.lifecycle.atd.TransactionLogger;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.testng.annotations.Test;

/**
 * Complementary test to AfterTypeDiscoveryTest using the same classes but focusing on mass operations on List
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class AfterTypeDiscoveryMassOperationsTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
            .withTestClassPackage(AfterTypeDiscoveryMassOperationsTest.class)
            .withClasses(TransactionLogger.class, Logger.class, Monitored.class, Alternatives.class)
            .withExtension(AfterTypeBeanDiscoveryMassOperationObserver.class)
            .build();
    }

    @Inject
    AfterTypeBeanDiscoveryMassOperationObserver extension;

    @Test
    @SpecAssertions({
        @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "a"),
        @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "c"),
        @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "hb") })
    public void testInitialInterceptors() {
        assertTrue(extension.getInterceptors().contains(AlphaInterceptor.class));
        assertTrue(extension.getInterceptors().contains(BetaInterceptor.class));
        assertTrue(extension.getInterceptors().contains(GammaInterceptor.class));
    }

    @Test
    @SpecAssertions({
        @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "b"),
        @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "ha") })
    public void testInitialAlternatives() {
        assertEquals(extension.getAlternatives().size(), 3);
        assertEquals(extension.getAlternatives().get(0), AlphaAlternative.class);
        assertEquals(extension.getAlternatives().get(1), BetaAlternative.class);
        assertEquals(extension.getAlternatives().get(2), GammaAlternative.class);
    }

    @Test
    @SpecAssertions({
        @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "d"),
        @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "hc") })
    public void testInitialDecorators() {
        assertEquals(extension.getDecorators().size(), 3);
        assertEquals(extension.getDecorators().get(0), AlphaDecorator.class);
        assertEquals(extension.getDecorators().get(1), BetaDecorator.class);
        assertEquals(extension.getDecorators().get(2), GammaDecorator.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({
        @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "gb") })
    public void testFinalInterceptors(TransactionLogger logger) {

        AlphaInterceptor.reset();
        BetaInterceptor.reset();
        GammaInterceptor.reset();

        logger.ping();

        assertTrue(AlphaInterceptor.isIntercepted());
        assertTrue(BetaInterceptor.isIntercepted());
        assertTrue(GammaInterceptor.isIntercepted());
        
        assertTrue(extension.containsWorks());
        assertTrue(extension.containsAllWorks());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({
        @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "gc") })
    public void testFinalDecorators(TransactionLogger logger) {
        assertEquals(logger.log("ping"), "pinggamma");
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({
        @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "ga") })
    public void testFinalAlternatives(TransactionLogger logger) {
        // assert that proper alternative is injected
        assertEquals(logger.getAlternativeClass(), GammaAlternative.class);
        assertTrue(getBeans(AlphaAlternative.class).isEmpty());
        assertTrue(getBeans(BetaAlternative.class).isEmpty());
    }
}
