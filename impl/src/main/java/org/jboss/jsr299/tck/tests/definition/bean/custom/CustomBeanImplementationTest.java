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
package org.jboss.jsr299.tck.tests.definition.bean.custom;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class CustomBeanImplementationTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(CustomBeanImplementationTest.class).withBeansXml("beans.xml")
                .withExtension("javax.enterprise.inject.spi.Extension").build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.1.1", id = "k"), @SpecAssertion(section = "5.1.4", id = "q") })
    public void testGetBeanClassCalled() {
        assert IntegerBean.bean.isGetBeanClassCalled();
    }

    @Test
    @SpecAssertion(section = "5.1.1", id = "k")
    public void testGetStereotypesCalled() {
        assert IntegerBean.bean.isGetStereotypesCalled();
    }

    @Test
    @SpecAssertion(section = "5.1.1", id = "k")
    public void testIsPolicyCalled() {
        assert IntegerBean.bean.isAlternativeCalled();
    }

    @Test
    @SpecAssertion(section = "5.2", id = "na")
    public void testGetTypesCalled() {
        assert IntegerBean.bean.isGetTypesCalled();
    }

    @Test
    @SpecAssertion(section = "5.2", id = "nb")
    public void testGetBindingsCalled() {
        assert IntegerBean.bean.isGetQualifiersCalled();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.2.1", id = "b"), @SpecAssertion(section = "6.6.4", id = "ga") })
    public void testGetInjectionPointsCalled() {
        assert IntegerBean.bean.isGetInjectionPointsCalled();
    }

    @Test
    @SpecAssertion(section = "5.2.4", id = "ba")
    public void testIsNullableCalled() {
        assert IntegerBean.bean.isNullableCalled();
    }

    @Test
    @SpecAssertion(section = "5.3", id = "e")
    public void testGetNameCalled() {
        assert IntegerBean.bean.isGetNameCalled();
    }

    @Test
    @SpecAssertion(section = "6.5.2", id = "e")
    public void testGetScopeTypeCalled() {
        assert IntegerBean.bean.isGetScopeCalled();
    }
}
