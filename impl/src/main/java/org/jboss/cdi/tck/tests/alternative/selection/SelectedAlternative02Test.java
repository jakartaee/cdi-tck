/*
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

package org.jboss.cdi.tck.tests.alternative.selection;

import static org.jboss.cdi.tck.cdi.Sections.UNSATISFIED_AND_AMBIG_DEPENDENCIES;
import static org.jboss.cdi.tck.tests.alternative.selection.SelectedAlternativeTestUtil.createBuilderBase;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test resolution of ambiguous dependencies.
 *
 * WAR deployment with 2 libraries:
 * <ul>
 * <li>WEB-INF/classes - alpha - does not declare any alternative, includes {@link TestBean} implementation</li>
 * <li>lib 1 - bravo - declares {@link Foo} alternative selected for the app with priority 1000</li>
 * <li>lib 2 - charlie - declares {@link Bar} alternative selected for the app with priority 2000</li>
 * </ul>
 *
 * Expected result: {@link Bar} is resolved in all bean archives
 *
 * @author Martin Kouba
 *
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class SelectedAlternative02Test extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return createBuilderBase().withTestClass(SelectedAlternative02Test.class)
                .withClasses(Alpha.class, SimpleTestBean.class, Boss.class).withBeanLibrary(Bravo.class, Foo.class)
                .withBeanLibrary(Charlie.class, Bar.class).build();
    }

    @Inject
    Alpha alpha;

    @Inject
    Bravo bravo;

    @Inject
    Charlie charlie;

    @Test
    @SpecAssertion(section = UNSATISFIED_AND_AMBIG_DEPENDENCIES, id = "cb")
    public void testDependencyResolvable() {
        assertNotNull(alpha);
        assertNotNull(bravo);
        assertNotNull(charlie);

        assertEquals(alpha.assertAvailable(TestBean.class).getId(), Bar.class.getName());
        assertEquals(bravo.assertAvailable(TestBean.class).getId(), Bar.class.getName());
        assertEquals(charlie.assertAvailable(TestBean.class).getId(), Bar.class.getName());
    }

}
