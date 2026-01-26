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
package org.jboss.cdi.tck;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.inject.AmbiguousResolutionException;
import jakarta.enterprise.inject.UnsatisfiedResolutionException;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanContainer;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.el.ELAwareBeanManager;
import jakarta.enterprise.util.TypeLiteral;
import jakarta.inject.Inject;

import org.jboss.arquillian.testng.Arquillian;
import org.jboss.cdi.tck.api.Configuration;
import org.jboss.cdi.tck.impl.ConfigurationFactory;
import org.jboss.cdi.tck.spi.Contextuals;
import org.jboss.cdi.tck.spi.CreationalContexts;
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

    /**
     * Note that TCK uses Arquillian Servlet protocol and so the test class is instantiated/enriched from within the WAR module.
     * This affects most of the {@link BeanContainer} methods functionality. For instance the method
     * {@link BeanContainer#getBeans(Type, Annotation...)} returns the set of beans which are available for injection in the
     * module or library containing the class into which the {@link BeanContainer} was injected (simplified).
     *
     * @return the current {@link BeanContainer}
     */
    protected BeanContainer getCurrentBeanContainer() {
        return beanManager;
    }

    protected ELAwareBeanManager getCurrentELAwareManager() {
        if (beanManager instanceof ELAwareBeanManager bm) {
            return bm;
        }
        throw new IllegalStateException("Current BeanManager is not ELAwareBeanManager");
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

    protected <T> CreationalContexts.Inspectable<T> createInspectableCreationalContext(Contextual<T> contextual) {
        return getCurrentConfiguration().getCreationalContexts().create(contextual);
    }

    protected <T> Contextuals.Inspectable<T> createInspectableContextual(T instance, Context context) {
        return getCurrentConfiguration().getContextuals().create(instance, context);
    }

    protected Configuration getCurrentConfiguration() {
        return ConfigurationFactory.get();
    }

    protected <T> Bean<T> getUniqueBean(Class<T> type, Annotation... bindings) {
        Set<Bean<T>> beans = getBeans(type, bindings);
        return resolveUniqueBean(type, beans);
    }

    protected <T> Bean<T> getUniqueBean(TypeLiteral<T> type, Annotation... bindings) {
        Set<Bean<T>> beans = getBeans(type, bindings);
        return resolveUniqueBean(type.getType(), beans);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <T> Set<Bean<T>> getBeans(Class<T> type, Annotation... bindings) {
        return (Set) getCurrentManager().getBeans(type, bindings);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <T> Set<Bean<T>> getBeans(TypeLiteral<T> type, Annotation... bindings) {
        return (Set) getCurrentManager().getBeans(type.getType(), bindings);
    }

    protected <T> T getContextualReference(Class<T> beanType, Annotation... qualifiers) {
        return BeanLookupUtils.<T> getContextualReference(getCurrentManager(), beanType, qualifiers);
    }

    protected <T> T getContextualReference(TypeLiteral<T> beanType, Annotation... qualifiers) {
        return BeanLookupUtils.<T> getContextualReference(getCurrentManager(), beanType, qualifiers);
    }

    protected <T> T getContextualReference(String name, Class<T> beanType) {
        return BeanLookupUtils.<T> getContextualReference(getCurrentManager(), name, beanType);
    }

    protected <T> DependentInstance<T> newDependentInstance(Class<T> beanType, Annotation... qualifiers) {
        return new DependentInstance<T>(getCurrentManager(), beanType, qualifiers);
    }

    private <T> Bean<T> resolveUniqueBean(Type type, Set<Bean<T>> beans) {
        if (beans.isEmpty()) {
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
