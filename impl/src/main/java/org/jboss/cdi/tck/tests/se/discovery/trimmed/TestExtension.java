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
package org.jboss.cdi.tck.tests.se.discovery.trimmed;

import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.ProcessBean;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;

public class TestExtension implements Extension {

    private AtomicBoolean barProducerPBAFired = new AtomicBoolean(false);
    private AtomicBoolean barPATFired = new AtomicBoolean(false);
    private AtomicBoolean barPBFired = new AtomicBoolean(false);


    void observesBarProducerPBA(@Observes ProcessBeanAttributes<BarProducer> event) {
        barProducerPBAFired.set(true);
    }

    void observesBarPAT(@Observes ProcessAnnotatedType<Bar> event) {
        barPATFired.set(true);
    }

    void observesBarPB(@Observes ProcessBean<Bar> event) {
        barPBFired.set(true);
    }

    public boolean getBarProducerPBAFired() {
        return barProducerPBAFired.get();
    }

    public boolean getBarPATFired() {
        return barPATFired.get();
    }

    public boolean getBarPBFired() {
        return barPBFired.get();
    }
}
