/*
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.UnsatisfiedResolutionException;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.util.TypeLiteral;

/**
 * Simple helper class for contextual reference lookup.
 *
 * @author Martin Kouba
 */
public final class BeanLookupUtils {

    private static final SimpleLogger logger = new SimpleLogger(BeanLookupUtils.class);

    private BeanLookupUtils() {
    }

    /**
     *
     * @param beanManager
     * @param beanType
     * @param qualifiers
     * @return
     */
    public static <T> T getContextualReference(BeanManager beanManager, Class<T> beanType, Annotation... qualifiers) {
        Set<Bean<?>> beans = getBeans(beanManager, beanType, qualifiers);
        return BeanLookupUtils.<T>getContextualReference(beanManager, beanType, beans);
    }

    /**
     *
     * @param beanManager
     * @param beanTypeLiteral
     * @param qualifiers
     * @return
     */
    public static <T> T getContextualReference(BeanManager beanManager, TypeLiteral<T> beanTypeLiteral,
            Annotation... qualifiers) {
        Type beanType = beanTypeLiteral.getType();
        Set<Bean<?>> beans = getBeans(beanManager, beanType, qualifiers);
        return BeanLookupUtils.<T>getContextualReference(beanManager, beanType, beans);
    }

    /**
     *
     * @param beanManager
     * @param beanType
     * @param name
     * @return
     */
    public static <T> T getContextualReference(BeanManager beanManager, String name, Class<T> beanType) {

        Set<Bean<?>> beans = beanManager.getBeans(name);

        if (beans == null || beans.isEmpty()) {
            return null;
        }
        return BeanLookupUtils.<T>getContextualReference(beanManager, beanType, beans);
    }

    private static Set<Bean<?>> getBeans(BeanManager beanManager, Type beanType, Annotation... qualifiers) {

        Set<Bean<?>> beans = beanManager.getBeans(beanType, qualifiers);

        if (beans == null || beans.isEmpty()) {
            throw new UnsatisfiedResolutionException(String.format(
                    "No bean matches required type %s and required qualifiers %s", beanType, Arrays.toString(qualifiers)));
        }
        return beans;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getContextualReference(BeanManager beanManager, Type beanType, Set<Bean<?>> beans) {

        Bean<?> bean = beanManager.resolve(beans);

        if (bean.getScope().equals(Dependent.class)) {
            logger.log("Dependent contextual instance cannot be properly destroyed!");
        }

        CreationalContext<?> creationalContext = beanManager.createCreationalContext(bean);

        return ((T) beanManager.getReference(bean, beanType, creationalContext));
    }

}
