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
package org.jboss.cdi.tck.tests.lookup.typesafe.resolution.parameterized.raw;

import static org.jboss.cdi.tck.cdi.Sections.ASSIGNABLE_PARAMETERS;
import static org.testng.Assert.assertEquals;

import jakarta.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Martin Kouba
 */
@SuppressWarnings("serial")
@SpecVersion(spec = "cdi", version = "2.0")
public class RawBeanTypeParameterizedRequiredTypeTest<T, X extends Number> extends AbstractTest {

    private final TypeLiteral<Foo<T>> FOO_UNBOUNDED_TYPE_VARIABLE_LITERAL = new TypeLiteral<Foo<T>>() {
    };

    private final TypeLiteral<Foo<X>> FOO_BOUNDED_TYPE_VARIABLE_LITERAL = new TypeLiteral<Foo<X>>() {
    };

    private final TypeLiteral<Foo<Object>> FOO_OBJECT_LITERAL = new TypeLiteral<Foo<Object>>() {
    };

    private final TypeLiteral<Foo<Integer>> FOO_INTEGER_LITERAL = new TypeLiteral<Foo<Integer>>() {
    };

    private final TypeLiteral<Bar<String, T>> BAR_STRING_UNBOUNDED_TYPE_VARIABLE_LITERAL = new TypeLiteral<Bar<String, T>>() {
    };

    private final TypeLiteral<Bar<String, X>> BAR_STRING_BOUNDED_TYPE_VARIABLE_LITERAL = new TypeLiteral<Bar<String, X>>() {
    };

    private final TypeLiteral<Bar<Object, X>> BAR_OBJECT_BOUNDED_TYPE_VARIABLE_LITERAL = new TypeLiteral<Bar<Object, X>>() {
    };

    private final TypeLiteral<Bar<Object, Integer>> BAR_OBJECT_STRING_LITERAL = new TypeLiteral<Bar<Object, Integer>>() {
    };

    private final TypeLiteral<Bar<Object, Object>> BAR_OBJECT_LITERAL = new TypeLiteral<Bar<Object, Object>>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(RawBeanTypeParameterizedRequiredTypeTest.class).build();
    }

    @Test
    @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "g")
    public void testNotAssignableTypeParams() {
        assertEquals(getBeans(FOO_INTEGER_LITERAL).size(), 0);
        assertEquals(getBeans(FOO_BOUNDED_TYPE_VARIABLE_LITERAL).size(), 0);
        assertEquals(getBeans(BAR_STRING_UNBOUNDED_TYPE_VARIABLE_LITERAL).size(), 0);
        assertEquals(getBeans(BAR_STRING_BOUNDED_TYPE_VARIABLE_LITERAL).size(), 0);
        assertEquals(getBeans(BAR_OBJECT_BOUNDED_TYPE_VARIABLE_LITERAL).size(), 0);
        assertEquals(getBeans(BAR_OBJECT_STRING_LITERAL).size(), 0);
    }

    @Test
    @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "g")
    public void testAssignableTypeParams() {
        assertEquals(getBeans(FOO_UNBOUNDED_TYPE_VARIABLE_LITERAL).size(), 1);
        assertEquals(getBeans(FOO_OBJECT_LITERAL).size(), 1);
        assertEquals(getBeans(BAR_OBJECT_LITERAL).size(), 1);
    }

}
