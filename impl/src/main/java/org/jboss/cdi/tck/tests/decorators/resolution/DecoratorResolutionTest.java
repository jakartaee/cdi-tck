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
package org.jboss.cdi.tck.tests.decorators.resolution;

import static org.jboss.cdi.tck.cdi.Sections.DELEGATE_ASSIGNABLE_PARAMETERS;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.inject.spi.Decorator;
import javax.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
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
public class DecoratorResolutionTest<T, C extends Cow, F extends FresianCow> extends AbstractTest {

    private final TypeLiteral<Bar<T>> BAR_TYPE_VARIABLE_LITERAL = new TypeLiteral<Bar<T>>() {
    };
    private final TypeLiteral<Baz<T>> BAZ_TYPE_VARIABLE_LITERAL = new TypeLiteral<Baz<T>>() {
    };
    private final TypeLiteral<Foo<T>> FOO_TYPE_VARIABLE_LITERAL = new TypeLiteral<Foo<T>>() {
    };
    private final TypeLiteral<Foo<Object>> FOO_OBJECT_LITERAL = new TypeLiteral<Foo<Object>>() {
    };
    private final TypeLiteral<Qux<String>> QUX_STRING_LITERAL = new TypeLiteral<Qux<String>>() {
    };
    private final TypeLiteral<Qux<List<String>>> QUX_STRING_LIST_LITERAL = new TypeLiteral<Qux<List<String>>>() {
    };
    private final TypeLiteral<Grault<Integer>> GRAULT_INTEGER_LITERAL = new TypeLiteral<Grault<Integer>>() {
    };
    private final TypeLiteral<Corge<C, C>> CORGE_TYPE_VARIABLE_EXTENDS_COW_LITERAL = new TypeLiteral<Corge<C, C>>() {
    };
    private final TypeLiteral<Garply<F>> GARPLY_EXTENDS_FRESIAN_COW_LITERAL = new TypeLiteral<Garply<F>>() {
    };
    private final TypeLiteral<Garply<Cow>> GARPLY_COW_LITERAL = new TypeLiteral<Garply<Cow>>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DecoratorResolutionTest.class).withBeansXml("beans.xml").build();
    }

    private static boolean decoratorCollectionMatches(Collection<Decorator<?>> decorators, Class<?>... types) {
        Set<Class<?>> typeSet = new HashSet<Class<?>>(Arrays.asList(types));
        for (Decorator<?> decorator : decorators) {
            typeSet.remove(decorator.getBeanClass());
        }
        return typeSet.isEmpty();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DELEGATE_ASSIGNABLE_PARAMETERS, id = "aa") })
    public void testUnboundedTypeVariables() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(
                Collections.singleton(BAR_TYPE_VARIABLE_LITERAL.getType()));
        assert decoratorCollectionMatches(decorators, BarDecorator.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DELEGATE_ASSIGNABLE_PARAMETERS, id = "ab") })
    public void testObject() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(
                Collections.singleton(BAZ_TYPE_VARIABLE_LITERAL.getType()));
        assert decoratorCollectionMatches(decorators, BazDecorator.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DELEGATE_ASSIGNABLE_PARAMETERS, id = "ac") })
    public void testUnboundedTypeVariablesAndObject() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(
                Collections.singleton(FOO_TYPE_VARIABLE_LITERAL.getType()));
        assert decoratorCollectionMatches(decorators, FooDecorator.class, FooObjectDecorator.class);
        List<Decorator<?>> decorators1 = getCurrentManager().resolveDecorators(
                Collections.singleton(FOO_OBJECT_LITERAL.getType()));
        assert decoratorCollectionMatches(decorators1, FooDecorator.class, FooObjectDecorator.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DELEGATE_ASSIGNABLE_PARAMETERS, id = "c") })
    public void testIdenticalTypeParamerters() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(
                Collections.singleton(QUX_STRING_LITERAL.getType()));
        assert decoratorCollectionMatches(decorators, QuxDecorator.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DELEGATE_ASSIGNABLE_PARAMETERS, id = "d") })
    public void testNestedIdenticalTypeParamerters() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(
                Collections.singleton(QUX_STRING_LIST_LITERAL.getType()));
        assert decoratorCollectionMatches(decorators, QuxListDecorator.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DELEGATE_ASSIGNABLE_PARAMETERS, id = "e") })
    public void testDelegateWildcardBeanActualType() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(
                Collections.singleton(GRAULT_INTEGER_LITERAL.getType()));
        assert decoratorCollectionMatches(decorators, GraultExtendsDecorator.class, GraultSuperDecorator.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DELEGATE_ASSIGNABLE_PARAMETERS, id = "f") })
    public void testDelegateWildcardBeanTypeVariable() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(
                Collections.singleton(CORGE_TYPE_VARIABLE_EXTENDS_COW_LITERAL.getType()));
        assert decoratorCollectionMatches(decorators, CorgeDecorator.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DELEGATE_ASSIGNABLE_PARAMETERS, id = "g") })
    public void testDelegateTypeVariableBeanTypeVariable() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(
                Collections.singleton(GARPLY_EXTENDS_FRESIAN_COW_LITERAL.getType()));
        assert decoratorCollectionMatches(decorators, GarplyDecorator.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DELEGATE_ASSIGNABLE_PARAMETERS, id = "h") })
    public void testDelegateTypeVariableBeanActualType() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(
                Collections.singleton(GARPLY_COW_LITERAL.getType()));
        assert decoratorCollectionMatches(decorators, GarplyDecorator.class);
    }

}
