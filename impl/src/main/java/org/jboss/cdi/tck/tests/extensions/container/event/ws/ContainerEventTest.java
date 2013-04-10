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
package org.jboss.cdi.tck.tests.extensions.container.event.ws;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.JAX_WS;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.PIT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test verifies that ProcessInjectionTarget event is fired for web service endpoint (JAX-WS).
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class ContainerEventTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static EnterpriseArchive createTestArchive() {
        return new EnterpriseArchiveBuilder().withTestClassPackage(ContainerEventTest.class)
                .withExtensions(ProcessInjectionTargetObserver.class, ProcessAnnotatedTypeObserver.class).build();
    }

    @Test(groups = {JAVAEE_FULL, JAX_WS})
    @SpecAssertions({ @SpecAssertion(section = PIT, id = "aag"), @SpecAssertion(section = PIT, id = "abg"),
            @SpecAssertion(section = BEAN_DISCOVERY, id = "di") })
    public void testProcessInjectionTargetFiredForWsEndpoint() {
        assertNotNull(ProcessInjectionTargetObserver.getWsEndpointEvent());
        validateWsEndpointAnnotatedType(ProcessInjectionTargetObserver.getWsEndpointEvent().getAnnotatedType());
    }

    @Test(groups = {JAVAEE_FULL, JAX_WS})
    @SpecAssertions({ @SpecAssertion(section = BEAN_DISCOVERY, id = "bi") })
    public void testProcessAnnotatedTypeFiredForWsEndpoint() {
        assertNotNull(ProcessAnnotatedTypeObserver.getWsEndpointEvent());
        validateWsEndpointAnnotatedType(ProcessAnnotatedTypeObserver.getWsEndpointEvent().getAnnotatedType());
    }

    private void validateWsEndpointAnnotatedType(AnnotatedType<TranslatorEndpoint> annotatedType) {
        assertEquals(annotatedType.getBaseType(), TranslatorEndpoint.class);
        // translate()
        assertEquals(annotatedType.getMethods().size(), 1);
        Set<Type> typeClosure = annotatedType.getTypeClosure();
        // Translator, TranslatorEndpoint, Object
        assertTrue(typeSetMatches(typeClosure, Translator.class, TranslatorEndpoint.class, Object.class));
    }

}
