/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.beanManager;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.PassivationCapable;

public class CowBean implements Bean<Cow>, PassivationCapable, Serializable {
    private static final long serialVersionUID = 6249623250272328272L;
    public static final String PASSIVATION_ID = "Cow-6249623250272328272L";
    private final Set<Annotation> qualifiers = new HashSet<Annotation>(Arrays.asList(Default.Literal.INSTANCE));
    private final Set<Type> types = new HashSet<Type>(Arrays.<Type> asList(Cow.class));

    public Class<?> getBeanClass() {
        return Cow.class;
    }

    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    public String getName() {
        return "cow";
    }

    public Set<Annotation> getQualifiers() {
        return qualifiers;
    }

    public Class<? extends Annotation> getScope() {
        return Dependent.class;
    }

    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    public Set<Type> getTypes() {
        return types;
    }

    public boolean isAlternative() {
        return false;
    }

    public boolean isNullable() {
        return false;
    }

    public Cow create(CreationalContext<Cow> creationalContext) {
        return new Cow("Betsy");
    }

    public void destroy(Cow instance, CreationalContext<Cow> creationalContext) {
    }

    public String getId() {
        return PASSIVATION_ID;
    }

}
