/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.AFTER_TYPE_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_PRODUCER;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_DISCOVERY_STEPS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd.lib.Bar;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd.lib.Baz;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd.lib.Boss;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd.lib.Foo;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd.lib.Pro;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class AfterTypeDiscoveryTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AfterTypeDiscoveryTest.class)
                .withExtension(AfterTypeDiscoveryObserver.class)
                .withLibrary(Boss.class, Foo.class, Bar.class, Baz.class, Pro.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL).interceptors(CharlieInterceptor.class)
                        .decorators(CharlieDecorator.class).alternatives(CharlieAlternative.class))
                .build();
    }

    @Inject
    AfterTypeDiscoveryObserver extension;

    @Test
    @SpecAssertions({ @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "a"), @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "c"),
            @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "hb") })
    public void testInitialInterceptors() {
        assertTrue(extension.getInterceptors().contains(BravoInterceptor.class));
        assertTrue(extension.getInterceptors().contains(AlphaInterceptor.class));
        assertTrue(extension.getInterceptors().contains(DeltaInterceptor.class));
        assertTrue(extension.getInterceptors().contains(EchoInterceptor.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "b"), @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "ha") })
    public void testInitialAlternatives() {
        // frameworks might add their own alternatives, we cannot assert positions in list but rather just ordering
        assertTrue(extension.getAlternatives().size() >= 3);
        List<Class<?>> alternatives = extension.getAlternatives();
        Integer alphaAltIndex = null;
        Integer echoAltIndex = null;
        Integer deltaAltIndex = null;
        for (int i = 0; i < alternatives.size(); i++) {
            if (alternatives.get(i).equals(AlphaAlternative.class)) {
                alphaAltIndex = i;
            }
            if (alternatives.get(i).equals(EchoAlternative.class)) {
                echoAltIndex = i;
            }
            if (alternatives.get(i).equals(DeltaAlternative.class)) {
                deltaAltIndex = i;
            }
        }
        assertNotNull(alphaAltIndex);
        assertNotNull(echoAltIndex);
        assertNotNull(deltaAltIndex);
        assertTrue(alphaAltIndex < echoAltIndex);
        assertTrue(echoAltIndex < deltaAltIndex);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "d"), @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "hc") })
    public void testInitialDecorators() {
        // frameworks might add their own decorators, we cannot assert positions in list but rather just ordering
        assertTrue(extension.getDecorators().size() >= 4);
        List<Class<?>> decorators = extension.getDecorators();
        Integer alphaDecIndex = null;
        Integer bravoDecIndex = null;
        Integer echoDecIndex = null;
        Integer deltaDecIndex = null;
        for (int i = 0; i < decorators.size(); i++) {
            if (decorators.get(i).equals(AlphaDecorator.class)) {
                alphaDecIndex = i;
            }
            if (decorators.get(i).equals(BravoDecorator.class)) {
                bravoDecIndex = i;
            }
            if (decorators.get(i).equals(EchoDecorator.class)) {
                echoDecIndex = i;
            }
            if (decorators.get(i).equals(DeltaDecorator.class)) {
                deltaDecIndex = i;
            }
        }
        assertNotNull(alphaDecIndex);
        assertNotNull(bravoDecIndex);
        assertNotNull(echoDecIndex);
        assertNotNull(deltaDecIndex);
        assertTrue(alphaDecIndex < bravoDecIndex);
        assertTrue(bravoDecIndex < echoDecIndex);
        assertTrue(echoDecIndex < deltaDecIndex);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "gb") })
    public void testFinalInterceptors(TransactionLogger logger) {

        AlphaInterceptor.reset();
        BravoInterceptor.reset();
        CharlieInterceptor.reset();
        DeltaInterceptor.reset();
        EchoInterceptor.reset();

        logger.ping();

        assertTrue(AlphaInterceptor.isIntercepted());
        assertFalse(BravoInterceptor.isIntercepted());
        assertTrue(CharlieInterceptor.isIntercepted());
        assertTrue(DeltaInterceptor.isIntercepted());
        assertFalse(EchoInterceptor.isIntercepted());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "gc") })

    public void testFinalDecorators(TransactionLogger logger) {
        assertEquals(logger.log("ping"), "pingdeltabravoalphacharlie");
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "ga") })
    public void testFinalAlternatives(TransactionLogger logger) {
        // assert that proper alternative is injected
        assertEquals(logger.getAlternativeClass(), DeltaAlternative.class);
        assertTrue(getBeans(AlphaAlternative.class).isEmpty());
        assertTrue(getBeans(EchoAlternative.class).isEmpty());
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertions({ @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "e"), @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "d") })
    public void testAddAnnotatedType() {
        assertTrue(extension.isBossObserved());
        getUniqueBean(Boss.class);

        assertEquals(getBeans(Bar.class).size(), 0);
        assertEquals(getBeans(Bar.class, new AnnotationLiteral<Pro>() {
        }).size(), 1);

        assertEquals(getBeans(Foo.class).size(), 0);
        assertEquals(getBeans(Foo.class, new AnnotationLiteral<Pro>() {
        }).size(), 1);
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertions({ @SpecAssertion(section = AFTER_TYPE_DISCOVERY, id = "ea") })
    public void testAddAnnotatedTypeWithConfigurator() {
        Bean<Baz> bazBean = getUniqueBean(Baz.class, Pro.ProLiteral.INSTANCE);
        assertEquals(bazBean.getScope(), RequestScoped.class);
        Baz baz = (Baz) getCurrentManager().getReference(bazBean, Baz.class, getCurrentManager().createCreationalContext(bazBean));
        assertFalse(baz.getBarInstance().isUnsatisfied());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_PRODUCER, id = "ab"), @SpecAssertion(section = PROCESS_PRODUCER, id = "bb") })
    public void testProcessProducerEventFiredForProducerField() {
        assertTrue(extension.isProcessProcuderFieldObserved());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_PRODUCER, id = "aa"), @SpecAssertion(section = PROCESS_PRODUCER, id = "ba") })
    public void testProcessProducerEventFiredForProducerMethod() {
        assertTrue(extension.isProcessProcuderMethodObserved());
    }

}
