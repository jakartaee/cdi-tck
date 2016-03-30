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
package org.jboss.cdi.tck.tests.extensions.configurators.BeanAttributesConfigurator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBeanAttributes;
import javax.enterprise.inject.spi.builder.BeanAttributesConfigurator;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
public class ProcessBeanAttributesObserver implements Extension {

    public static AnnotatedType<Axe> annotatedType;

    public void observeSword(@Observes ProcessBeanAttributes<Sword> pba) {
        BeanAttributesConfigurator<Sword> configurator = pba.configureBeanAttributes();

        // init configurator with bean attributes
        // add qualifier @TwoHanded
        // add Type Weapon.class
        // set name
        // set stereotype 
        configurator.read(pba.getBeanAttributes());
        configurator.addQualifier(TwoHanded.TwoHandedLiteral.INSTANCE);
        configurator.addType(Weapon.class);
        configurator.name(BeanAttributesConfiguratorTest.SWORD_NAME);
        configurator.addStereotype(Equipment.class); //cannot use literal, have to use class due to type Class<? extends Annotation>
    }

    public void observeAxe(@Observes ProcessBeanAttributes<Axe> pba) {
        BeanAttributesConfigurator<Axe> configurator = pba.configureBeanAttributes();

        // init configurator with previously stored annotated type
        // make alternative
        // add multiple qualifiers (TwoHanded, Reforged)
        // add multiple stereotypes (Equipment, Melee)
        // add multiple types (Weapon, Tool)
        // change scope to RequestScoped
        configurator.read(annotatedType);
        configurator.alternative(true);
        configurator.addQualifiers(getAxeQualifiers());
        configurator.addStereotypes(getStereotypes());
        configurator.types(getAxeTypes());
        configurator.scope(RequestScoped.class);
    }
    
    public void observeHoe(@Observes ProcessBeanAttributes<Hoe> pba) {
        BeanAttributesConfigurator<Hoe> configurator = pba.configureBeanAttributes();

        // init configurator with bean attributes
        // add types with closure method (adding a Tool.java will result in having a type UsableItem as well)
        // replace @Melee stereotype with @Equipment
        // replace qualifier @TwoHanded with @Reforged
        configurator.addTransitiveTypeClosure(Tool.class);
        Set<Class<? extends Annotation>> stereotypes = getStereotypes();
        stereotypes.remove(Melee.class);
        configurator.stereotypes(stereotypes);
        configurator.qualifiers(Reforged.ReforgedLiteral.INSTANCE);
    }

    public void observePAT(@Observes ProcessAnnotatedType<Axe> pat) {
        annotatedType = pat.getAnnotatedType();
    }
    
    private Set<Annotation> getAxeQualifiers() {
        Set<Annotation> result = new HashSet<>();
        result.add(TwoHanded.TwoHandedLiteral.INSTANCE);
        result.add(Reforged.ReforgedLiteral.INSTANCE);
        return result;
    }
    
    private Set<Class<? extends Annotation>> getStereotypes() {
        Set<Class<? extends Annotation>> result = new HashSet<>();
        result.add(Equipment.class);
        result.add(Melee.class);
        return result;
    }
    
    private Set<Type> getAxeTypes() {
        Set<Type> result = new HashSet<>();
        result.add(Weapon.class);
        result.add(Tool.class);
        return result;
    }
}
