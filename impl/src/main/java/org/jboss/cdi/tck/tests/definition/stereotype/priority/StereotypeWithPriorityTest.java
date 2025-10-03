/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.definition.stereotype.priority;

import static org.testng.Assert.assertEquals;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "4.0")
public class StereotypeWithPriorityTest extends AbstractTest {

    @Deployment
    public static WebArchive deploy() {
        return new WebArchiveBuilder().withTestClassPackage(StereotypeWithPriorityTest.class)
                .withBeansXml(new BeansXml())
                .build();
    }

    @Inject
    Foo foo;

    @Inject
    Bar bar;

    @Inject
    Baz baz;

    @Inject
    Charlie charlie;

    @Inject
    Qux qux;

    @Test
    @SpecAssertion(section = Sections.STEREOTYPES, id = "ac")
    @SpecAssertion(section = Sections.DECLARING_STEREOTYPE_WITH_PRIORITY, id = "a")
    public void testStereotypeWithPriority() {
        // injected Foo should be FooAlternative
        assertEquals(foo.ping(), FooAlternative.class.getSimpleName());
    }

    @Test
    @SpecAssertion(section = Sections.STEREOTYPES, id = "ac")
    @SpecAssertion(section = Sections.DECLARING_STEREOTYPE_WITH_PRIORITY, id = "a")
    @SpecAssertion(section = Sections.ALTERNATIVE_STEREOTYPE, id = "a")
    public void testStereotypeWithAlternativeAndPriority() {
        // injected Bar should be instance of BarExtended
        assertEquals(bar.ping(), BarExtended.class.getSimpleName());
    }

    @Test
    @SpecAssertion(section = Sections.STEREOTYPES, id = "ac")
    @SpecAssertion(section = Sections.DECLARING_STEREOTYPE_WITH_PRIORITY, id = "a")
    @SpecAssertion(section = Sections.ALTERNATIVE_STEREOTYPE, id = "a", note = "PriorityStereotype overrides BazAlternative")
    public void testBeanPriorityFromStereotypeOverridesOtherAlternative() {
        // injected Baz should be instance of BazAlternative2
        assertEquals(baz.ping(), BazAlternative2.class.getSimpleName());
    }

    @Test
    @SpecAssertion(section = Sections.STEREOTYPES, id = "ac")
    @SpecAssertion(section = Sections.DECLARING_SELECTED_ALTERNATIVES_APPLICATION, id = "aa")
    public void testBeanOverridesPriorityFromStereotype() {
        // injected Charlie should be instance of CharlieAlternative
        assertEquals(charlie.ping(), CharlieAlternative.class.getSimpleName());
    }

    @Test
    @SpecAssertion(section = Sections.STEREOTYPES, id = "ac")
    @SpecAssertion(section = Sections.DECLARING_STEREOTYPE_WITH_PRIORITY, id = "a")
    @SpecAssertion(section = Sections.RESERVE_STEREOTYPE, id = "a")
    public void testStereotypeWithReserveAndPriority() {
        assertEquals(qux.ping(), Qux.class.getSimpleName());
    }
}
