/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.extensions.alternative.metadata.annotated;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class ObservingExtension implements Extension {

    private AnnotatedType<Kryten> kryten;

    private AnnotatedType<Rimmer> rimmer;

    private AnnotatedType<Android> android;

    private List<AnnotatedType<Android>> allAndroids = new ArrayList<AnnotatedType<Android>>();

    private List<AnnotatedType<Human>> allHumans = new ArrayList<AnnotatedType<Human>>();

    public void observeAndroid(@Observes ProcessAnnotatedType<Kryten> event) {
        kryten = event.getAnnotatedType();
    }

    public void observeRimmer(@Observes ProcessAnnotatedType<Rimmer> event) {
        rimmer = event.getAnnotatedType();
    }

    /**
     * Store the result, don't verify anything since it's more transparent to have assertions in the test class methods.
     *
     * The methods {@link BeanManager#getAnnotatedType(Class, String)} and {@link BeanManager#getAnnotatedTypes(Class)} will be
     * probably placed on the {@link AfterBeanDiscovery}. See also CDI-83.
     *
     * @param event
     * @param beanManager
     */
    public void observeAfterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager beanManager) {

        for (AnnotatedType<Human> annotatedType : event.getAnnotatedTypes(Human.class)) {
            allHumans.add(annotatedType);
        }
        for (AnnotatedType<Android> annotatedType : event.getAnnotatedTypes(Android.class)) {
            allAndroids.add(annotatedType);
        }
        android = event.getAnnotatedType(Android.class, null);
    }

    public AnnotatedType<Kryten> getKryten() {
        return kryten;
    }

    public AnnotatedType<Rimmer> getRimmer() {
        return rimmer;
    }

    public AnnotatedType<Android> getAndroid() {
        return android;
    }

    public List<AnnotatedType<Human>> getAllHumans() {
        return allHumans;
    }

    public List<AnnotatedType<Android>> getAllAndroids() {
        return allAndroids;
    }

}
