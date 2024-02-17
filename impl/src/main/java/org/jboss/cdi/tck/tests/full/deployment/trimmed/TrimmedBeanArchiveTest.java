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
package org.jboss.cdi.tck.tests.full.deployment.trimmed;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.TestGroups;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = TestGroups.CDI_FULL)
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
    @SpecAssertion(section = Sections.TRIMMED_BEAN_ARCHIVE, id = "a")
    public void testDiscoveredBean() {
        Assert.assertEquals(extension.getVehiclePBAinvocations().get(), 1);
        Bean<MotorizedVehicle> vehicleBean = getUniqueBean(MotorizedVehicle.class);
        CreationalContext<MotorizedVehicle> cc = getCurrentManager().createCreationalContext(vehicleBean);
        MotorizedVehicle vehicle = (MotorizedVehicle) getCurrentManager().getReference(vehicleBean, MotorizedVehicle.class, cc);
        Assert.assertEquals(Bus.class.getSimpleName(), vehicle.start());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.TRIMMED_BEAN_ARCHIVE, id = "a")
    public void testProducerNotDsicovered(Instance<Bike> bikeInstance) {
        Assert.assertTrue(extension.isBikerProducerPATFired());
        Assert.assertFalse(extension.isBikerProducerPBAFired());
        Assert.assertFalse(bikeInstance.isAmbiguous());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = Sections.TRIMMED_BEAN_ARCHIVE, id = "a")
    public void testDiscoveredBeanWithStereoType(Instance<Segway> segwayInstance) {
        Assert.assertFalse(segwayInstance.isUnsatisfied());
    }
}
