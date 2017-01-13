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
package org.jboss.cdi.tck.tests.implementation.initializer;

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_INITIALIZER;
import static org.jboss.cdi.tck.cdi.Sections.FIELDS_INITIALIZER_METHODS;
import static org.jboss.cdi.tck.cdi.Sections.INITIALIZER_METHODS;
import static org.jboss.cdi.tck.cdi.Sections.INJECTION_POINT_DEFAULT_QUALIFIER;
import static org.jboss.cdi.tck.cdi.Sections.METHOD_CONSTRUCTOR_PARAMETER_QUALIFIERS;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0-PFD")
public class InitializerMethodTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InitializerMethodTest.class)
                .withExcludedClass(EjbInitializerMethodTest.class.getName()).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_INITIALIZER, id = "f"), @SpecAssertion(section = METHOD_CONSTRUCTOR_PARAMETER_QUALIFIERS, id = "b"),
            @SpecAssertion(section = INITIALIZER_METHODS, id = "a") })
    public void testBindingTypeOnInitializerParameter() {
        PremiumChickenHutch hutch = getContextualReference(PremiumChickenHutch.class);
        assert hutch.getChicken().getName().equals("Preferred");
        StandardChickenHutch anotherHutch = getContextualReference(StandardChickenHutch.class);
        assert anotherHutch.getChicken().getName().equals("Standard");
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INITIALIZER_METHODS, id = "g"), @SpecAssertion(section = DECLARING_INITIALIZER, id = "a"),
            @SpecAssertion(section = DECLARING_INITIALIZER, id = "e"), @SpecAssertion(section = FIELDS_INITIALIZER_METHODS, id = "ad"),
            @SpecAssertion(section = INJECTION_POINT_DEFAULT_QUALIFIER, id = "a") })
    public void testMultipleInitializerMethodsAreCalled() {
        ChickenHutch chickenHutch = getContextualReference(ChickenHutch.class);
        assert chickenHutch.fox != null;
        assert chickenHutch.chicken != null;
    }

}
