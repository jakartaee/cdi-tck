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
package org.jboss.cdi.tck.tests.lookup.injection.parameterized;

import static org.jboss.cdi.tck.cdi.Sections.ASSIGNABLE_PARAMETERS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.inject.Inject;

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
@SpecVersion(spec = "cdi", version = "2.0")
public class ParameterizedTypesInjectionToRawTypeTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(ParameterizedTypesInjectionToRawTypeTest.class)
                .withClasses(Dao.class, DaoProducer.class, NumberDao.class, StringDao.class, ConsumerRaw.class,
                        ConsumerRawObject.class, ObjectPowered.class)
                .build();
    }

    @Inject
    ConsumerRaw consumer;

    @Inject
    ConsumerRawObject consumerObject;

    @Test
    @SpecAssertion(section = ASSIGNABLE_PARAMETERS, id = "a")
    public void testInjection() {

        assertNotNull(consumer);
        assertNotNull(consumer.getDao());
        // Bean types of NumberDao are restricted
        // StringDao type parameters are actual types
        assertEquals(consumer.getDao().getId(), Dao.class.getName());

        assertNotNull(consumerObject);
        assertNotNull(consumerObject.getDao());
        assertEquals(consumerObject.getDao().getId(), DaoProducer.class.getName());
    }

}
