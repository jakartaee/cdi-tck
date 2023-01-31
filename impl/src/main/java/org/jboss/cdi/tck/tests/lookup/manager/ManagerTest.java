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
package org.jboss.cdi.tck.tests.lookup.manager;

import static org.jboss.cdi.tck.cdi.Sections.BEANMANAGER;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_CONTEXTUAL_REFERENCE;

import java.math.BigDecimal;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class ManagerTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ManagerTest.class).build();
    }

    @Test
    @SpecAssertion(section = BEANMANAGER, id = "c")
    public void testInjectingManager() {
        FishFarmOffice fishFarmOffice = getContextualReference(FishFarmOffice.class);
        assert fishFarmOffice.beanManager != null;
    }

    @Test
    @SpecAssertion(section = BEANMANAGER, id = "aa")
    public void testContainerProvidesManagerBean() {
        assert getBeans(BeanManager.class).size() > 0;
    }

    @Test
    @SpecAssertion(section = BEANMANAGER, id = "ab")
    public void testManagerBeanIsDependentScoped() {
        Bean<BeanManager> beanManager = getBeans(BeanManager.class).iterator().next();
        assert beanManager.getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertion(section = BEANMANAGER, id = "ac")
    public void testManagerBeanHasCurrentBinding() {
        Bean<BeanManager> beanManager = getBeans(BeanManager.class).iterator().next();
        assert beanManager.getQualifiers().contains(Default.Literal.INSTANCE);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_CONTEXTUAL_REFERENCE, id = "a"), @SpecAssertion(section = BM_OBTAIN_CONTEXTUAL_REFERENCE, id = "b") })
    public void testGetReferenceReturnsContextualInstance() {
        Bean<FishFarmOffice> bean = getBeans(FishFarmOffice.class).iterator().next();
        assert getCurrentManager().getReference(bean, FishFarmOffice.class, getCurrentManager().createCreationalContext(bean)) instanceof FishFarmOffice;
    }

    @Test(expectedExceptions = { IllegalArgumentException.class })
    @SpecAssertion(section = BM_OBTAIN_CONTEXTUAL_REFERENCE, id = "c")
    public void testGetReferenceWithIllegalBeanType() {
        Bean<FishFarmOffice> bean = getBeans(FishFarmOffice.class).iterator().next();
        getCurrentManager().getReference(bean, BigDecimal.class, getCurrentManager().createCreationalContext(bean));
    }

}
