package org.jboss.cdi.tck.util;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

/**
 * Simple helper class for creating and destroying dependent contextual instances.
 *
 * @author Martin Kouba
 * @param <T>
 */
public class DependentInstance<T> {

    private final Bean<T> bean;
    private final CreationalContext<T> creationalContext;
    private final T instance;
    private boolean destroyed = false;

    @SuppressWarnings("unchecked")
    public DependentInstance(BeanManager beanManager, Class<T> beanType, Annotation... qualifiers) {

        Set<Bean<?>> beans = beanManager.getBeans(beanType, qualifiers);

        if (beans == null || beans.isEmpty()) {
            throw new UnsatisfiedResolutionException(String.format(
                    "No bean matches required type %s and required qualifiers %s", beanType, Arrays.toString(qualifiers)));
        }

        bean = (Bean<T>) beanManager.resolve(beans);

        if (!bean.getScope().equals(Dependent.class)) {
            throw new IllegalStateException("Bean is not dependent");
        }

        creationalContext = beanManager.createCreationalContext(bean);
        instance = bean.create(creationalContext);
    }

    /**
     *
     * @return the created instance
     */
    public T get() {
        checkDestroyed();
        return instance;
    }

    /**
     * Destroy the created instance properly.
     *
     * @return self
     */
    public DependentInstance<T> destroy() {
        checkDestroyed();
        bean.destroy(instance, creationalContext);
        destroyed = true;
        return this;
    }

    /**
     * @return <code>true</code> if destroyed, <code>false</code> otherwise
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    private void checkDestroyed() {
        if (destroyed) {
            throw new IllegalStateException("Instance already destroyed");
        }
    }

}