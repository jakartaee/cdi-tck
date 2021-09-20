/*
 * JBoss, Home of Professional Open Source
 * Copyright 2017, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.beanDiscovery.event.ordering;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.BeforeBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;
import jakarta.enterprise.inject.spi.ProcessInjectionPoint;
import jakarta.enterprise.inject.spi.ProcessInjectionTarget;
import jakarta.enterprise.inject.spi.ProcessManagedBean;
import jakarta.enterprise.inject.spi.ProcessProducer;
import jakarta.enterprise.inject.spi.ProcessProducerMethod;

import org.jboss.cdi.tck.util.ActionSequence;

/**
 * CDI defines this order (see Bean Discovery chapter) for producers: PIP -> PP -> PBA -> PPM (e.g. ProcessBean for producer
 * methods) And following order for managed beans: PIP -> PIT -> PBA -> PB
 *
 * Of course this can get a lot more complex, but let's cover this basic case.
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class ProductManagement implements Extension {

    public static String PRODUCER_SEQ = "PRODUCER_EVENTS_ORDER_SEQ";
    public static String BEAN_SEQ = "BEAN_EVENTS_ORDER_SEQ";

    public void resetSequence(@Observes BeforeBeanDiscovery bbd) {
        ActionSequence.reset();
    }

    // producer method observers
    public void observePIP(@Observes ProcessInjectionPoint<PoorWorker, String> pip) {
        ActionSequence.addAction(PRODUCER_SEQ, "PIP");
    }

    public void observePP(@Observes ProcessProducer<PoorWorker, HighQualityAndLowCostProduct> pp) {
        ActionSequence.addAction(PRODUCER_SEQ, "PP");
    }

    public void observePBA(@Observes ProcessBeanAttributes<HighQualityAndLowCostProduct> pba) {
        ActionSequence.addAction(PRODUCER_SEQ, "PBA");
    }

    public void observerPPM(@Observes ProcessProducerMethod<HighQualityAndLowCostProduct, PoorWorker> ppm) {
        ActionSequence.addAction(PRODUCER_SEQ, "PPM");
    }

    // managed bean observers - PIP - PIT - PBA - PB
    public void observePIPBean(@Observes ProcessInjectionPoint<PoorWorker, MassiveJugCoffee> pip) {
        ActionSequence.addAction(BEAN_SEQ, "PIP");
    }

    public void observePIT(@Observes ProcessInjectionTarget<PoorWorker> pit) {
        ActionSequence.addAction(BEAN_SEQ, "PIT");
    }

    public void observePBABean(@Observes ProcessBeanAttributes<PoorWorker> pba) {
        ActionSequence.addAction(BEAN_SEQ, "PBA");
    }

    public void observerPB(@Observes ProcessManagedBean<PoorWorker> pb) {
        ActionSequence.addAction(BEAN_SEQ, "PB");
    }
}
