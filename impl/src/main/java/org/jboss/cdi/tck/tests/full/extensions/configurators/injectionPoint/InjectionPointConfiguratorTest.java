/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.configurators.injectionPoint;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.INJECTION_POINT_CONFIGURATOR;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_INJECTION_POINT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.Decorator;
import jakarta.enterprise.inject.spi.InjectionPoint;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class InjectionPointConfiguratorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InjectionPointConfiguratorTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withExtension(ProcessInjectionPointObserver.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTION_POINT_CONFIGURATOR, id = "ba"),
            @SpecAssertion(section = INJECTION_POINT_CONFIGURATOR, id = "bb") })
    public void changeTypeAndAddQualifier() {
        Bean<AirPlane> airPlaneBean = getUniqueBean(AirPlane.class);
        assertEquals(airPlaneBean.getInjectionPoints().size(), 1);
        InjectionPoint engineIP = airPlaneBean.getInjectionPoints().iterator().next();
        assertNotNull(engineIP);
        assertEquals(engineIP.getType(), Engine.class);
        assertEquals(engineIP.getQualifiers(), Collections.singleton(Flying.FlyingLiteral.INSTANCE));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTION_POINT_CONFIGURATOR, id = "bc"),
            @SpecAssertion(section = INJECTION_POINT_CONFIGURATOR, id = "be")
    })
    public void replaceQualifiersAndDelegate() {
        List<Decorator<?>> vehicleDecorators = getCurrentManager().resolveDecorators(Collections.<Type> singleton(Car.class),
                Driving.DrivingLiteral.INSTANCE);
        assertEquals(vehicleDecorators.size(), 1);
        Decorator<Car> vehicleDecorator = (Decorator<Car>) vehicleDecorators.get(0);
        assertEquals(vehicleDecorator.getInjectionPoints().size(), 1);
        InjectionPoint vehicleIp = vehicleDecorator.getInjectionPoints().iterator().next();
        assertEquals(vehicleIp.isDelegate(), true);
        assertEquals(vehicleIp.getQualifiers(), Collections.singleton(Driving.DrivingLiteral.INSTANCE));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTION_POINT_CONFIGURATOR, id = "bf") })
    public void readFromFieldAndCheckTransientField() {
        Bean<Ship> shipBean = getUniqueBean(Ship.class);
        assertEquals(shipBean.getInjectionPoints().size(), 1);
        InjectionPoint engineIP = shipBean.getInjectionPoints().iterator().next();
        assertEquals(engineIP.isTransient(), true);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "bba") })
    public void configuratorInitializedWithOriginalInjectionPoint() {
        InjectionPoint configuredOne = getUniqueBean(Helicopter.class).getInjectionPoints().iterator().next();
        InjectionPoint originalOne = getCurrentManager().getExtension(ProcessInjectionPointObserver.class).getEngineIP();
        assertEquals(configuredOne.getType(), originalOne.getType());
        assertEquals(configuredOne.getQualifiers(), originalOne.getQualifiers());
        assertEquals(configuredOne.getAnnotated(), originalOne.getAnnotated());
        assertEquals(configuredOne.getBean(), originalOne.getBean());
    }
}
