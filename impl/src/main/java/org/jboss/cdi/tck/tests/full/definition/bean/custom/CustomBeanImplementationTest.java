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
package org.jboss.cdi.tck.tests.full.definition.bean.custom;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.CONTEXTUAL_INSTANCE;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.INTER_MODULE_INJECTION;
import static org.jboss.cdi.tck.cdi.Sections.NAME_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_DEPENDENCY;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_VALIDATION;
import static org.jboss.cdi.tck.cdi.Sections.PERFORMING_TYPESAFE_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.UNSATISFIED_AND_AMBIG_DEPENDENCIES;

import java.io.IOException;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class CustomBeanImplementationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(CustomBeanImplementationTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withClasses(AfterBeanDiscoveryObserver.class, House.class, CustomInjectionPoint.class, Bar.class, PassivationCapableBean.class, SomeBean.class, AlternativeSomeBean.class)
                .withLibrary(Foo.class, FooBean.class, IntegerBean.class, Passivable.class, PassivableLiteral.class)
                .withExtension(AfterBeanDiscoveryObserver.class)
                .build();
    }

    @Test
    @SpecAssertions(
            { @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "h"), @SpecAssertion(section = INTER_MODULE_INJECTION, id = "q") })
    public void testGetBeanClassCalled() {
        assert AfterBeanDiscoveryObserver.integerBean.isGetBeanClassCalled();
    }

    @Test
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "h")
    public void testGetStereotypesCalled() {
        assert AfterBeanDiscoveryObserver.integerBean.isGetStereotypesCalled();
    }

    @Test
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "h")
    public void testIsPolicyCalled() {
        assert AfterBeanDiscoveryObserver.integerBean.isAlternativeCalled();
    }

    @Inject
    Instance<SomeBean> instance;

    @Test
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "h")
    public void testCustomBeanNotAutomaticallySelected() {
        // custom bean AlternativeSomeBean has no priority and should't be selected
        Assert.assertTrue(instance.isResolvable());
        Assert.assertEquals(instance.get().whoAmI(), SomeBean.class.getSimpleName());
    }

    @Test
    @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "na")
    public void testGetTypesCalled() {
        assert AfterBeanDiscoveryObserver.integerBean.isGetTypesCalled();
    }

    @Test
    @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "nb")
    public void testGetBindingsCalled() {
        assert AfterBeanDiscoveryObserver.integerBean.isGetQualifiersCalled();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = UNSATISFIED_AND_AMBIG_DEPENDENCIES, id = "b"), @SpecAssertion(section = PASSIVATION_VALIDATION, id = "ga"),
            @SpecAssertion(section = PASSIVATION_VALIDATION, id = "gb") })
    public void testGetInjectionPointsCalled(Bar bar) {
        assert AfterBeanDiscoveryObserver.integerBean.isGetInjectionPointsCalled();
        assert FooBean.barInjectionPoint.isTransientCalled();
        assert bar.getOne() == 1;
    }

    @Test
    @SpecAssertion(section = NAME_RESOLUTION, id = "e")
    public void testGetNameCalled() {
        assert AfterBeanDiscoveryObserver.integerBean.isGetNameCalled();
    }

    @Test
    @SpecAssertion(section = CONTEXTUAL_INSTANCE, id = "e")
    public void testGetScopeTypeCalled() {
        assert AfterBeanDiscoveryObserver.integerBean.isGetScopeCalled();
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE, id = "ea")
    public void testCustomBeanIsPassivationCapable() throws IOException, ClassNotFoundException {

        Foo customFoo = getContextualReference(Foo.class, new PassivableLiteral());
        byte[] serializedBean = passivate(customFoo);
        Foo customFooDeserialized = (Foo) activate(serializedBean);
        Assert.assertEquals(customFoo.getId(), customFooDeserialized.getId());
    }

    @Test
    @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY, id = "fa")
    public void testCustomBeanIsPassivationCapableDependency() throws IOException, ClassNotFoundException {
        PassivationCapableBean passCapBean = getContextualReference(PassivationCapableBean.class);
        byte[] serializedBean = passivate(passCapBean);
        PassivationCapableBean actCapBean = (PassivationCapableBean) activate(serializedBean);
        Assert.assertEquals(passCapBean.getFoo().getId(), actCapBean.getFoo().getId());
    }

    @Test(enabled = false) // disabled due to CDITCK-579
   // @SpecAssertion(section = INTER_MODULE_INJECTION, id = "r")
    public void testInjectionPointGetMemberIsUsedToDetermineTheClassThatDeclaresAnInjectionPoint(){
        Assert.assertEquals(CustomInjectionPoint.getMembersClasses().size(),2);
        Assert.assertTrue(CustomInjectionPoint.getMembersClasses().contains(Bar.class));
        Assert.assertTrue(CustomInjectionPoint.getMembersClasses().contains(Integer.class));
    }
}
