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
package org.jboss.jsr299.tck.tests.lookup.manager;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.DefaultLiteral;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class ManagerTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ManagerTest.class).build();
    }

    @Test(groups = { "manager", "injection", "deployment" })
    @SpecAssertion(section = "11.3", id = "c")
    public void testInjectingManager() {
        FishFarmOffice fishFarmOffice = getInstanceByType(FishFarmOffice.class);
        assert fishFarmOffice.beanManager != null;
    }

    @Test
    @SpecAssertion(section = "11.3", id = "aa")
    public void testContainerProvidesManagerBean() {
        assert getBeans(BeanManager.class).size() > 0;
    }

    @Test
    @SpecAssertion(section = "11.3", id = "ab")
    public void testManagerBeanIsDependentScoped() {
        Bean<BeanManager> beanManager = getBeans(BeanManager.class).iterator().next();
        assert beanManager.getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertion(section = "11.3", id = "ac")
    public void testManagerBeanHasCurrentBinding() {
        Bean<BeanManager> beanManager = getBeans(BeanManager.class).iterator().next();
        assert beanManager.getQualifiers().contains(new DefaultLiteral());
    }

    @Test
    @SpecAssertion(section = "11.3", id = "b")
    public void testManagerBeanIsPassivationCapable() {
        assert isSerializable(getCurrentManager().getClass());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.3.1", id = "a"), @SpecAssertion(section = "11.3.1", id = "b") })
    public void testGetReferenceReturnsContextualInstance() {
        Bean<FishFarmOffice> bean = getBeans(FishFarmOffice.class).iterator().next();
        assert getCurrentManager().getReference(bean, FishFarmOffice.class, getCurrentManager().createCreationalContext(bean)) instanceof FishFarmOffice;
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    @SpecAssertion(section = "11.3.1", id = "c")
    public void testGetReferenceWithIllegalBeanType() {
        Bean<FishFarmOffice> bean = getBeans(FishFarmOffice.class).iterator().next();
        getCurrentManager().getReference(bean, BigDecimal.class, getCurrentManager().createCreationalContext(bean));
    }

    private boolean isSerializable(Class<?> clazz) {
        return clazz.isPrimitive() || Serializable.class.isAssignableFrom(clazz);
    }

}
