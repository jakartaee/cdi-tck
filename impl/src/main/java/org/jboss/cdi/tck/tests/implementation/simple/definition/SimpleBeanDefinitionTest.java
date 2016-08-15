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
package org.jboss.cdi.tck.tests.implementation.simple.definition;

import static org.jboss.cdi.tck.cdi.Sections.BEAN_CONSTRUCTORS;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_BEAN_CONSTRUCTOR;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_MANAGED_BEAN;
import static org.jboss.cdi.tck.cdi.Sections.INSTANTIATION;
import static org.jboss.cdi.tck.cdi.Sections.MANAGED_BEANS;
import static org.jboss.cdi.tck.cdi.Sections.WHAT_CLASSES_ARE_BEANS;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.implementation.simple.definition.OuterClass.InnerClass_NotBean;
import org.jboss.cdi.tck.tests.implementation.simple.definition.OuterClass.StaticInnerClass;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class SimpleBeanDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SimpleBeanDefinitionTest.class).build();
    }

    @Test
    @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS, id = "ca")
    public void testAbstractClassDeclaredInJavaNotDiscovered() {
        Assert.assertEquals(getBeans(Cow_NotBean.class).size(), 0);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS, id = "ba") })
    public void testStaticInnerClassDeclaredInJavaAllowed() {
        Assert.assertEquals(getBeans(StaticInnerClass.class).size(), 1);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS, id = "b") })
    public void testNonStaticInnerClassDeclaredInJavaNotDiscovered() {
        Assert.assertEquals(getBeans(InnerClass_NotBean.class).size(), 0);
    }

    @Test
    @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS, id = "cb")
    public void testInterfaceNotDiscoveredAsSimpleBean() {
        Assert.assertEquals(getBeans(Car.class).size(), 0);
    }

    @Test
    @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS, id = "g")
    public void testExtensionNotDiscoveredAsSimpleBean() {
        Assert.assertEquals(getBeans(SimpleExtension.class).size(), 0);
    }

    @Test
    @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS, id = "p")
    public void testSimpleBeanOnlyIfConstructorParameterless() {
        Assert.assertTrue(getBeans(Antelope_NotBean.class).isEmpty());
        Assert.assertFalse(getBeans(Donkey.class).isEmpty());
    }

    @Test
    @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS, id = "q")
    public void testSimpleBeanOnlyIfConstructorIsInitializer() {
        Assert.assertTrue(getBeans(Antelope_NotBean.class).isEmpty());
        Assert.assertFalse(getBeans(Sheep.class).isEmpty());
    }

    @Test
    @SpecAssertion(section = DECLARING_BEAN_CONSTRUCTOR, id = "aa")
    public void testInitializerAnnotatedConstructor() throws Exception {
        Sheep.constructedCorrectly = false;
        getContextualReference(Sheep.class);
        Assert.assertTrue(Sheep.constructedCorrectly);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_BEAN_CONSTRUCTOR, id = "ba"), @SpecAssertion(section = DECLARING_MANAGED_BEAN, id = "a"),
            @SpecAssertion(section = BEAN_CONSTRUCTORS, id = "a"), @SpecAssertion(section = INSTANTIATION, id = "ba") })
    public void testEmptyConstructorUsed() {
        Donkey.constructedCorrectly = false;
        getContextualReference(Donkey.class);
        Assert.assertTrue(Donkey.constructedCorrectly);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_BEAN_CONSTRUCTOR, id = "aa"), @SpecAssertion(section = INSTANTIATION, id = "aa") })
    public void testInitializerAnnotatedConstructorUsedOverEmptyConstuctor() throws Exception {
        getContextualReference(Turkey.class);
        Assert.assertTrue(Turkey.constructedCorrectly);
    }

    @Test
    @SpecAssertion(section = MANAGED_BEANS, id = "fa")
    public void testDependentScopedBeanCanHaveNonStaticPublicField() throws Exception {
        Assert.assertEquals(getContextualReference(Tiger.class).name, "pete");
    }

}
