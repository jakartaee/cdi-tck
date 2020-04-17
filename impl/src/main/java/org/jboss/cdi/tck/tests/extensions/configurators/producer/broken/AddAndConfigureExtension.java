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
package org.jboss.cdi.tck.tests.extensions.configurators.producer.broken;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.ProcessProducer;
import jakarta.enterprise.inject.spi.Producer;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class AddAndConfigureExtension implements Extension {

    public static AtomicBoolean configureThanSetExceptionThrown = new AtomicBoolean(false);
    public static AtomicBoolean setThanConfigureExceptionThrown = new AtomicBoolean(false);

    public void configureThanSet(@Observes ProcessProducer<ProducerBean, Foo> event) {
        event.configureProducer(); // replace all IPs with empty array
        // trying to call setProducer after configureProducer should raise an exception
        try {
            event.setProducer(new Producer<Foo>() {

                @Override
                public void dispose(Foo instance) {
                    //no-op
                }

                @Override
                public Set<InjectionPoint> getInjectionPoints() {
                    return new HashSet<>();
                }

                @Override
                public Foo produce(CreationalContext<Foo> ctx) {
                    return new Foo();
                }
            });
        } catch (IllegalStateException e) {
            //expected
            configureThanSetExceptionThrown.set(true);
        }
    }

    public void setThanConfigure(@Observes ProcessProducer<AnotherProducerBean, Foo> event) {
        event.setProducer(new Producer<Foo>() {

            @Override
            public void dispose(Foo instance) {
                //no-op
            }

            @Override
            public Set<InjectionPoint> getInjectionPoints() {
                return new HashSet<>();
            }

            @Override
            public Foo produce(CreationalContext<Foo> ctx) {
                return new Foo();
            }
        });
        // calling configure after set should raise exception
        try {
            event.configureProducer(); // replace all IPs with empty array
        } catch (IllegalStateException e) {
            //expected
            setThanConfigureExceptionThrown.set(true);
        }

    }
}
