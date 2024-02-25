/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.build.compatible.extensions.changeObserverQualifier;

import static org.testng.Assert.assertEquals;

import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "4.0")
public class ChangeObserverQualifierTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        // no beans.xml + an extension = not a bean archive, bean classes are added through the extension
        return new WebArchiveBuilder()
                .withTestClassPackage(ChangeObserverQualifierTest.class)
                .withBuildCompatibleExtension(ChangeObserverQualifierExtension.class)
                .withoutBeansXml()
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.ENHANCEMENT_PHASE, id = "b", note = "MyConsumer.noConsume not invoked")
    public void test() {
        MyProducer producer = getContextualReference(MyProducer.class);
        producer.produce();

        assertEquals(MyConsumer.events, Set.of("qualified"));
    }

    @Test
    public void testScopeIsRetained() {
        {
            Set<Bean<MyProducer>> beans = getBeans(MyProducer.class);
            assertEquals(beans.size(), 1);
            assertEquals(ApplicationScoped.class, beans.iterator().next().getScope());
        }

        {
            Set<Bean<MyConsumer>> beans = getBeans(MyConsumer.class);
            assertEquals(beans.size(), 1);
            assertEquals(ApplicationScoped.class, beans.iterator().next().getScope());
        }
    }
}
