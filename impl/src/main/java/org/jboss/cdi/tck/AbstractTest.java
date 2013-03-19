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
package org.jboss.cdi.tck;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.AmbiguousResolutionException;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;

import org.jboss.arquillian.testng.Arquillian;
import org.jboss.cdi.tck.api.Configuration;
import org.jboss.cdi.tck.impl.ConfigurationFactory;
import org.jboss.cdi.tck.util.BeanLookupUtils;
import org.jboss.cdi.tck.util.DependentInstance;

/**
 * Abstract CDI TCK test.
 */
public abstract class AbstractTest extends Arquillian {

    @Inject
    protected BeanManager beanManager;

    /**
     * Note that TCK uses Arquillian Servlet protocol and so the test class is instantiated/enriched from within the WAR module.
     * This affects most of the {@link BeanManager} methods functionality. For instance the method
     * {@link BeanManager#getBeans(Type, Annotation...)} returns the set of beans which are available for injection in the
     * module or library containing the class into which the {@link BeanManager} was injected (simplified).
     *
     * @return the current {@link BeanManager}
     */
    protected BeanManager getCurrentManager() {
        return beanManager;
    }

    protected byte[] passivate(Object instance) throws IOException {
        return getCurrentConfiguration().getBeans().passivate(instance);
    }

    protected Object activate(byte[] bytes) throws IOException, ClassNotFoundException {
        return getCurrentConfiguration().getBeans().activate(bytes);
    }

    protected void setContextActive(Context context) {
        getCurrentConfiguration().getContexts().setActive(context);
    }

    protected void setContextInactive(Context context) {
        getCurrentConfiguration().getContexts().setInactive(context);
    }

    protected void destroyContext(Context context) {
        getCurrentConfiguration().getContexts().destroyContext(context);
    }

    protected Configuration getCurrentConfiguration() {
        return ConfigurationFactory.get();
    }

    /**
     * Checks if all annotations are in a given set of annotations
     *
     * @param annotations The annotation set
     * @param requiredAnnotationTypes The annotations to match
     * @return True if match, false otherwise
     */
    public boolean annotationSetMatches(Set<? extends Annotation> annotations,
            Class<? extends Annotation>... requiredAnnotationTypes) {
        List<Class<? extends Annotation>> annotationTypeList = new ArrayList<Class<? extends Annotation>>();
        annotationTypeList.addAll(Arrays.asList(requiredAnnotationTypes));
        for (Annotation annotation : annotations) {
            if (annotationTypeList.contains(annotation.annotationType())) {
                annotationTypeList.remove(annotation.annotationType());
            } else {
                return false;
            }
        }
        return annotationTypeList.size() == 0;
    }

    /**
     * @param annotations The annotation set
     * @param requiredAnnotationTypes The required annotations
     * @return <code>true</code> if speficied set matches required annotations, <code>false</code> otherwise
     */
    public boolean annotationSetMatches(Set<? extends Annotation> annotations, Annotation... requiredAnnotations) {
        List<Annotation> requiredAnnotationList = new ArrayList<Annotation>();
        return requiredAnnotations.length == annotations.size() && annotations.containsAll(requiredAnnotationList);
    }

    public boolean rawTypeSetMatches(Set<Type> types, Class<?>... requiredTypes) {
        List<Class<?>> typeList = new ArrayList<Class<?>>();
        typeList.addAll(Arrays.asList(requiredTypes));
        for (Type type : types) {
            if (type instanceof Class<?>) {
                typeList.remove(type);
            } else if (type instanceof ParameterizedType) {
                typeList.remove(((ParameterizedType) type).getRawType());
            }
        }
        return typeList.size() == 0;
    }

    public boolean typeSetMatches(Collection<? extends Type> types, Type... requiredTypes) {
        List<Type> typeList = Arrays.asList(requiredTypes);
        return requiredTypes.length == types.size() && types.containsAll(typeList);
    }

    public <T> Bean<T> getUniqueBean(Class<T> type, Annotation... bindings) {
        Set<Bean<T>> beans = getBeans(type, bindings);
        return resolveUniqueBean(type, beans);
    }

    public <T> Bean<T> getUniqueBean(TypeLiteral<T> type, Annotation... bindings) {
        Set<Bean<T>> beans = getBeans(type, bindings);
        return resolveUniqueBean(type.getType(), beans);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> Set<Bean<T>> getBeans(Class<T> type, Annotation... bindings) {
        return (Set) getCurrentManager().getBeans(type, bindings);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> Set<Bean<T>> getBeans(TypeLiteral<T> type, Annotation... bindings) {
        return (Set) getCurrentManager().getBeans(type.getType(), bindings);
    }

    public <T> T getContextualReference(Class<T> beanType, Annotation... qualifiers) {
        return BeanLookupUtils.getContextualReference(getCurrentManager(), beanType, qualifiers);
    }

    public <T> T getContextualReference(TypeLiteral<T> beanType, Annotation... qualifiers) {
        return BeanLookupUtils.getContextualReference(getCurrentManager(), beanType, qualifiers);
    }

    public <T> T getContextualReference(String name, Class<T> beanType) {
        return BeanLookupUtils.getContextualReference(getCurrentManager(), name, beanType);
    }

    public <T> DependentInstance<T> newDependentInstance(Class<T> beanType, Annotation... qualifiers) {
        return new DependentInstance<T>(getCurrentManager(), beanType, qualifiers);
    }

    private <T> Bean<T> resolveUniqueBean(Type type, Set<Bean<T>> beans) {
        if (beans.size() == 0) {
            throw new UnsatisfiedResolutionException("Unable to resolve any beans of " + type);
        } else if (beans.size() > 1) {
            throw new AmbiguousResolutionException("More than one bean available (" + beans + ")");
        }
        return beans.iterator().next();
    }

    /**
     * Extracted from test harness. We need this since testng expected exceptions feature is not working with exception cause -
     * see org.testng.internal.Invoker.isExpectedException(Throwable, ExpectedExceptionsHolder).
     *
     * @param throwableType
     * @param throwable
     * @return <code>true</code> if throwable type is assignable from specified throwable or any cause in stack (works
     *         recursively), <code>false</code> otherwise
     */
    protected boolean isThrowablePresent(Class<? extends Throwable> throwableType, Throwable throwable) {
        if (throwable == null) {
            return false;
        } else if (throwableType.isAssignableFrom(throwable.getClass())) {
            return true;
        } else {
            return isThrowablePresent(throwableType, throwable.getCause());
        }
    }

}
