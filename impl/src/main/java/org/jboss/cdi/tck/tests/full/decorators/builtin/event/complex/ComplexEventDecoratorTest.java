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
package org.jboss.cdi.tck.tests.full.decorators.builtin.event.complex;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_INVOCATION;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test that uses a more complex scenario to verify that Decorators can be used to alter event delivery logic.
 *
 * @author Jozef Hartinger
 *
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ComplexEventDecoratorTest extends AbstractTest {

    @Inject
    private Event<Payload> event;

    @Inject
    private Observers observers;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(ComplexEventDecoratorTest.class)
                .withExtension(OrderedEventDeliveryExtension.class)
                .withBeansXml(
                        new BeansXml().decorators(OrderedEventDeliveryDecorator.class))
                .build();
    }

    @Test(groups = CDI_FULL)
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_INVOCATION, id = "aca") })
    public void testOrderedEvents() {
        event.select(Bar.Literal.INSTANCE).select(Baz.Literal.INSTANCE).fire(new Payload());

        List<String> expectedResult = new ArrayList<String>();
        expectedResult.add("first");
        expectedResult.add("second");
        expectedResult.add("third");

        List<String> result = observers.getSequence().getData();
        Assert.assertEquals(result, expectedResult);
    }
}
