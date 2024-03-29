/*
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.lookup.injection.parameterized.multiple.bounds;

import static org.jboss.cdi.tck.cdi.Sections.ASSIGNABLE_PARAMETERS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Matus Abaffy
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ParameterizedTypesWithTypeVariableWithMultipleBoundsTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(ParameterizedTypesWithTypeVariableWithMultipleBoundsTest.class)
                .build();
    }

    @Inject
    ConsumerMultipleBounds<?, ?> consumer;

    @Test
    @SpecAssertions({ @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "dc") })
    public void testInjectionOfBeanWithWildcardWithTypeVariableAsLowerBound() {
        assertNotNull(consumer.getGenericInterfaceSuperBazImpl2());
        assertEquals(consumer.getGenericInterfaceSuperBazImpl2().getId(), GenericInterfaceSuperBazImpl.class.getSimpleName());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "e") })
    public void testInjectionOfBeanWithTypeVariableWithMultipleBoundsToParameterizedTypeWithActualType() {
        assertNotNull(consumer.getGenericInterfaceBarFooImpl2());
        assertEquals(consumer.getGenericInterfaceBarFooImpl2().getId(), GenericInterfaceBarFooImpl.class.getSimpleName());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "f") })
    public void testInjectionOfBeanWithTypeVariableWithMultipleBounds() {
        assertNotNull(consumer.getGenericInterfaceBarBazFooImpl());
        assertEquals(consumer.getGenericInterfaceBarBazFooImpl().getId(), GenericInterfaceBarBazFooImpl.class.getSimpleName());
        assertNotNull(consumer.getGenericInterfaceBarFooImpl());
        assertEquals(consumer.getGenericInterfaceBarFooImpl().getId(), GenericInterfaceBarFooImpl.class.getSimpleName());
        assertNotNull(consumer.getGenericInterfaceSuperBazImpl());
        assertEquals(consumer.getGenericInterfaceSuperBazImpl().getId(), GenericInterfaceSuperBazImpl.class.getSimpleName());
    }
}
