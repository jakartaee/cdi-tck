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

package org.jboss.cdi.tck.tests.event.observer.inheritance;

import static org.jboss.cdi.tck.cdi.Sections.MEMBER_LEVEL_INHERITANCE;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.event.EventTest;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test duplicates {@link EventTest#testNonStaticObserverMethodInherited()} and
 * {@link EventTest#testNonStaticObserverMethodIndirectlyInherited()} but the base class extended is abstract.
 * 
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class ObserverInheritanceTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ObserverInheritanceTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "dc"), @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "di") })
    public void testNonStaticObserverMethodInherited() {

        ActionSequence.reset();
        getCurrentManager().fireEvent(new Egg());

        // Foo directly extends AbstractFooObserver - observer invoked
        // Bar indirectly extends AbstractFooObserver - observer invoked
        // Baz indirectly extends AbstractFooObserver but overrides the observer method and does not define @Observes - not
        // invoked
        assertEquals(ActionSequence.getSequenceSize(), 2);
        ActionSequence.getSequence().assertDataContainsAll(Foo.class.getName(), Bar.class.getName());
    }

}
