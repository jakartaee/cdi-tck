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

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessInjectionPoint;

public class ProcessInjectionPointObserver implements Extension {

    private Bean<MotorBike> motorBikeBean;

    void observesPB(ProcessBean<MotorBike> event) {
        motorBikeBean = event.getBean();
    }

    void observesCarPIPEngine(@Observes ProcessInjectionPoint<Car, Engine> event) {
        event.configureInjectionPoint().bean(motorBikeBean).type(Tank.class);
    }

    void observesAirPlanePIP(@Observes ProcessInjectionPoint<AirPlane, Tank> event) {

        // change type of constructor param IP and add @Flying qualifier
        event.configureInjectionPoint()
                .type(Engine.class)
                .addQualifier(Flying.FlyingLiteral.INSTANCE);
    }

    void observesCarPIPTank(@Observes ProcessInjectionPoint<Car, Tank> event) {

        event.configureInjectionPoint()
                .qualifiers(Riding.RidingLiteral.INSTANCE)
                .delegate(true);
    }

    void observesShipPIPEngine(@Observes ProcessInjectionPoint<Ship, Engine> event) {
        event.configureInjectionPoint().transientField(true);
    }

}
