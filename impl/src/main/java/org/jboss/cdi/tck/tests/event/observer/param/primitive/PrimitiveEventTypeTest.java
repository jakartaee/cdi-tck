/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.event.observer.param.primitive;

import static org.jboss.cdi.tck.cdi.Sections.OBSERVERS_PRIMITIVE_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_METHOD_EVENT_PARAMETER;
import static org.testng.Assert.assertEquals;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class PrimitiveEventTypeTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(PrimitiveEventTypeTest.class).build();
    }

    @Inject
    SimpleProducer producer;

    @Test
    @SpecAssertion(section = OBSERVERS_PRIMITIVE_TYPES, id = "a")
    @SpecAssertion(section = OBSERVER_METHOD_EVENT_PARAMETER, id = "e")
    public void test() {
        assertEquals(SimpleConsumer.intCounter, 0);
        assertEquals(SimpleConsumer.longCounter, 0);

        producer.produceInt();

        assertEquals(SimpleConsumer.intCounter, 1);
        assertEquals(SimpleConsumer.longCounter, 0);

        producer.produceInt();

        assertEquals(SimpleConsumer.intCounter, 2);
        assertEquals(SimpleConsumer.longCounter, 0);

        producer.produceLong();

        assertEquals(SimpleConsumer.intCounter, 2);
        assertEquals(SimpleConsumer.longCounter, 1);
    }
}
