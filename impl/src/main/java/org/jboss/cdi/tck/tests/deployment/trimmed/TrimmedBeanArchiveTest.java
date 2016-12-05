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
package org.jboss.cdi.tck.tests.deployment.trimmed;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class TrimmedBeanArchiveTest extends AbstractTest {

    @Inject
    TestExtension extension;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(TrimmedBeanArchiveTest.class)
                .withExtension(TestExtension.class)
                .withBeansXml("beans.xml").build();
    }

    @Test
    public void testDiscoveredBeansWithDecorator() {
        Assert.assertEquals(1, extension.getVehiclePBAinvocations().get());
        Bean<MotorizedVehicle> vehicleBean = getUniqueBean(MotorizedVehicle.class);
        CreationalContext<MotorizedVehicle> cc = getCurrentManager().createCreationalContext(vehicleBean);
        MotorizedVehicle vehicle = (MotorizedVehicle) getCurrentManager().getReference(vehicleBean, MotorizedVehicle.class, cc);
        Assert.assertEquals("decorated bus start", vehicle.start());
        Assert.assertEquals(1, MotorizedVehicleDecorator.invocations);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    public void testDiscoveredBeans(Instance<Bike> bikeInstance) {
        Assert.assertFalse(extension.getBikerProducerProcessed().get());
        Assert.assertFalse(bikeInstance.isAmbiguous());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    public void testDiscoveredBeansWithInterceptor(Instance<Segway> segwayInstance){
       Assert.assertFalse(segwayInstance.isUnsatisfied());
       segwayInstance.get().start();
       Assert.assertTrue(SegwayInterceptor.intercepted);
    }
}
