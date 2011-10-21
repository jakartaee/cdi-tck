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
package org.jboss.jsr299.tck.tests.interceptors.definition.inheritance;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test interceptor binding inheritance.
 * 
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class InterceptorBindingInheritanceTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InterceptorBindingInheritanceTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors()
                                .clazz(SquirrelInterceptor.class.getName(), WoodpeckerInterceptor.class.getName()).up())
                .build();
    }

    private String squirrel = SquirrelInterceptor.class.getName();
    private String woodpecker = WoodpeckerInterceptor.class.getName();

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "4.1", id = "ad"), @SpecAssertion(section = "4.1", id = "ada") })
    public void testInterceptorBindingDirectlyInheritedFromManagedBean(Larch larch) throws Exception {
        larch.pong();
        assertTrue(larch.inspectedBy(squirrel));
        assertFalse(larch.inspectedBy(woodpecker));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "4.1", id = "aj"), @SpecAssertion(section = "4.1", id = "aja") })
    public void testInterceptorBindingIndirectlyInheritedFromManagedBean(@European Larch europeanLarch) throws Exception {
        europeanLarch.pong();
        assertTrue(europeanLarch instanceof EuropeanLarch);
        assertTrue(europeanLarch.inspectedBy(squirrel));
        assertFalse(europeanLarch.inspectedBy(woodpecker));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "4.1", id = "an"), @SpecAssertion(section = "4.1", id = "ana") })
    public void testInterceptorBindingDirectlyInheritedFromSessionBean(ForgetMeNot forgetMeNot) throws Exception {
        forgetMeNot.pong();
        assertTrue(forgetMeNot.inspectedBy(squirrel));
        assertFalse(forgetMeNot.inspectedBy(woodpecker));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "4.1", id = "ar"), @SpecAssertion(section = "4.1", id = "ara") })
    public void testInterceptorBindingIndirectlyInheritedFromSessionBean(@European ForgetMeNot woodForgetMeNot)
            throws Exception {
        woodForgetMeNot.pong();
        assertTrue(woodForgetMeNot instanceof WoodForgetMeNot);
        assertTrue(woodForgetMeNot.inspectedBy(squirrel));
        assertFalse(woodForgetMeNot.inspectedBy(woodpecker));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "4.2", id = "ka")
    public void testMethodInterceptorBindingDirectlyInheritedFromManagedBean(Herb herb) {
        herb.pong();
        assertTrue(herb.inspectedBy(squirrel));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "4.2", id = "kc")
    public void testMethodInterceptorBindingIndirectlyInheritedFromManagedBean(@Culinary Herb thyme) {
        thyme.pong();
        assertTrue(thyme instanceof Thyme);
        assertTrue(thyme.inspectedBy(squirrel));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "4.2", id = "kb")
    public void testMethodInterceptorBindingDirectlyInheritedFromSessionBean(Grass grass) {
        grass.pong();
        assertTrue(grass.inspectedBy(squirrel));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "4.2", id = "kd")
    public void testMethodInterceptorBindingIndirectlyInheritedFromSessionBean(@Culinary Grass waterChestnut) {
        waterChestnut.pong();
        assertTrue(waterChestnut instanceof WaterChestnut);
        assertTrue(waterChestnut.inspectedBy(squirrel));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "4.2", id = "ka")
    public void testMethodInterceptorBindingDirectlyNotInherited(Shrub shrub) {
        shrub.pong();
        assertFalse(shrub.inspectedBy(squirrel));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = "4.2", id = "kc")
    public void testMethodInterceptorBindingIndirectlyNotInherited(@Culinary Shrub rosehip) {
        rosehip.pong();
        assertTrue(rosehip instanceof Rosehip);
        assertFalse(rosehip.inspectedBy(squirrel));
    }
}
