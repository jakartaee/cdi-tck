/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.beanManager.unmanaged.broken;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;

import jakarta.enterprise.inject.spi.Unmanaged;
import jakarta.enterprise.inject.spi.Unmanaged.UnmanagedInstance;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class UnamangedInstanceIllegalStatesTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(UnamangedInstanceIllegalStatesTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL)).build();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertion(section = Sections.BM_OBTAIN_UNMANAGED_INSTANCE, id = "c")
    public void produceCalledOnAlreadyProducedInstance() {
        UnmanagedInstance<House> unmanagedHouseInstance = createUnmanagedInstance();
        unmanagedHouseInstance.produce().get();
        unmanagedHouseInstance.produce();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertion(section = Sections.BM_OBTAIN_UNMANAGED_INSTANCE, id = "d")
    public void produceCalledOnAlreadyDisposedInstance() {
        UnmanagedInstance<House> unmanagedHouseInstance = createUnmanagedInstance();
        House house = unmanagedHouseInstance.produce().get();
        house.build();
        unmanagedHouseInstance.dispose();
        unmanagedHouseInstance.produce();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertion(section = Sections.BM_OBTAIN_UNMANAGED_INSTANCE, id = "e")
    public void injectCallBeforeProduce() {
        UnmanagedInstance<House> unmanagedHouseInstance = createUnmanagedInstance();
        unmanagedHouseInstance.inject();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertion(section = Sections.BM_OBTAIN_UNMANAGED_INSTANCE, id = "f")
    public void injectCalledOnAlreadyDisposedInstance() {
        UnmanagedInstance<House> unmanagedHouseInstance = createUnmanagedInstance();
        House house = unmanagedHouseInstance.produce().get();
        house.build();
        unmanagedHouseInstance.dispose();
        unmanagedHouseInstance.inject();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertion(section = Sections.BM_OBTAIN_UNMANAGED_INSTANCE, id = "g")
    public void posConstructCallBeforeProduce() {
        UnmanagedInstance<House> unmanagedHouseInstance = createUnmanagedInstance();
        unmanagedHouseInstance.postConstruct();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertion(section = Sections.BM_OBTAIN_UNMANAGED_INSTANCE, id = "h")
    public void postConstructCalledOnAlreadyDisposedInstance() {
        UnmanagedInstance<House> unmanagedHouseInstance = createUnmanagedInstance();
        House house = unmanagedHouseInstance.produce().get();
        house.build();
        unmanagedHouseInstance.dispose();
        unmanagedHouseInstance.postConstruct();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertion(section = Sections.BM_OBTAIN_UNMANAGED_INSTANCE, id = "i")
    public void preDestroyCallBeforeProduce() {
        UnmanagedInstance<House> unmanagedHouseInstance = createUnmanagedInstance();
        unmanagedHouseInstance.preDestroy();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertion(section = Sections.BM_OBTAIN_UNMANAGED_INSTANCE, id = "j")
    public void preDeStroyCalledOnAlreadyDisposedInstance() {
        UnmanagedInstance<House> unmanagedHouseInstance = createUnmanagedInstance();
        House house = unmanagedHouseInstance.produce().get();
        house.build();
        unmanagedHouseInstance.dispose();
        unmanagedHouseInstance.preDestroy();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertion(section = Sections.BM_OBTAIN_UNMANAGED_INSTANCE, id = "k")
    public void disposeCallBeforeProduce() {
        UnmanagedInstance<House> unmanagedHouseInstance = createUnmanagedInstance();
        unmanagedHouseInstance.dispose();
    }

    @Test(expectedExceptions = IllegalStateException.class)
    @SpecAssertion(section = Sections.BM_OBTAIN_UNMANAGED_INSTANCE, id = "l")
    public void disposeCalledOnAlreadyDisposedInstance() {
        UnmanagedInstance<House> unmanagedHouseInstance = createUnmanagedInstance();
        House house = unmanagedHouseInstance.produce().get();
        house.build();
        unmanagedHouseInstance.dispose();
        unmanagedHouseInstance.dispose();
    }

    private UnmanagedInstance<House> createUnmanagedInstance() {
        Unmanaged<House> unmanagedHouse = new Unmanaged<House>(getCurrentManager(), House.class);
        UnmanagedInstance<House> unmanagedHouseInstance = unmanagedHouse.newInstance();
        return unmanagedHouseInstance;
    }

}
