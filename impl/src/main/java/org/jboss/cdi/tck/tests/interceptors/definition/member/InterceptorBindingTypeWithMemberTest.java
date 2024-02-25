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
package org.jboss.cdi.tck.tests.interceptors.definition.member;

import static org.jboss.cdi.tck.cdi.Sections.CONCEPTS;
import static org.jboss.cdi.tck.cdi.Sections.INTERCEPTOR_RESOLUTION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for interceptor bindings types with members.
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class InterceptorBindingTypeWithMemberTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InterceptorBindingTypeWithMemberTest.class)
                .build();
    }

    @Test
    @SpecAssertion(section = CONCEPTS, id = "f")
    public void testInterceptorBindingTypeWithMember() {
        Farm farm = getContextualReference(Farm.class);
        assertEquals(farm.getAnimalCount(), 20);
        assertTrue(IncreasingInterceptor.isIntercepted());
        assertFalse(DecreasingInterceptor.isIntercepted());
    }

    @Test
    @SpecAssertion(section = INTERCEPTOR_RESOLUTION, id = "b")
    public void testInterceptorBindingTypeWithNonBindingMember() {
        Farm farm = getContextualReference(Farm.class);
        assertEquals(farm.getVehicleCount(), 20);
        assertTrue(VehicleCountInterceptor.isIntercepted());
    }

}
