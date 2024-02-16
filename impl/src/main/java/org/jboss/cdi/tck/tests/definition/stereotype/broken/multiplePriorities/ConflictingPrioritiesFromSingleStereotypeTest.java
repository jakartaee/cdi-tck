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
package org.jboss.cdi.tck.tests.definition.stereotype.broken.multiplePriorities;

import jakarta.enterprise.inject.spi.DefinitionException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.definition.stereotype.priority.PriorityStereotype;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * A single stereotype which indirectly declares multiple different priorities.
 */
@SpecVersion(spec = "cdi", version = "4.0")
public class ConflictingPrioritiesFromSingleStereotypeTest extends AbstractTest {

    @Deployment
    @ShouldThrowException(DefinitionException.class)
    public static WebArchive deploy() {
        return new WebArchiveBuilder().withTestClass(ConflictingPrioritiesFromSingleStereotypeTest.class)
                .withClasses(SomeOtherBean.class, AnotherPriorityStereotype.class, PriorityStereotype.class)
                .withBeansXml(new BeansXml())
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.DECLARING_STEREOTYPE_WITH_PRIORITY, id = "b", note = "indirect")
    public void testConflictingPrioritiesFromStereotype() {
        // test should throw an exception
    }
}
