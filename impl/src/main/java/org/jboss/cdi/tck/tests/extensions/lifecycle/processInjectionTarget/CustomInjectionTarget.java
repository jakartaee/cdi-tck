/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processInjectionTarget;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import java.util.Set;

public class CustomInjectionTarget<T> implements InjectionTarget<T> {

    private InjectionTarget<T> wrappedInjectionTarget;
    private BeanManager beanManager;

    public CustomInjectionTarget(InjectionTarget<T> originalInjectionTarget, BeanManager beanManager) {
        this.wrappedInjectionTarget = originalInjectionTarget;
        this.beanManager = beanManager;
    }

    public void inject(T instance, CreationalContext<T> ctx) {
        beanManager.fireEvent(instance);
        wrappedInjectionTarget.inject(instance, ctx);
    }

    @Override
    public void postConstruct(T instance) {
        wrappedInjectionTarget.postConstruct(instance);
    }

    @Override
    public void preDestroy(T instance) {
        wrappedInjectionTarget.preDestroy(instance);
    }

    @Override
    public Object produce(CreationalContext ctx) {
        return wrappedInjectionTarget.produce(ctx);
    }

    @Override
    public void dispose(T instance) {
        wrappedInjectionTarget.dispose(instance);
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return wrappedInjectionTarget.getInjectionPoints();
    }

}
