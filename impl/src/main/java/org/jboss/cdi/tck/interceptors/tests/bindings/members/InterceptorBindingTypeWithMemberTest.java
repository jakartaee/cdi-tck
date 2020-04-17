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
package org.jboss.cdi.tck.interceptors.tests.bindings.members;

import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INT_BINDING_TYPES_WITH_MEMBERS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import jakarta.enterprise.inject.spi.InterceptionType;
import jakarta.enterprise.inject.spi.Interceptor;
import jakarta.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for interceptor bindings types with members.
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "interceptos", version = "1.2")
public class InterceptorBindingTypeWithMemberTest extends AbstractTest {

    @SuppressWarnings("serial")
    public abstract class PlantInterceptorBindingLiteral extends AnnotationLiteral<PlantInterceptorBinding> implements
            PlantInterceptorBinding {
    }

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InterceptorBindingTypeWithMemberTest.class)
                .withBeansXml(
                        Descriptors
                                .create(BeansDescriptor.class)
                                .getOrCreateInterceptors()
                                .clazz(IncreasingInterceptor.class.getName(), DecreasingInterceptor.class.getName(),
                                        VehicleCountInterceptor.class.getName(), PlantInterceptor.class.getName()).up())
                .build();
    }

    @Test
    @SpecAssertion(section = INT_BINDING_TYPES_WITH_MEMBERS, id = "a")
    public void testInterceptorBindingTypeWithMember() {
        Farm farm = getContextualReference(Farm.class);
        assertEquals(farm.getAnimalCount(), 20);
        assertTrue(IncreasingInterceptor.isIntercepted());
        assertFalse(DecreasingInterceptor.isIntercepted());
    }

    @Test
    @SpecAssertion(section = INT_BINDING_TYPES_WITH_MEMBERS, id = "c")
    public void testInterceptorBindingTypeWithNonBindingMember() {
        Farm farm = getContextualReference(Farm.class);
        assert farm.getVehicleCount() == 20;
        assert VehicleCountInterceptor.isIntercepted();
    }

    /**
     * Note that we cannot directly test that member values are compared using equals() as only primitive types, String, Class,
     * enum and annotation are permitted to be annotation attributes. We test that two different String objects used as member
     * values are considered equal when resolving interceptor (interceptor is resolved and applied).
     */
    @Test
    @SpecAssertion(section = INT_BINDING_TYPES_WITH_MEMBERS, id = "b")
    public void testInterceptorBindingTypeMemberValuesComparedWithEquals() {

        @SuppressWarnings("serial")
        List<Interceptor<?>> interceptors = getCurrentManager().resolveInterceptors(InterceptionType.AROUND_INVOKE,
                new PlantInterceptorBindingLiteral() {

                    public String name() {
                        return new String("TEST");
                    }

                    public int age() {
                        return 1;
                    }
                });
        assertTrue(interceptors.size() > 0);
        Plant plant = getContextualReference(Plant.class);
        plant.grow();
        assertTrue(PlantInterceptor.isIntercepted());
    }
}
