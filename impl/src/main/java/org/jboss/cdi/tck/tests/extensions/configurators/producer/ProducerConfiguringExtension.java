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
package org.jboss.cdi.tck.tests.extensions.configurators.producer;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionPoint;
import javax.enterprise.inject.spi.ProcessProducer;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class ProducerConfiguringExtension implements Extension {

    public static AtomicBoolean producerCalled = new AtomicBoolean(false);
    public static AtomicBoolean disposerCalled = new AtomicBoolean(false);

    private final String barFieldOneName = "barOne";
    private final String barFieldTwoName = "barTwo";
    private final String barFieldThreeName = "barThree";
    private final String barFieldFourName = "barFour";
    private final String barFieldFiveName = "barFive";
    private final String barFieldSixName = "barSix";
    private final String barFieldSevenName = "barSeven";
    private final String barFieldEightName = "barEight";

    private InjectionPoint barOneIP;
    private InjectionPoint barTwoIP;
    private InjectionPoint barThreeIP;
    private InjectionPoint barFourIP;
    private InjectionPoint barFiveIP;
    private InjectionPoint barSixIP;
    private InjectionPoint barSevenIP;
    private InjectionPoint barEightIP;

    public void storeInjectionPoints(@Observes ProcessInjectionPoint<Bar, SomeBeanWithInjectionPoints> event) {
        if (event.getInjectionPoint().getMember().getName().equals(barFieldOneName)) {
            barOneIP = event.getInjectionPoint();
        } else if (event.getInjectionPoint().getMember().getName().equals(barFieldTwoName)) {
            barTwoIP = event.getInjectionPoint();
        } else if (event.getInjectionPoint().getMember().getName().equals(barFieldThreeName)) {
            barThreeIP = event.getInjectionPoint();
        } else if (event.getInjectionPoint().getMember().getName().equals(barFieldFourName)) {
            barFourIP = event.getInjectionPoint();
        } else if (event.getInjectionPoint().getMember().getName().equals(barFieldFiveName)) {
            barFiveIP = event.getInjectionPoint();
        } else if (event.getInjectionPoint().getMember().getName().equals(barFieldSixName)) {
            barSixIP = event.getInjectionPoint();
        } else if (event.getInjectionPoint().getMember().getName().equals(barFieldSevenName)) {
            barSevenIP = event.getInjectionPoint();
        } else if (event.getInjectionPoint().getMember().getName().equals(barFieldEightName)) {
            barEightIP = event.getInjectionPoint();
        }
    }

    public void observeProducer(@Observes ProcessProducer<BarProducer, Bar> event) {
        // producer with @YetAnother
        if (isProducerWithAnnotation(event, YetAnother.YetAnotherLiteral.INSTANCE)) {
            event.configureProducer().addInjectionPoint(barOneIP);
            // producer with @Some
        } else if (isProducerWithAnnotation(event, Some.SomeLiteral.INSTANCE)) {
            event.configureProducer().addInjectionPoints(barTwoIP, barThreeIP);
            // producer with @OneAmongMany
        } else if (isProducerWithAnnotation(event, OneAmongMany.OneAmongManyLiteral.INSTANCE)) {
            Set<InjectionPoint> ipSet = new HashSet<>();
            ipSet.add(barFourIP);
            ipSet.add(barFiveIP);
            event.configureProducer().addInjectionPoints(ipSet);
            // producer with @Default
        } else if (isProducerWithAnnotation(event, Default.Literal.INSTANCE)) {
            event.configureProducer().injectionPoints(barSixIP, barSevenIP);
            // producer with @Another
        } else if (isProducerWithAnnotation(event, Another.AnotherLiteral.INSTANCE)) {
            // set producer and disposer and add boolean to test they were called
            event.configureProducer().disposeWith(new Consumer<Bar>() {

                @Override
                public void accept(Bar t) {
                    disposerCalled.set(true);
                }
            }).produceWith(new Function<CreationalContext<Bar>, Bar>() {

                @Override
                public Bar apply(CreationalContext<Bar> t) {
                    producerCalled.set(true);
                    return new Bar(Default.class);
                }
            }).injectionPoints(barEightIP);
        }
    }

    private boolean isProducerWithAnnotation(ProcessProducer<BarProducer, Bar> event, Annotation annotation) {
        return event.getAnnotatedMember().getAnnotations().contains(annotation);
    }
}
