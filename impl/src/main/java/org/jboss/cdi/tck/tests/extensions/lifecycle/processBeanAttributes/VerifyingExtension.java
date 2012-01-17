/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.BeanAttributes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBeanAttributes;

public class VerifyingExtension implements Extension {

    private int alphaAttributesObserved = 0;
    private BeanAttributes<Alpha> alphaAttributes;
    private BeanAttributes<Bravo> producedBravoAttributes;
    private BeanAttributes<BravoInterceptor> bravoInterceptorAttributes;
    private BeanAttributes<BravoDecorator> bravoDecoratorAttributes;
    private BeanAttributes<Charlie> producedCharlieAttributes;
    private BeanAttributes<Delta> deltaAttributes;
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

    void observeDelta(@Observes ProcessBeanAttributes<Delta> event) {
        deltaAttributes = event.getBeanAttributes();
        annotatedMap.put(Delta.class, event.getAnnotated());
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

    public BeanAttributes<Delta> getDeltaAttributes() {
        return deltaAttributes;
    }

    public Map<Class<?>, Annotated> getAnnotatedMap() {
        return annotatedMap;
    }

}
