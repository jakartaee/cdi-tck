/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle.processBeanAttributes;

import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.BeanAttributes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;

public class VerifyingExtension implements Extension {

    private int alphaAttributesObserved = 0;
    private BeanAttributes<Alpha> alphaAttributes;
    private BeanAttributes<Bravo> producedBravoAttributes;
    private BeanAttributes<BravoInterceptor> bravoInterceptorAttributes;
    private BeanAttributes<BravoDecorator> bravoDecoratorAttributes;
    private BeanAttributes<Charlie> producedCharlieAttributes;
    private BeanAttributes<Mike> mike;

    private Map<Class<?>, Annotated> annotatedMap = new HashMap<Class<?>, Annotated>();

    void observeAlpha(@Observes ProcessBeanAttributes<Alpha> event) {
        alphaAttributesObserved++;
        alphaAttributes = event.getBeanAttributes();
        annotatedMap.put(Alpha.class, event.getAnnotated());
    }

    void observeBravo(@Observes ProcessBeanAttributes<Bravo> event) {
        if (event.getAnnotated() instanceof AnnotatedMethod) {
            // We only want producer method
            producedBravoAttributes = event.getBeanAttributes();
            annotatedMap.put(Bravo.class, event.getAnnotated());
        }
    }

    void observeBravoInterceptor(@Observes ProcessBeanAttributes<BravoInterceptor> event) {
        bravoInterceptorAttributes = event.getBeanAttributes();
    }

    void observeBravoDecorator(@Observes ProcessBeanAttributes<BravoDecorator> event) {
        bravoDecoratorAttributes = event.getBeanAttributes();
    }

    void observeCharlie(@Observes ProcessBeanAttributes<Charlie> event) {
        if (event.getAnnotated() instanceof AnnotatedField) {
            // We only want producer field
            producedCharlieAttributes = event.getBeanAttributes();
            annotatedMap.put(Charlie.class, event.getAnnotated());
        }
    }

    void observeMike(@Observes ProcessBeanAttributes<Mike> event) {
        mike = event.getBeanAttributes();
    }

    protected BeanAttributes<Alpha> getAlphaAttributes() {
        return alphaAttributes;
    }

    public int getAlphaAttributesObserved() {
        return alphaAttributesObserved;
    }

    protected BeanAttributes<Bravo> getProducedBravoAttributes() {
        return producedBravoAttributes;
    }

    protected BeanAttributes<Charlie> getProducedCharlieAttributes() {
        return producedCharlieAttributes;
    }

    public BeanAttributes<BravoInterceptor> getBravoInterceptorAttributes() {
        return bravoInterceptorAttributes;
    }

    public BeanAttributes<BravoDecorator> getBravoDecoratorAttributes() {
        return bravoDecoratorAttributes;
    }

    public BeanAttributes<Mike> getMike() {
        return mike;
    }

    public Map<Class<?>, Annotated> getAnnotatedMap() {
        return annotatedMap;
    }

}
