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
package org.jboss.cdi.tck.tests.full.extensions.configurators.annotatedTypeConfigurator;

import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.literal.InjectLiteral;
import jakarta.enterprise.inject.spi.AnnotatedConstructor;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.configurator.AnnotatedConstructorConfigurator;
import jakarta.enterprise.inject.spi.configurator.AnnotatedFieldConfigurator;
import jakarta.enterprise.inject.spi.configurator.AnnotatedMethodConfigurator;
import jakarta.enterprise.inject.spi.configurator.AnnotatedParameterConfigurator;
import jakarta.enterprise.inject.spi.configurator.AnnotatedTypeConfigurator;
import jakarta.inject.Inject;

import org.jboss.cdi.tck.literals.DisposesLiteral;
import org.jboss.cdi.tck.literals.ProducesLiteral;
import org.jboss.cdi.tck.util.annotated.AnnotatedTypes;

public class ProcessAnnotatedTypeObserver implements Extension {

    public static AtomicBoolean annotatedTypesEqual = new AtomicBoolean(false);
    public static AtomicBoolean annotatedMethodEqual = new AtomicBoolean(false);
    public static AtomicBoolean annotatedFieldEqual = new AtomicBoolean(false);
    public static AtomicBoolean annotatedConstructorEqual = new AtomicBoolean(false);
    public static AtomicBoolean annotatedParameterEqual = new AtomicBoolean(false);

    private AnnotatedType<Cat> originalCatAT;

    void observesDogPAT(@Observes ProcessAnnotatedType<Dog> event) {

        annotatedTypesEqual.set(AnnotatedTypes.compareAnnotatedTypes(event.configureAnnotatedType().getAnnotated(), event.getAnnotatedType()));
        AnnotatedTypeConfigurator<Dog> annotatedTypeConfigurator = event.configureAnnotatedType();

        // add @RequestScoped to Dog and @Inject and @Dogs to its Feed field
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
        getAMConfiguratorByName(annotatedTypeConfigurator, "produceFeed").add(ProducesLiteral.INSTANCE)
                .add(Dogs.DogsLiteral.INSTANCE);

        // add @Produces and @Created to DogDependenciesProducer.dogName
        annotatedTypeConfigurator.filterFields(af -> af.getJavaMember().getName().equals("dogName")).findFirst().get().add(ProducesLiteral.INSTANCE)
                .add(Dogs.DogsLiteral.INSTANCE);

        // add @Disposes to DogDependenciesProducer.disposeFeed
        getAMConfiguratorByName(annotatedTypeConfigurator, "disposeFeed")
                .filterParams(ap -> ap.getPosition() == 0).findFirst().get().add(DisposesLiteral.INSTANCE).add(Dogs.DogsLiteral.INSTANCE);
    }

    void observesCatPAT(@Observes ProcessAnnotatedType<Cat> event) {

        AnnotatedTypeConfigurator<Cat> annotatedTypeConfigurator = event.configureAnnotatedType();
        originalCatAT = annotatedTypeConfigurator.getAnnotated();

        // remove @RequestScoped from Cat
        annotatedTypeConfigurator.remove(a -> a.annotationType().equals(RequestScoped.class));
        // remove @Produces and @Cats Cat.produceCatName()
        getAMConfiguratorByName(annotatedTypeConfigurator, "produceCatFeed").remove(a -> a.equals(ProducesLiteral.INSTANCE))
                .remove(a -> a.equals(Cats.CatsLiteral.INSTANCE));
        // remove @Inject from Cat.feed
        annotatedTypeConfigurator.filterFields(af -> af.getJavaMember().getName().equals("feed")).findFirst().get()
                .remove(a -> a.equals(InjectLiteral.INSTANCE));

        // remove @Inject and @Cats from constructor with parameter
        annotatedTypeConfigurator.filterConstructors(ac -> ac.getParameters().size() == 1 && ac.getParameters().get(0).getBaseType().equals(Feed.class))
                .findFirst().get().remove(a -> a.equals(InjectLiteral.INSTANCE)).params().get(0).remove(a -> a.equals(Cats.CatsLiteral.INSTANCE));

        // remove @Observes from Cat.observesCatsFeed method parameter
        getAMConfiguratorByName(annotatedTypeConfigurator, "observesCatsFeed")
                .filterParams(ap -> ap.getPosition() == 0).findFirst().get().remove(a -> a.annotationType().equals(Observes.class));

    }

