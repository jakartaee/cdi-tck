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
package org.jboss.cdi.tck.tests.extensions.configurators.annotatedTypeConfigurator;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.literal.InjectLiteral;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.builder.AnnotatedTypeConfigurator;

import org.jboss.cdi.tck.literals.DisposesLiteral;
import org.jboss.cdi.tck.literals.ProducesLiteral;

public class ProcessAnnotatedTypeObserver implements Extension {

    void observesDogPAT(@Observes ProcessAnnotatedType<Dog> event) {

        AnnotatedTypeConfigurator<Dog> annotatedTypeConfigurator = event.configureAnnotatedType();

        // add @RequestScoped to Dog and @Inject and @Dogs to its Feed field
        // add @Inject to Dog constructor
        annotatedTypeConfigurator.add(RequestScoped.Literal.INSTANCE)
                .filterFields(af -> (af.getJavaMember()
                        .getName().equals("feed"))).findFirst().get().add(InjectLiteral.INSTANCE).add(Dogs.DogsLiteral.INSTANCE);

        // add @Inject to Dog constructor and @Dogs to its param
        annotatedTypeConfigurator.constructors().iterator().next().add(InjectLiteral.INSTANCE)
                .filterParams(ap -> ap.getPosition() == 0).findFirst().get().add(Dogs.DogsLiteral.INSTANCE);

    }

    void observesDogDependenciesProducerPAT(@Observes ProcessAnnotatedType<DogDependenciesProducer> event) {

        AnnotatedTypeConfigurator<DogDependenciesProducer> annotatedTypeConfigurator = event.configureAnnotatedType();

        // add @Produces and @Created to DogDependenciesProducer.produceFeed
        annotatedTypeConfigurator.filterMethods(am -> am.getJavaMember().getName().equals("produceFeed")).findFirst().get().add(ProducesLiteral.INSTANCE)
                .add(Dogs.DogsLiteral.INSTANCE);

        // add @Produces and @Created to DogDependenciesProducer.dogName
        annotatedTypeConfigurator.filterFields(af -> af.getJavaMember().getName().equals("dogName")).findFirst().get().add(ProducesLiteral.INSTANCE)
                .add(Dogs.DogsLiteral.INSTANCE);

        // add @Disposes to DogDependenciesProducer.disposeFeed
        annotatedTypeConfigurator.filterMethods(am -> am.getJavaMember().getName().equals("disposeFeed")).findFirst().get()
                .filterParams(ap -> ap.getPosition() == 0).findFirst().get().add(DisposesLiteral.INSTANCE);
    }

    void observesCatPAT(@Observes ProcessAnnotatedType<Cat> event) {

        AnnotatedTypeConfigurator<Cat> annotatedTypeConfigurator = event.configureAnnotatedType();

        // remove @RequestScoped from Cat
        annotatedTypeConfigurator.remove(RequestScoped.class);
        // remove @Produces and @Cats Cat.produceCatName()
        annotatedTypeConfigurator.filterMethods(am -> am.getJavaMember().getName().equals("produceCatName")).findFirst().get().remove(ProducesLiteral.INSTANCE)
                .remove(Cats.CatsLiteral.INSTANCE);
        // remove @Inject from Cat.feed
        annotatedTypeConfigurator.filterFields(af -> af.getJavaMember().getName().equals("feed")).findFirst().get().remove(InjectLiteral.INSTANCE);

        // remove @Inject and @Cats from constructor with parameter
        annotatedTypeConfigurator.filterConstructors(ac -> ac.getParameters().size() > 0).findFirst().get().remove(InjectLiteral.INSTANCE)
                .filterParams(ap -> ap.getPosition() == 0).findFirst().get().remove(Cats.CatsLiteral.INSTANCE);

        // remove @Observes from Cat.observesCatsFeed method parameter
        annotatedTypeConfigurator.filterMethods(am -> am.getJavaMember().getName().equals("observersCatsFeed")).findFirst().get()
                .filterParams(ap -> ap.getPosition() == 0).findFirst().get().remove(Observes.class);

    }

    void observesDogProducerPAT(@Observes ProcessAnnotatedType<DogProducer> event) {
        // remove all @Produces from DogProducer
        event.configureAnnotatedType().methods().stream().forEach(am -> am.remove(Produces.class));

    }

}
