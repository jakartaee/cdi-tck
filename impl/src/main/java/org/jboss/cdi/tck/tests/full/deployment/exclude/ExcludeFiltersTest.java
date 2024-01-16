/*
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.deployment.exclude;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.SYSTEM_PROPERTIES;
import static org.jboss.cdi.tck.cdi.Sections.EXCLUDE_FILTERS;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.TestSystemProperty;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.full.deployment.exclude.food.Meat;
import org.jboss.cdi.tck.tests.full.deployment.exclude.haircut.Chonmage;
import org.jboss.cdi.tck.tests.full.deployment.exclude.mustache.Mustache;
import org.jboss.cdi.tck.tests.full.deployment.exclude.mustache.beard.Beard;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class ExcludeFiltersTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(ExcludeFiltersTest.class)
                .withPackage(Mustache.class.getPackage())
                .withPackage(Beard.class.getPackage())
                .withPackage(Chonmage.class.getPackage())
                .withPackage(Meat.class.getPackage())
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL).excludeFilters(
                        BeansXml.Exclude.match(Chonmage.class.getPackage().getName() + ".*"),
                        BeansXml.Exclude.match(Mustache.class.getPackage().getName() + ".**"),
                        BeansXml.Exclude.match(Meat.class.getPackage().getName() + ".*").ifClassAvailable("com.some.unreal.class.Name"),
                        BeansXml.Exclude.match(Meat.class.getPackage().getName() + ".*").ifClassNotAvailable(ExcludeFiltersTest.class.getName()),
                        BeansXml.Exclude.match(Alpha.class.getName()).ifClassAvailable(Stubble.class.getName()),
                        BeansXml.Exclude.match(Stubble.class.getName()),
                        BeansXml.Exclude.match(Foxtrot.class.getName()).ifClassAvailable("com.some.unreal.class.Name"),
                        BeansXml.Exclude.match(Bravo.class.getName()).ifClassNotAvailable("com.some.unreal.class.Name"),
                        BeansXml.Exclude.match(Echo.class.getName()).ifClassNotAvailable(ExcludeFiltersTest.class.getName()),
                        BeansXml.Exclude.match(Charlie.class.getName()).ifSystemProperty(TestSystemProperty.EXCLUDE_DUMMY.getKey()),
                        BeansXml.Exclude.match(Delta.class.getName()).ifSystemProperty(TestSystemProperty.EXCLUDE_DUMMY.getKey(), TestSystemProperty.EXCLUDE_DUMMY.getValue())
                ))
                .withExtension(VerifyingExtension.class).build();
    }

    @Inject
    VerifyingExtension extension;

    @Test
    @SpecAssertions({ @SpecAssertion(section = EXCLUDE_FILTERS, id = "a"), @SpecAssertion(section = EXCLUDE_FILTERS, id = "f") })
    public void testTypeFcqnMatchesExcludeFilterName() {
        assertTypeIsExcluded(Stubble.class);
        assertTypeIsNotExcluded(Golf.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = EXCLUDE_FILTERS, id = "a"), @SpecAssertion(section = EXCLUDE_FILTERS, id = "g"),
            @SpecAssertion(section = EXCLUDE_FILTERS, id = "h") })
    public void testTypePackageMatchesExcludeFilterName() {
        assertTypeIsExcluded(Mustache.class);
        assertTypeIsExcluded(Beard.class);
        assertTypeIsExcluded(Chonmage.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = EXCLUDE_FILTERS, id = "b"), @SpecAssertion(section = EXCLUDE_FILTERS, id = "c") })
    public void testExcludeClassActivators() {
        assertTypeIsExcluded(Alpha.class);
        assertTypeIsNotExcluded(Foxtrot.class);
        assertTypeIsExcluded(Bravo.class);
        assertTypeIsNotExcluded(Echo.class);
        assertTypeIsNotExcluded(Meat.class);
    }

    @Test(groups = { INTEGRATION, SYSTEM_PROPERTIES, CDI_FULL })
    @SpecAssertions({ @SpecAssertion(section = EXCLUDE_FILTERS, id = "d"), @SpecAssertion(section = EXCLUDE_FILTERS, id = "e") })
    public void testExcludeSystemPropertyActivator() {
        assertTypeIsExcluded(Charlie.class);
        assertTypeIsExcluded(Delta.class);
    }

    private void assertTypeIsExcluded(Class<?> type) {
        assertTrue(getBeans(type).isEmpty());
        assertFalse(extension.getObservedAnnotatedTypes().contains(type));
    }

    private void assertTypeIsNotExcluded(Class<?> type) {
        assertFalse(getBeans(type).isEmpty());
        assertTrue(extension.getObservedAnnotatedTypes().contains(type));
    }

}
