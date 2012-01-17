/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.inject.AmbiguousResolutionException;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.TypeLiteral;

public class OldSPIBridge {

    /**
     * This used to be an spi method of BeanManager, but it has been removed. Replicate it here using the new spi, and deprecate
     * its usage
     * 
     */
    @Deprecated
    public static <T> T getInstanceByType(BeanManager manager, Class<T> beanType, Annotation... bindings) {
        Bean<T> bean = (Bean<T>) ensureUniqueBean(beanType, manager.getBeans(beanType, bindings));
        return (T) manager.getReference(bean, beanType, manager.createCreationalContext(bean));
    }

    @Deprecated
    public static <T> T getInstanceByType(BeanManager manager, TypeLiteral<T> beanType, Annotation... bindings) {
        Bean<T> bean = (Bean<T>) ensureUniqueBean(beanType.getType(), manager.getBeans(beanType.getType(), bindings));
        return (T) manager.getReference(bean, beanType.getType(), manager.createCreationalContext(bean));
    }

    public static Bean<?> ensureUniqueBean(Type type, Set<Bean<?>> beans) {
        if (beans.size() == 0) {
            throw new UnsatisfiedResolutionException("Unable to resolve any Web Beans of " + type);
        } else if (beans.size() > 1) {
            throw new AmbiguousResolutionException("More than one bean available for type " + type);
        }
        return beans.iterator().next();
    }

    @Deprecated
    public static Object getInstanceByName(BeanManager manager, String name) {
        Set<Bean<?>> beans = manager.getBeans(name);
        if (beans.size() == 0) {
            return null;
        } else if (beans.size() > 1) {
            throw new AmbiguousResolutionException("Resolved multiple Web Beans with " + name);
        } else {
            Bean<?> bean = beans.iterator().next();
            return manager.getReference(bean, bean.getTypes().iterator().next(), manager.createCreationalContext(bean));
        }
    }

}
