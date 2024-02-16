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
package org.jboss.cdi.tck.tests.lookup.circular;

import static org.jboss.cdi.tck.cdi.Sections.INJECTION_AND_RESOLUTION;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class CircularDependencyTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(CircularDependencyTest.class).build();
    }

    @Test
    @SpecAssertion(section = INJECTION_AND_RESOLUTION, id = "b")
    public void testCircularInjectionOnTwoNormalBeans() throws Exception {
        Pig pig = getContextualReference(Pig.class);
        Food food = getContextualReference(Food.class);
        assert pig.getNameOfFood().equals(food.getName());
        assert food.getNameOfPig().equals(pig.getName());
    }

    @Test
    @SpecAssertion(section = INJECTION_AND_RESOLUTION, id = "b")
    public void testCircularInjectionOnOneNormalAndOneDependentBean() throws Exception {
        Petrol petrol = getContextualReference(Petrol.class);
        Car car = getContextualReference(Car.class);
        assert petrol.getNameOfCar().equals(car.getName());
        assert car.getNameOfPetrol().equals(petrol.getName());
    }

    @Test
    @SpecAssertion(section = INJECTION_AND_RESOLUTION, id = "b")
    public void testNormalProducerMethodDeclaredOnNormalBeanWhichInjectsProducedBean() throws Exception {
        getContextualReference(NormalSelfConsumingNormalProducer.class).ping();
    }

    @Test
    @SpecAssertion(section = INJECTION_AND_RESOLUTION, id = "b")
    public void testNormalProducerMethodDeclaredOnDependentBeanWhichInjectsProducedBean() throws Exception {
        getContextualReference(DependentSelfConsumingNormalProducer.class).ping();
    }

    @Test
    @SpecAssertion(section = INJECTION_AND_RESOLUTION, id = "b")
    public void testNormalCircularConstructors() throws Exception {
        assert getContextualReference(Bird.class) != null;
    }

    @Test
    @SpecAssertion(section = INJECTION_AND_RESOLUTION, id = "b")
    public void testNormalAndDependentCircularConstructors() throws Exception {
        assert getContextualReference(Planet.class) != null;
    }

    @Test
    @SpecAssertion(section = INJECTION_AND_RESOLUTION, id = "b")
    public void testSelfConsumingConstructorsOnNormalBean() throws Exception {
        assert getContextualReference(House.class) != null;
    }
}
