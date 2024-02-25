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
package org.jboss.cdi.tck.tests.event.resolve.nonbinding;

import static org.jboss.cdi.tck.cdi.Sections.BM_DECORATOR_RESOLUTION;

import java.util.Set;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.ObserverMethod;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class NonBindingTypesWhenResolvingTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(NonBindingTypesWhenResolvingTest.class).build();
    }

    public static class AnEventType {
    }

    public static class AnObserver {
        public boolean wasNotified = false;

        public void observer(@Observes AnEventType event) {
            wasNotified = true;
        }
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    @SpecAssertion(section = BM_DECORATOR_RESOLUTION, id = "e")
    public void testNonBindingTypeAnnotationWhenResolvingFails() {
        Set<ObserverMethod<? super AnEventType>> resolvedObservers = getCurrentManager().resolveObserverMethods(
                new AnEventType(), new AnimalStereotypeAnnotationLiteral());
    }
}
