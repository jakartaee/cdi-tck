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

package org.jboss.cdi.tck.tests.implementation.producer.method.definition.name;

import static org.jboss.cdi.tck.cdi.Sections.NAMED_STEREOTYPE;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_METHOD_NAME;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@SuppressWarnings("serial")
@SpecVersion(spec = "cdi", version = "2.0")
public class ProducerMethodWithDefaultNameTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProducerMethodWithDefaultNameTest.class).build();
    }

    @Test
    @SpecAssertion(section = PRODUCER_METHOD_NAME, id = "a")
    public void testMethodName() {
        String name = "findTerry";
        Bean<Bug> terry = getUniqueBean(Bug.class, new Crazy.Literal());
        assertEquals(terry.getName(), name);
    }

    @Test
    @SpecAssertion(section = PRODUCER_METHOD_NAME, id = "b")
    public void testJavaBeansPropertyName() {
        String name = "graham";
        Bean<Bug> graham = getUniqueBean(Bug.class);
        assertEquals(graham.getName(), name);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = NAMED_STEREOTYPE, id = "aa"),
            @SpecAssertion(section = NAMED_STEREOTYPE, id = "ab") })
    public void testProducerMethodQualifiers() {
        String name = "produceJohn";
        Bean<Bug> john = getUniqueBean(Bug.class, new Funny.Literal());
        assertEquals(john.getName(), name);
        assertTrue(annotationSetMatches(john.getQualifiers(), Any.Literal.INSTANCE, Default.Literal.INSTANCE));
    }

}