    void observesDogProducerPAT(@Observes ProcessAnnotatedType<DogProducer> event) {
        // remove all @Produces from DogProducer
        AnnotatedTypeConfigurator<DogProducer> annotatedTypeConfigurator = event.configureAnnotatedType();

        annotatedTypeConfigurator.methods().stream().forEach(am -> am.remove(a -> a.annotationType().equals(Produces.class)));
        annotatedTypeConfigurator.fields().stream().forEach(am -> am.remove(a -> a.annotationType().equals(Produces.class)));
    }

    void observesAnimalShelterPAT(@Observes ProcessAnnotatedType<AnimalShelter> event) {
        AnnotatedTypeConfigurator<AnimalShelter> annotatedTypeConfigurator = event.configureAnnotatedType();

        //compare AnnotatedMethod from AnnotatedType to the one from AnnotatedMethodConfigurator
        AnnotatedMethodConfigurator<? super AnimalShelter> methodConfigurator = getAMConfiguratorByName(annotatedTypeConfigurator, "observesRoomInShelter");
        AnnotatedMethod<? super AnimalShelter> annotatedMethod = methodConfigurator.getAnnotated();
        annotatedMethodEqual.set(AnnotatedTypes.compareAnnotatedCallable(
                event.getAnnotatedType().getMethods().stream().filter(m -> m.getJavaMember().getName().equals("observesRoomInShelter")).findAny().get(),
                annotatedMethod));

        // compare AnnotatedConstructor from AnnotatedType to the one from AnnotatedConstructorConfigurator
        AnnotatedConstructorConfigurator<AnimalShelter> constructorConfigurator = annotatedTypeConfigurator
                .filterConstructors(ac -> ac.isAnnotationPresent(Inject.class))
                .findFirst().get();

        AnnotatedConstructor<AnimalShelter> configuratorAnnotatedConstructor = constructorConfigurator.getAnnotated();
        AnnotatedConstructor<AnimalShelter> originalAnnotatedConstructor = event.getAnnotatedType().getConstructors().stream()
                .filter(m -> m.isAnnotationPresent(Inject.class)).findAny().get();

        annotatedConstructorEqual.set(AnnotatedTypes.compareAnnotatedCallable(originalAnnotatedConstructor, configuratorAnnotatedConstructor));
        // compare AnnotatedParameter from AnnotatedType to the one from AnnotatedParameterConfigurator
        annotatedParameterEqual
                .set(AnnotatedTypes.compareAnnotatedParameters(originalAnnotatedConstructor.getParameters(), configuratorAnnotatedConstructor.getParameters()));

        //compare AnnnotatedField from AnnotatedType to the one from AnnotatedFieldConfigurator
        AnnotatedFieldConfigurator<? super AnimalShelter> fieldConfigurator = annotatedTypeConfigurator.filterFields(annotatedField -> {
            return annotatedField.getJavaMember().getName().equals("cat");
        }).findFirst().get();

        AnnotatedField<? super AnimalShelter> annotatedField = fieldConfigurator.getAnnotated();
        annotatedFieldEqual.set(AnnotatedTypes
                .compareAnnotatedField(event.getAnnotatedType().getFields().stream().filter(af -> af.getJavaMember().getName().equals("cat")).findAny().get(),
                        annotatedField));

        // remove all annotations
        annotatedTypeConfigurator.removeAll();
        annotatedTypeConfigurator.constructors().stream().forEach(AnnotatedConstructorConfigurator::removeAll);
        annotatedTypeConfigurator.methods().stream().forEach(AnnotatedMethodConfigurator::removeAll);
        annotatedTypeConfigurator.methods().stream().forEach(annotatedMethodConfigurator -> {
            annotatedMethodConfigurator.params().stream().forEach(AnnotatedParameterConfigurator::removeAll);
        });
        annotatedTypeConfigurator.fields().stream().forEach(AnnotatedFieldConfigurator::removeAll);
    }

    void observesCountrysidePAT(@Observes ProcessAnnotatedType<Countryside> event) {

        AnnotatedConstructorConfigurator<Countryside> annotatedConstructorConfigurator = event.configureAnnotatedType()
                .filterConstructors(constructor -> constructor.isAnnotationPresent(Inject.class)).findFirst().get();

        //add qualifier to each constructor param
        annotatedConstructorConfigurator.params().forEach(annotatedParam -> annotatedParam.add(Wild.WildLiteral.INSTANCE));
    }

    private <T> AnnotatedMethodConfigurator<? super T> getAMConfiguratorByName(AnnotatedTypeConfigurator<T> annotatedTypeConfigurator, String name) {
        return annotatedTypeConfigurator.filterMethods(am -> am.getJavaMember().getName().equals(name)).findFirst().get();
    }

    public AnnotatedType<Cat> getOriginalCatAT() {
        return originalCatAT;
    }

}
