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

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
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

    @Inject
    Larch larch;

    @Inject
    ForgetMeNot forgetMeNot;

    @Inject
    @European
    Larch europeanLarch;

    @Inject
    @European
    ForgetMeNot woodForgetMeNot;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "4.1", id = "ad"), @SpecAssertion(section = "4.1", id = "ada") })
    public void testInterceptorBindingDirectlyInheritedFromManagedBean() throws Exception {
        larch.pong();
        Assert.assertTrue(larch.inspections.contains(SquirrelInterceptor.class.getName()));
        Assert.assertFalse(larch.inspections.contains(WoodpeckerInterceptor.class.getName()));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "4.1", id = "aj"), @SpecAssertion(section = "4.1", id = "aja") })
    public void testInterceptorBindingIndirectlyInheritedFromManagedBean() throws Exception {
        europeanLarch.pong();
        Assert.assertTrue(europeanLarch.inspections.contains(SquirrelInterceptor.class.getName()));
        Assert.assertFalse(europeanLarch.inspections.contains(WoodpeckerInterceptor.class.getName()));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "4.1", id = "an"), @SpecAssertion(section = "4.1", id = "ana") })
    public void testInterceptorBindingDirectlyInheritedFromSessionBean() throws Exception {
        forgetMeNot.pong();
        Assert.assertTrue(forgetMeNot.inspections.contains(SquirrelInterceptor.class.getName()));
        Assert.assertFalse(forgetMeNot.inspections.contains(WoodpeckerInterceptor.class.getName()));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "4.1", id = "ar"), @SpecAssertion(section = "4.1", id = "ara") })
    public void testInterceptorBindingIndirectlyInheritedFromSessionBean() throws Exception {
        woodForgetMeNot.pong();
        Assert.assertTrue(woodForgetMeNot.inspections.contains(SquirrelInterceptor.class.getName()));
        Assert.assertFalse(woodForgetMeNot.inspections.contains(WoodpeckerInterceptor.class.getName()));
    }

}
