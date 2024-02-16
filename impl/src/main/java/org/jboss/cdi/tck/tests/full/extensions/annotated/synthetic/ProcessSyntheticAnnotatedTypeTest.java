/*
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.annotated.synthetic;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_ANNOTATED_TYPE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Map;
import java.util.Set;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * </p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class ProcessSyntheticAnnotatedTypeTest extends AbstractTest {

    @Inject
    private VerifyingExtension verifyingExtension;

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(ProcessSyntheticAnnotatedTypeTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withExtensions(RegisteringAnnotationExtension.class, RegisteringExtension1.class, RegisteringExtension2.class,
                        RegisteringExtension3.class, ModifyingExtension.class, VerifyingExtension.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "ae"), @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "af"),
            @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "j"), @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "ag"),
            @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "ah") })
    public void testEventsFired() {
        Set<Class<?>> patClasses = verifyingExtension.getPatClasses();
        Set<Class<?>> psatClasses = verifyingExtension.getPsatClasses();

        assertTrue(psatClasses.contains(Orange.class));
        assertTrue(psatClasses.contains(Apple.class));
        assertTrue(psatClasses.contains(Pear.class));
        assertTrue(psatClasses.contains(Vegetables.class));
        // Also verify that PAT is fired for classes in a BDA
        assertTrue(patClasses.contains(Orange.class));
        assertTrue(patClasses.contains(Apple.class));
        assertTrue(patClasses.contains(Pear.class));

        // Verify that PAT is not fired for annotation type
        assertFalse(psatClasses.contains(TestAnnotation.class));
        assertFalse(psatClasses.contains(Juicy.class));

        // Test changes applied
        Set<Bean<?>> oranges = getCurrentManager().getBeans(Orange.class, Any.Literal.INSTANCE);
        assertEquals(oranges.size(), 1);
        assertFalse(oranges.iterator().next().getQualifiers().contains(Juicy.Literal.INSTANCE));
        Set<Bean<?>> apples = getCurrentManager().getBeans(Apple.class, Any.Literal.INSTANCE);
        assertEquals(apples.size(), 2);
        Set<Bean<?>> juicyApples = getCurrentManager().getBeans(Apple.class, Juicy.Literal.INSTANCE);
        assertEquals(juicyApples.size(), 1);
        assertTrue(juicyApples.iterator().next().getQualifiers().contains(Fresh.Literal.INSTANCE));
        assertEquals(getCurrentManager().getBeans(Pear.class, Any.Literal.INSTANCE).size(), 2);
        Set<Bean<?>> juicyPears = getCurrentManager().getBeans(Pear.class, Juicy.Literal.INSTANCE);
        assertEquals(juicyPears.size(), 1);
        Set<Bean<?>> annotation = getCurrentManager().getBeans(TestAnnotation.class, Any.Literal.INSTANCE);
        assertEquals(annotation.size(), 0);

    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "bd") })
    public void testEventsSources() {
        Map<Class<?>, Extension> sources = verifyingExtension.getSources();
        assertTrue(sources.get(Apple.class) instanceof RegisteringExtension1);
        assertTrue(sources.get(Orange.class) instanceof RegisteringExtension1);
        assertTrue(sources.get(Pear.class) instanceof RegisteringExtension2);
    }
}
