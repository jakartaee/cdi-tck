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
package org.jboss.cdi.tck.tests.interceptors.definition.inheritance;

import static org.jboss.cdi.tck.cdi.Sections.MEMBER_LEVEL_INHERITANCE;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_LEVEL_INHERITANCE;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test interceptor binding inheritance.
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class InterceptorBindingInheritanceTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InterceptorBindingInheritanceTest.class)
                .build();
    }

    private String squirrel = SquirrelInterceptor.class.getName();
    private String woodpecker = WoodpeckerInterceptor.class.getName();

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "ad"),
            @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "ada") })
    public void testInterceptorBindingDirectlyInheritedFromManagedBean(Larch larch) throws Exception {
        Plant.clearInspections();
        larch.pong();
        assertTrue(Plant.inspectedBy(squirrel));
        assertFalse(Plant.inspectedBy(woodpecker));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "aj"),
            @SpecAssertion(section = TYPE_LEVEL_INHERITANCE, id = "aja") })
    public void testInterceptorBindingIndirectlyInheritedFromManagedBean(@European Larch europeanLarch) throws Exception {
        Plant.clearInspections();
        europeanLarch.pong();
        assertTrue(europeanLarch instanceof EuropeanLarch);
        assertTrue(Plant.inspectedBy(squirrel));
        assertFalse(Plant.inspectedBy(woodpecker));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "ka")
    public void testMethodInterceptorBindingDirectlyInheritedFromManagedBean(Herb herb) {
        Plant.clearInspections();
        herb.pong();
        assertTrue(Plant.inspectedBy(squirrel));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "kc")
    public void testMethodInterceptorBindingIndirectlyInheritedFromManagedBean(@Culinary Herb thyme) {
        Plant.clearInspections();
        thyme.pong();
        assertTrue(thyme instanceof Thyme);
        assertTrue(Plant.inspectedBy(squirrel));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "ka")
    public void testMethodInterceptorBindingDirectlyNotInheritedFromManagedBean(Shrub shrub) {
        Plant.clearInspections();
        shrub.pong();
        assertFalse(Plant.inspectedBy(squirrel));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "kc")
    public void testMethodInterceptorBindingIndirectlyNotInheritedFromManagedBean(@Culinary Shrub rosehip) {
        Plant.clearInspections();
        rosehip.pong();
        assertTrue(rosehip instanceof Rosehip);
        assertFalse(Plant.inspectedBy(squirrel));
    }

}
