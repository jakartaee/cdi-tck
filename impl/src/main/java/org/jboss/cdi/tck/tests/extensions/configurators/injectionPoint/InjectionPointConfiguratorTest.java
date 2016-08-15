/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.tests.extensions.configurators.injectionPoint;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Decorator;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@Test
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class InjectionPointConfiguratorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InjectionPointConfiguratorTest.class)
                .withExtension(ProcessInjectionPointObserver.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = Sections.INJECTION_POINT_CONFIGURATOR, id = "ba"),
            @SpecAssertion(section = Sections.INJECTION_POINT_CONFIGURATOR, id = "bb") })
    public void changeTypeAndAddQualifier() {
        Bean<AirPlane> airPlaneBean = getUniqueBean(AirPlane.class);
        Assert.assertEquals(airPlaneBean.getInjectionPoints().size(), 1);
        InjectionPoint engineIP = airPlaneBean.getInjectionPoints().iterator().next();
        Assert.assertNotNull(engineIP);
        Assert.assertEquals(engineIP.getType(), Engine.class);
        Assert.assertEquals(engineIP.getQualifiers(), Collections.singleton(Flying.FlyingLiteral.INSTANCE));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = Sections.INJECTION_POINT_CONFIGURATOR, id = "bc"),
            @SpecAssertion(section = Sections.INJECTION_POINT_CONFIGURATOR, id = "be")
    })
    public void replaceQualifiersAndDelegate() {
        List<Decorator<?>> vehicleDecorators = getCurrentManager().resolveDecorators(Collections.<Type>singleton(Car.class), Driving.DrivingLiteral.INSTANCE);
        Assert.assertEquals(vehicleDecorators.size(), 1);
        Decorator<Car> vehicleDecorator = (Decorator<Car>) vehicleDecorators.get(0);
        Assert.assertEquals(vehicleDecorator.getInjectionPoints().size(), 1);
        InjectionPoint vehicleIp = vehicleDecorator.getInjectionPoints().iterator().next();
        Assert.assertEquals(vehicleIp.isDelegate(), true);
        Assert.assertEquals(vehicleIp.getQualifiers(), Collections.singleton(Driving.DrivingLiteral.INSTANCE));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = Sections.INJECTION_POINT_CONFIGURATOR, id = "bf") })
    public void readFromFieldAndCheckTransientField() {
        Bean<Ship> shipBean = getUniqueBean(Ship.class);
        Assert.assertEquals(shipBean.getInjectionPoints().size(), 1);
        InjectionPoint engineIP = shipBean.getInjectionPoints().iterator().next();
        Assert.assertEquals(engineIP.isTransient(), true);
    }
}
