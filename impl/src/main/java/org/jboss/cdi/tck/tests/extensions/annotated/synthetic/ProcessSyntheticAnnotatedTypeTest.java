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
package org.jboss.cdi.tck.tests.extensions.annotated.synthetic;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.extensions.alternative.metadata.AnnotatedTypeWrapper;
import org.jboss.cdi.tck.tests.extensions.alternative.metadata.AnnotatedWrapper;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * Temporarily marked as integration tests - see SHRINKWRAP-369.
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class ProcessSyntheticAnnotatedTypeTest extends AbstractTest {

    @Inject
    private VerifyingExtension verifyingExtension;

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(ProcessSyntheticAnnotatedTypeTest.class)
                .withClasses(AnnotatedTypeWrapper.class, AnnotatedWrapper.class)
                .withExtensions(RegisteringExtension1.class, RegisteringExtension2.class, ModifyingExtension.class,
                        VerifyingExtension.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.6", id = "ab") })
    public void testEventsFired() {
        Set<Class<?>> patClasses = verifyingExtension.getPatClasses();
        Set<Class<?>> psatClasses = verifyingExtension.getPsatClasses();
        assertEquals(psatClasses.size(), 3);
        assertTrue(psatClasses.contains(Orange.class));
        assertTrue(psatClasses.contains(Apple.class));
        assertTrue(psatClasses.contains(Pear.class));
        // Also verify that PAT is fired for classes in a BDA
        assertTrue(patClasses.contains(Orange.class));
        assertTrue(patClasses.contains(Apple.class));
        assertTrue(patClasses.contains(Pear.class));

        // Test changes applied
        Set<Bean<?>> oranges = getCurrentManager().getBeans(Orange.class, AnyLiteral.INSTANCE);
        assertEquals(oranges.size(), 1);
        assertFalse(oranges.iterator().next().getQualifiers().contains(Juicy.Literal.INSTANCE));
        Set<Bean<?>> apples = getCurrentManager().getBeans(Apple.class, AnyLiteral.INSTANCE);
        assertEquals(apples.size(), 2);
        Set<Bean<?>> juicyApples = getCurrentManager().getBeans(Apple.class, Juicy.Literal.INSTANCE);
        assertEquals(juicyApples.size(), 1);
        assertTrue(juicyApples.iterator().next().getQualifiers().contains(Fresh.Literal.INSTANCE));
        assertEquals(getCurrentManager().getBeans(Pear.class, AnyLiteral.INSTANCE).size(), 2);
        Set<Bean<?>> juicyPears = getCurrentManager().getBeans(Pear.class, Juicy.Literal.INSTANCE);
        assertEquals(juicyPears.size(), 1);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.6", id = "bd") })
    public void testEventsSources() {
        Map<Class<?>, Extension> sources = verifyingExtension.getSources();
        assertTrue(sources.get(Apple.class) instanceof RegisteringExtension1);
        assertTrue(sources.get(Orange.class) instanceof RegisteringExtension1);
        assertTrue(sources.get(Pear.class) instanceof RegisteringExtension2);
    }

}
