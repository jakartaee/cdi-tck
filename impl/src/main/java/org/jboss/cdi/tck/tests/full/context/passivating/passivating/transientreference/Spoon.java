/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.context.passivating.passivating.transientreference;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.TransientReference;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

@SuppressWarnings("serial")
@SessionScoped
public class Spoon implements Serializable {

    private boolean isConstructorParamInjected = false;
    private boolean isInitParamInjected = false;

    public Spoon() {
    }

    @Inject
    public Spoon(BeanManager beanManager, @TransientReference Meal meal) {
        isConstructorParamInjected = (meal != null ? true : false);
    }

    @Inject
    public void initMeal2(@TransientReference Meal meal, BeanManager beanManager) {
        isInitParamInjected = (meal != null ? true : false);
    }

    public boolean isConstructorParamInjected() {
        return isConstructorParamInjected;
    }

    public boolean isInitParamInjected() {
        return isInitParamInjected;
    }

}
