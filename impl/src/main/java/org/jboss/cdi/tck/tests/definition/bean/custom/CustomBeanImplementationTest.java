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
package org.jboss.cdi.tck.tests.definition.bean.custom;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class CustomBeanImplementationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(CustomBeanImplementationTest.class)
                .withClasses(AfterBeanDiscoveryObserver.class, House.class, CustomInjectionPoint.class, Bar.class)
                .withLibrary(Foo.class, FooBean.class, IntegerBean.class).withExtension(AfterBeanDiscoveryObserver.class)
                .build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.1.1", id = "k"), @SpecAssertion(section = "5.1.4", id = "q") })
    public void testGetBeanClassCalled() {
        assert AfterBeanDiscoveryObserver.integerBean.isGetBeanClassCalled();
    }

    @Test
    @SpecAssertion(section = "5.1.1", id = "k")
    public void testGetStereotypesCalled() {
        assert AfterBeanDiscoveryObserver.integerBean.isGetStereotypesCalled();
    }

    @Test
    @SpecAssertion(section = "5.1.1", id = "k")
    public void testIsPolicyCalled() {
        assert AfterBeanDiscoveryObserver.integerBean.isAlternativeCalled();
    }

    @Test
    @SpecAssertion(section = "5.2.1", id = "na")
    public void testGetTypesCalled() {
        assert AfterBeanDiscoveryObserver.integerBean.isGetTypesCalled();
    }

    @Test
    @SpecAssertion(section = "5.2.1", id = "nb")
    public void testGetBindingsCalled() {
        assert AfterBeanDiscoveryObserver.integerBean.isGetQualifiersCalled();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "5.2.2", id = "b"), @SpecAssertion(section = "6.6.4", id = "ga"),
            @SpecAssertion(section = "6.6.4", id = "gb") })
    public void testGetInjectionPointsCalled(Bar bar) {
        assert AfterBeanDiscoveryObserver.integerBean.isGetInjectionPointsCalled();
        assert FooBean.barInjectionPoint.isTransientCalled();
        assert FooBean.integerInjectionPoint.isTransientCalled();
        assert bar.getOne() == 1;
    }

    @Test
    @SpecAssertion(section = "5.2.5", id = "c")
    public void testIsNullableCalled() {
        assert AfterBeanDiscoveryObserver.integerBean.isNullableCalled();
    }

    @Test
    @SpecAssertion(section = "5.3", id = "e")
    public void testGetNameCalled() {
        assert AfterBeanDiscoveryObserver.integerBean.isGetNameCalled();
    }

    @Test
    @SpecAssertion(section = "6.5.2", id = "e")
    public void testGetScopeTypeCalled() {
        assert AfterBeanDiscoveryObserver.integerBean.isGetScopeCalled();
    }
}
