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
package org.jboss.cdi.tck.tests.full.extensions.beanManager.bootstrap.unavailable.methods;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Vetoed;
import jakarta.enterprise.inject.spi.*;
import jakarta.enterprise.util.AnnotationLiteral;
import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.fail;

public class WrongExtension implements Extension {

    private Bean<Foo> fooBean;
    private InjectionPoint injectionPoint;

    public void observeBeforeBeanDiscovery(@Observes BeforeBeanDiscovery event, BeanManager beanManager) {
        testUnavailableMethodsBeforeABD(beanManager);
        testUnavailableMethodsBeforeADV(beanManager);
    }

    public void observeProcessAnnotatedType(@Observes ProcessAnnotatedType<?> event, BeanManager beanManager) {

        testUnavailableMethodsBeforeABD(beanManager);
        testUnavailableMethodsBeforeADV(beanManager);
    }

    public void observeAfterTypeDiscovery(@Observes AfterTypeDiscovery event, BeanManager beanManager) {
        testUnavailableMethodsBeforeABD(beanManager);
        testUnavailableMethodsBeforeADV(beanManager);
    }

    public void observeProcessInjectionTarget(@Observes ProcessInjectionTarget<Foo> event, BeanManager beanManager) {
        testUnavailableMethodsBeforeABD(beanManager);
        testUnavailableMethodsBeforeADV(beanManager);
    }

    public void observeInjectionPoint(@Observes ProcessInjectionPoint<Foo, ?> event, BeanManager beanManager) {
        if (injectionPoint == null) {
            // simply store some IP which we'll try to validate later
            injectionPoint = event.getInjectionPoint();
        }
        testUnavailableMethodsBeforeABD(beanManager);
        testUnavailableMethodsBeforeADV(beanManager);
    }

    public void observerProcessBeanAttributes(@Observes ProcessBeanAttributes<Foo> event, BeanManager beanManager) {
        testUnavailableMethodsBeforeABD(beanManager);
    }

    public void observeProcessBean(@Observes ProcessBean<Foo> event, BeanManager beanManager) {
        this.fooBean = event.getBean();
        testUnavailableMethodsBeforeABD(beanManager);
        testUnavailableMethodsBeforeADV(beanManager);
    }

    public void observerProcessObserverMethod(@Observes ProcessObserverMethod<Integer, Foo> event, BeanManager beanManager) {
        testUnavailableMethodsBeforeABD(beanManager);
        testUnavailableMethodsBeforeADV(beanManager);
    }

    public void observeAfterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager beanManager) {
        testAvailableMethodsInABD(beanManager);
        testUnavailableMethodsBeforeADV(beanManager);
    }

    public void observerAfterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager manager) {
        testAvailableMethodsInABD(manager);
        testAvailableMethodsInADV(manager);
    }

    @SuppressWarnings({ "serial", "unchecked" })
    private void testUnavailableMethodsBeforeABD(final BeanManager beanManager) {

        new Invocation() {
            void execute() {
                beanManager.getBeans("foo");
            }
        }.run();

        new Invocation() {
            void execute() {
                beanManager.getBeans(Foo.class);
            }
        }.run();

        new Invocation() {
            void execute() {
                beanManager.resolve(null);
            }
        }.run();

        new Invocation() {
            void execute() {
                beanManager.resolveObserverMethods(new Foo());
            }
        }.run();

        new Invocation() {
            void execute() {
                beanManager.resolveInterceptors(InterceptionType.AROUND_INVOKE, new Transactional.Literal());
            }
        }.run();

        new Invocation() {
            void execute() {
                beanManager.resolveDecorators(new HashSet<Type>(Arrays.asList(Foo.class)));
            }
        }.run();

        new Invocation() {
            void execute() {
                beanManager.validate(injectionPoint);
            }
        }.run();

        new Invocation() {
            void execute() {
                beanManager.getPassivationCapableBean("foo");
            }
        }.run();

    }

    @SuppressWarnings({ "serial", "unchecked" })
    private void testUnavailableMethodsBeforeADV(final BeanManager beanManager) {

        final CreationalContext<Foo> creationalContext = beanManager.createCreationalContext(fooBean);

        new Invocation() {
            void execute() {
                beanManager.getReference(fooBean, Foo.class, creationalContext);
            }
        }.run();

        new Invocation() {
            void execute() {
                beanManager.getInjectableReference(
                        beanManager.createInjectionPoint(beanManager.createAnnotatedType(Foo.class).getFields().iterator().next()),
                        creationalContext);
            }
        }.run();

        new Invocation() {
            void execute() {
                beanManager.createInstance().select(Foo.class);
            }
        }.run();
    }

    @SuppressWarnings({ "unchecked", "serial" })
    private void testAvailableMethodsInABD(BeanManager beanManager) {
        beanManager.getBeans("foo");
        beanManager.getBeans(Foo.class);
        beanManager.resolve(null);
        beanManager.resolveObserverMethods(new Foo());
        beanManager.resolveInterceptors(InterceptionType.AROUND_INVOKE, new Transactional.Literal());
        beanManager.resolveDecorators(new HashSet<Type>(Arrays.asList(Foo.class)));
        beanManager.validate(injectionPoint);
        beanManager.getPassivationCapableBean("foo");
    }

    private void testAvailableMethodsInADV(BeanManager beanManager) {
        beanManager.getReference(new FooBean(), Foo.class, beanManager.createCreationalContext(null));
        beanManager.getInjectableReference(
                beanManager.createInjectionPoint(beanManager.createAnnotatedType(Foo.class).getFields().iterator().next()),
                beanManager.createCreationalContext(null));
    }

    private static class FooBean implements Bean<Foo> {
        @Override
        public Class<?> getBeanClass() {
            return Foo.class;
        }

        @Override
        public Set<InjectionPoint> getInjectionPoints() {
            return Collections.emptySet();
        }

        @Override
        public Set<Type> getTypes() {
            HashSet<Type> set = new HashSet<Type>();
            set.add(Foo.class);
            return set;
        }

        @Override
        public Set<Annotation> getQualifiers() {
            return Collections.emptySet();
        }

        @Override
        public Class<? extends Annotation> getScope() {
            return Dependent.class;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public Set<Class<? extends Annotation>> getStereotypes() {
            return Collections.emptySet();
        }

        @Override
        public boolean isAlternative() {
            return false;
        }

        @Override
        public Foo create(CreationalContext<Foo> creationalContext) {
            return null;
        }

        @Override
        public void destroy(Foo instance, CreationalContext<Foo> creationalContext) {
        }
    }

    @Vetoed
    private static class FooInjectionPoint implements InjectionPoint {

        @Override
        public boolean isTransient() {
            return false;
        }

        @Override
        public boolean isDelegate() {
            return false;
        }

        @Override
        public Type getType() {
            return Foo.class;
        }

        @Override
        public Set<Annotation> getQualifiers() {
            return null;
        }

        @Override
        public Member getMember() {
            return null;
        }

        @Override
        public Bean<?> getBean() {
            return null;
        }

        @Override
        public Annotated getAnnotated() {
            return null;
        }
    }

    private static abstract class Invocation {
        void run() {
            try {
                execute();
                fail("Expected exception not thrown");
            } catch (IllegalStateException expected) {
            }
        }

        abstract void execute();
    }
}
