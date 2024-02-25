/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.full.decorators.builtin.event;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_INVOCATION;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_RESOLUTION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Collections;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.util.TypeLiteral;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.full.decorators.AbstractDecoratorTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 *
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class BuiltinEventDecoratorTest extends AbstractDecoratorTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(BuiltinEventDecoratorTest.class)
                .withClass(AbstractDecoratorTest.class)
                .withBeansXml(
                        new BeansXml().decorators(FooEventDecorator.class, StringEventDecorator.class,
                                CharSequenceEventDecorator.class))
                .build();
    }

    @Inject
    Observer observer;

    @Inject
    Event<Foo> fooEvent;

    @Inject
    private Event<String> stringEvent;

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_INVOCATION, id = "aca"),
            @SpecAssertion(section = DECORATOR_RESOLUTION, id = "aa") })
    public void testDecoratorIsResolved() {
        @SuppressWarnings("serial")
        TypeLiteral<Event<Foo>> eventFooLiteral = new TypeLiteral<Event<Foo>>() {
        };
        checkDecorator(resolveUniqueDecorator(Collections.singleton(eventFooLiteral.getType())), FooEventDecorator.class,
                Collections.<Type> singleton(eventFooLiteral.getType()), eventFooLiteral.getType());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_INVOCATION, id = "aca") })
    public void testDecoratorIsInvoked() {
        Foo payload = new Foo(false);
        fooEvent.fire(payload);
        assertTrue(observer.isObserved());
        assertTrue(payload.isDecorated());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_INVOCATION, id = "aca") })
    public void testMultipleDecorators() {
        stringEvent.fire("TCK");
        assertEquals(observer.getString(), "DecoratedCharSequenceDecoratedStringTCK");
        try {
            stringEvent.select();
            Assert.fail("Exception not thrown");
        } catch (Exception expected) {
        }
    }

}
