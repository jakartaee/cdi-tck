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
package org.jboss.jsr299.tck.tests.decorators.definition;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;

import javax.decorator.Delegate;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Decorator;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author pmuir
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class DecoratorDefinitionTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DecoratorDefinitionTest.class).withBeansXml("beans.xml").build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "8.1", id = "d"), @SpecAssertion(section = "8.1.1", id = "a"),
            @SpecAssertion(section = "8.1.3", id = "c"), @SpecAssertion(section = "8.3", id = "aa"),
            @SpecAssertion(section = "11.1.1", id = "a"), @SpecAssertion(section = "12.3", id = "kc") })
    public void testDecoratorIsManagedBean() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(MockLogger.TYPES);
        assert decorators.size() == 1;
        boolean implementsInterface = false;
        for (Class<?> interfaze : decorators.get(0).getClass().getInterfaces()) {
            if (Decorator.class.isAssignableFrom(interfaze)) {
                implementsInterface = true;
                break;
            }
        }
        assert implementsInterface;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "8.1", id = "b"), @SpecAssertion(section = "8.1", id = "c"),
            @SpecAssertion(section = "11.1.1", id = "b"), @SpecAssertion(section = "11.3.11", id = "a"),
            @SpecAssertion(section = "11.3.11", id = "b") })
    public void testDecoratedTypes() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(FooBar.TYPES);
        assert decorators.size() == 1;
        assert decorators.get(0).getDecoratedTypes().size() == 4;
        assert decorators.get(0).getDecoratedTypes().contains(Foo.class);
        assert decorators.get(0).getDecoratedTypes().contains(Bar.class);
        assert decorators.get(0).getDecoratedTypes().contains(Baz.class);
        assert decorators.get(0).getDecoratedTypes().contains(Boo.class);
        assert !decorators.get(0).getDecoratedTypes().contains(Serializable.class);
        assert !decorators.get(0).getDecoratedTypes().contains(FooDecorator.class);
        assert !decorators.get(0).getDecoratedTypes().contains(AbstractFooDecorator.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "8.1.2", id = "a"), @SpecAssertion(section = "11.1.1", id = "c") })
    public void testDelegateInjectionPoint() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(Logger.TYPES);
        assert decorators.size() == 1;
        Decorator<?> decorator = decorators.get(0);
        assert decorator.getInjectionPoints().size() == 1;
        assert decorator.getInjectionPoints().iterator().next().getType().equals(Logger.class);
        assert decorator.getInjectionPoints().iterator().next().getAnnotated().isAnnotationPresent(Delegate.class);
        assert decorator.getDelegateType().equals(Logger.class);
        assert decorator.getDelegateQualifiers().size() == 1;
        assert annotationSetMatches(decorator.getDelegateQualifiers(), Default.class);
    }

    @Test
    @SpecAssertion(section = "8.1.3", id = "b")
    public void testDecoratorDoesNotImplementDelegateType() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(Bazt.TYPES);
        assert decorators.size() == 2;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "8.2", id = "b"), @SpecAssertion(section = "8.3", id = "aa"),
            @SpecAssertion(section = "11.3.11", id = "a") })
    public void testDecoratorOrdering() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(Bazt.TYPES);
        assert decorators.size() == 2;
        assert decorators.get(0).getTypes().contains(BazDecorator.class);
        assert decorators.get(1).getTypes().contains(BazDecorator1.class);
    }

    @Test
    @SpecAssertion(section = "8.2", id = "a")
    public void testNonEnabledDecoratorNotResolved() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(Field.TYPES);
        assert decorators.size() == 0;
    }

    @Test
    @SpecAssertion(section = "11.1.1", id = "d")
    public void testInstanceOfDecoratorForEachEnabled() {
        assert !getCurrentManager().resolveDecorators(MockLogger.TYPES).isEmpty();
        assert !getCurrentManager().resolveDecorators(FooBar.TYPES).isEmpty();
        assert !getCurrentManager().resolveDecorators(Logger.TYPES).isEmpty();
        assert getCurrentManager().resolveDecorators(Bazt.TYPES).size() == 2;
        assert getCurrentManager().resolveDecorators(Field.TYPES).isEmpty();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = "11.3.11", id = "c")
    public void testDuplicateBindingsOnResolveDecoratorsFails() {
        Annotation binding = new AnnotationLiteral<Meta>() {
        };
        getCurrentManager().resolveDecorators(FooBar.TYPES, binding, binding);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = "11.3.11", id = "d")
    public void testNonBindingsOnResolveDecoratorsFails() {
        Annotation binding = new AnnotationLiteral<NonMeta>() {
        };
        getCurrentManager().resolveDecorators(FooBar.TYPES, binding);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = "11.3.11", id = "e")
    public void testEmptyTypeSetOnResolveDecoratorsFails() {
        Annotation binding = new AnnotationLiteral<NonMeta>() {
        };
        getCurrentManager().resolveDecorators(new HashSet<Type>(), binding);
    }
}
