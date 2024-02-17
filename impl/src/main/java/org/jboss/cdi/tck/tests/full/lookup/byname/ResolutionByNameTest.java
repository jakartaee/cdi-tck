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
package org.jboss.cdi.tck.tests.full.lookup.byname;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.AMBIG_NAMES;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_BEAN_BY_NAME;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.spi.Bean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * This test is same as {@link org.jboss.cdi.tck.tests.lookup.byname.ResolutionByNameTest} except that it includes
 * a bean with specialization
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class ResolutionByNameTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ResolutionByNameTest.class)
                .build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = AMBIG_NAMES, id = "ca"), @SpecAssertion(section = BM_OBTAIN_BEAN_BY_NAME, id = "aa"),
            @SpecAssertion(section = BM_OBTAIN_BEAN_BY_NAME, id = "b") })
    public void testAmbiguousELNamesResolved() throws Exception {
        // Cod, Plaice and AlaskaPlaice are named "whitefish" - Cod is a not-enabled alternative, AlaskaPlaice specializes
        // Plaice
        Set<Bean<?>> beans = getCurrentManager().getBeans("whitefish");
        assertEquals(beans.size(), 1);
        assertTrue(getCurrentManager().resolve(beans).getTypes().contains(AlaskaPlaice.class));
        // Both Salmon and Sole are named "fish" - Sole is an enabled alternative
        beans = getCurrentManager().getBeans("fish");
        assertEquals(beans.size(), 2);
        assertTrue(getCurrentManager().resolve(beans).getTypes().contains(Sole.class));
    }

}
