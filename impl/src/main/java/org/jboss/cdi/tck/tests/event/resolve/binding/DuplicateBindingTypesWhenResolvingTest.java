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
package org.jboss.cdi.tck.tests.event.resolve.binding;

import static org.jboss.cdi.tck.cdi.Sections.BM_OBSERVER_METHOD_RESOLUTION;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class DuplicateBindingTypesWhenResolvingTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DuplicateBindingTypesWhenResolvingTest.class).build();
    }

    public static class AnEventType {
    }

    @Dependent
    public static class AnObserver {
        public boolean wasNotified = false;

        public void observer(@Observes AnEventType event) {
            wasNotified = true;
        }
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    @SpecAssertion(section = BM_OBSERVER_METHOD_RESOLUTION, id = "d")
    public void testDuplicateBindingTypesWhenResolvingFails() {
        getCurrentManager().resolveObserverMethods(new AnEventType(), new BindingTypeABinding("a1"),
                new BindingTypeABinding("a2"));
    }
}
