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
package org.jboss.cdi.tck.tests.full.extensions.configurators.bean.alternativePriority;

import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_CONFIGURATOR;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@SpecVersion(spec = "cdi", version = "4.0")
public class BeanConfiguratorAlternativePriorityTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(BeanConfiguratorAlternativePriorityTest.class)
                .withExtension(AlternativePriorityExtension.class)
                .build();
    }

    @Inject
    Foo foo;

    @Inject
    Bar bar;

    @Test(groups = CDI_FULL, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = BEAN_CONFIGURATOR, id = "h")
    public void testSyntheticAlternativeIsEnabled(AlternativePriorityExtension extension) {
        assertTrue(extension.isSyntheticAlternativeProcessed());

        assertEquals(foo.ping(), "bar");
        assertEquals(bar.ping(), "bar");
    }
}
