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
package org.jboss.jsr299.tck.tests.interceptors.definition.member;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
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
@SpecVersion(spec = "cdi", version = "20091101")
public class InterceptorBindingTypeWithMemberTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InterceptorBindingTypeWithMemberTest.class)
                .withBeansXml("beans.xml").build();
    }

    @Test
    @SpecAssertion(section = "9.5.2", id = "a")
    public void testInterceptorBindingTypeWithMember() {
        Farm farm = getInstanceByType(Farm.class);
        assert farm.getAnimalCount() == 20;
        assert IncreasingInterceptor.isIntercepted();
        assert !DecreasingInterceptor.isIntercepted();
    }

    @Test
    @SpecAssertion(section = "9.5.2", id = "b")
    public void testInterceptorBindingTypeWithNonBindingMember() {
        Farm farm = getInstanceByType(Farm.class);
        assert farm.getVehicleCount() == 20;
        assert VehicleCountInterceptor.isIntercepted();
    }

    /**
     * Note that we cannot directly test that member values are compared using equals() as only primitive types, String, Class,
     * enum and annotation are permitted to be annotation attributes. We test that two different String objects used as member
     * values are considered equal when resolving interceptor (interceptor is applied).
     */
    @Test
    @SpecAssertion(section = "9.5.2", id = "ab")
    public void testInterceptorBindingTypeMemberValuesComparedWithEquals() {
        Plant plant = getInstanceByType(Plant.class);
        plant.grow();
        assert PlantInterceptor.isIntercepted();
    }
}
