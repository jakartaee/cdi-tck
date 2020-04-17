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

package org.jboss.cdi.tck.tests.decorators;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Decorator;

import org.jboss.cdi.tck.AbstractTest;

/**
 * @author Martin Kouba
 * 
 */
public abstract class AbstractDecoratorTest extends AbstractTest {

    protected Decorator<?> resolveUniqueDecorator(Set<Type> types, Annotation... qualifiers) {
        return resolveUniqueDecorator(null, types, qualifiers);
    }

    protected Decorator<?> resolveUniqueDecorator(BeanManager currentBeanManager, Set<Type> types, Annotation... qualifiers) {
        if (currentBeanManager == null) {
            currentBeanManager = getCurrentManager();
        }
        List<Decorator<?>> decorators = currentBeanManager.resolveDecorators(types, qualifiers);
        assertNotNull(decorators);
        assertEquals(decorators.size(), 1);
        return decorators.iterator().next();

    }

    protected void checkDecorator(Decorator<?> decorator, Class<?> beanClass, Set<Type> decoratedTypes, Type delegateType) {
        assertEquals(decorator.getBeanClass(), beanClass);
        assertEquals(decorator.getDecoratedTypes(), decoratedTypes);
        assertEquals(decorator.getDelegateType(), delegateType);
    }

}
