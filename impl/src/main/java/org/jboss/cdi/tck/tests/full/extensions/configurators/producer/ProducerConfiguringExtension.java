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
package org.jboss.cdi.tck.tests.full.extensions.configurators.producer;

import java.lang.annotation.Annotation;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessProducer;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class ProducerConfiguringExtension implements Extension {

    public static AtomicBoolean producerCalled = new AtomicBoolean(false);
    public static AtomicBoolean disposerCalled = new AtomicBoolean(false);

    public void observeProducer(@Observes ProcessProducer<MassProducer, Bar> event) {

        if (isProducerWithAnnotation(event, Some.SomeLiteral.INSTANCE)) {
            // Bar producer with @Some - change create/dispose
            event.configureProducer().disposeWith(new Consumer<Bar>() {

                @Override
                public void accept(Bar t) {
                    disposerCalled.set(true);
                }
            }).produceWith(new Function<CreationalContext<Bar>, Bar>() {

                @Override
                public Bar apply(CreationalContext<Bar> t) {
                    producerCalled.set(true);
                    return new Bar(new ParameterInjectedBean(null));
                }
            });
        }
    }

    private boolean isProducerWithAnnotation(ProcessProducer<MassProducer, Bar> event, Annotation annotation) {
        return event.getAnnotatedMember().getAnnotations().contains(annotation);
    }
}
