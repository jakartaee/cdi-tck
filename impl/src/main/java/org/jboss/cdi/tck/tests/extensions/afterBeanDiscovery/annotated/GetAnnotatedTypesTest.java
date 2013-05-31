/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.extensions.afterBeanDiscovery.annotated;

import static org.jboss.cdi.tck.cdi.Sections.ABD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.literals.InjectLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.extensions.afterBeanDiscovery.annotated.Alpha.AlphaLiteral;
import org.jboss.cdi.tck.tests.extensions.afterBeanDiscovery.annotated.Bravo.BravoLiteral;
import org.jboss.cdi.tck.tests.extensions.afterBeanDiscovery.annotated.Charlie.CharlieLiteral;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class GetAnnotatedTypesTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(GetAnnotatedTypesTest.class)
                .withExtension(ModifyingExtension.class).build();
    }

    @Inject
    ModifyingExtension extension;

    @Test
    @SpecAssertions({ @SpecAssertion(section = ABD, id = "h") })
    public void testGetAnnotatedType() {

        AnnotatedType<Foo> aplha = extension.getAplha();
        assertNotNull(aplha);
        assertEquals(aplha.getAnnotations().size(), 1);
        // @RequestScoped was replaced by @Alpha
        assertEquals(aplha.getAnnotations().iterator().next(), AlphaLiteral.INSTANCE);
        assertEquals(aplha.getMethods().size(), 1);
        assertEquals(aplha.getMethods().iterator().next().getAnnotations().size(), 1);
        assertEquals(aplha.getMethods().iterator().next().getAnnotations().iterator().next(), InjectLiteral.INSTANCE);

        AnnotatedType<Foo> bravo = extension.getBravo();
        assertNotNull(bravo);
        assertEquals(bravo.getAnnotations().size(), 2);
        assertTrue(bravo.getAnnotations().contains(BravoLiteral.INSTANCE));
        assertTrue(bravo.getAnnotations().contains(AnyLiteral.INSTANCE));

        AnnotatedType<Foo> charlie = extension.getCharlie();
        assertNotNull(charlie);
        assertEquals(charlie.getAnnotations().size(), 1);
        assertEquals(charlie.getAnnotations().iterator().next(), CharlieLiteral.INSTANCE);

        AnnotatedType<Bar> bar = extension.getBar();
        assertNotNull(bar);
        // @Named, @RequestScoped
        assertEquals(bar.getAnnotations().size(), 2);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ABD, id = "i") })
    public void testGetAnnotatedTypes() {

        List<AnnotatedType<Foo>> allFoo = extension.getAllFoo();
        assertEquals(allFoo.size(), 3);
        assertTrue(allFoo.contains(extension.getAplha()));
        assertTrue(allFoo.contains(extension.getBravo()));
        assertTrue(allFoo.contains(extension.getCharlie()));
    }

}
