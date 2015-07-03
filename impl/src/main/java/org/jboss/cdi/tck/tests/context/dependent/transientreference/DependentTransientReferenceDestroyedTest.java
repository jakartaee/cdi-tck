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
package org.jboss.cdi.tck.tests.context.dependent.transientreference;

import static org.jboss.cdi.tck.cdi.Sections.DEPENDENT_DESTRUCTION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.cdi.tck.util.DependentInstance;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0-EDR1")
public class DependentTransientReferenceDestroyedTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DependentTransientReferenceDestroyedTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DEPENDENT_DESTRUCTION, id = "fb"),
            @SpecAssertion(section = DEPENDENT_DESTRUCTION, id = "fc") })
    public void testConstructorAndInitializer() {

        ActionSequence.reset();

        DependentInstance<Spoon> spoonInstance = newDependentInstance(Spoon.class);
        spoonInstance.get().ping();

        // transientChef params in Spoon bean constructor and initializer
        assertEquals(ActionSequence.getSequenceSize(), 2);
        assertTrue(ActionSequence.getSequence().containsAll(Util.buildOwnerId(Spoon.class, true, Util.TYPE_CONSTRUCTOR),
                Util.buildOwnerId(Spoon.class, true, Util.TYPE_INIT)));

        ActionSequence.reset();
        spoonInstance.destroy();

        assertEquals(ActionSequence.getSequenceSize(), 2);
        assertTrue(ActionSequence.getSequence().containsAll(Util.buildOwnerId(Spoon.class, false, Util.TYPE_CONSTRUCTOR),
                Util.buildOwnerId(Spoon.class, false, Util.TYPE_INIT)));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DEPENDENT_DESTRUCTION, id = "fa") })
    public void testProducerMethod() {

        ActionSequence.reset();

        DependentInstance<Meal> mealInstance = newDependentInstance(Meal.class);
        Meal meal = mealInstance.get();

        assertEquals(meal.getName(), "soup");
        // transientChef param in Kitchen producer
        assertEquals(ActionSequence.getSequenceSize(), 1);
        assertTrue(ActionSequence.getSequence().containsAll(Util.buildOwnerId(Kitchen.class, true, Util.TYPE_PRODUCER)));

        mealInstance.destroy();
    }
}
