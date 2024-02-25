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

package org.jboss.cdi.tck.tests.full.alternative.selection.stereotype;

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE;
import static org.testng.Assert.assertEquals;

import java.util.Set;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.TestGroups;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = TestGroups.CDI_FULL)
public class SelectedBeanWithUnselectedStereotypeTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SelectedBeanWithUnselectedStereotypeTest.class)
                .withBeansXml(new BeansXml().alternatives(Bar.class))
                .build();
    }

    @Test
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "ba")
    public void testSingleAlternativeIsSelected() {
        // Bar is selected
        Set<Bean<Bar>> barBeans = getBeans(Bar.class);
        assertEquals(barBeans.size(), 1);
        assertEquals(barBeans.iterator().next().getScope(), RequestScoped.class);
        // Foo is not selected
        Set<Bean<Foo>> fooBeans = getBeans(Foo.class);
        assertEquals(fooBeans.size(), 0);
    }
}
