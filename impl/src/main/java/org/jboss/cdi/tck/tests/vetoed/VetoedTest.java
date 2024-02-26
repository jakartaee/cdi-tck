/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.vetoed;

import static org.jboss.cdi.tck.cdi.Sections.WHAT_CLASSES_ARE_BEANS;
import static org.testng.Assert.assertEquals;

import jakarta.enterprise.inject.Any;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.vetoed.aquarium.Fishy;
import org.jboss.cdi.tck.tests.vetoed.aquarium.Piranha;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * A simplified version of {@code org.jboss.cdi.tck.tests.full.vetoed.VetoedTest} which doesn't use extensions.
 *
 * @author Matej Novotny
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class VetoedTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(VetoedTest.class)
                .withClasses(Elephant.class, Shark.class).withPackage(Piranha.class.getPackage())
                .build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS, id = "h") })
    public void testClassLevelVeto() {
        assertEquals(getCurrentManager().getBeans(Elephant.class, Any.Literal.INSTANCE).size(), 0);
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertions({ @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS, id = "h") })
    public void testPackageLevelVeto() {
        assertEquals(getCurrentManager().getBeans(Piranha.class, Any.Literal.INSTANCE).size(), 0);
        assertEquals(getCurrentManager().getBeans(Shark.class, Any.Literal.INSTANCE).size(), 1);
        assertEquals(getCurrentManager().getBeans(Shark.class, new Fishy.Literal()).size(), 1);
    }

}
