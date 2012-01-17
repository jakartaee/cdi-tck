/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.lookup.byname;

import static org.jboss.cdi.tck.TestGroups.RESOLUTION;

import java.util.Set;

import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class ResolutionByNameTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ResolutionByNameTest.class).withBeansXml("beans.xml").build();
    }

    @Test(groups = { RESOLUTION })
    @SpecAssertions({ @SpecAssertion(section = "5.3.1", id = "ca"), @SpecAssertion(section = "11.3.6", id = "aa"),
            @SpecAssertion(section = "11.3.6", id = "b") })
    public void testAmbiguousELNamesResolved() throws Exception {
        // Cod, Plaice and AlaskaPlaice are named "whitefish" - Cod is a not-enabled policy, AlaskaPlaice specializes Plaice
        Set<Bean<?>> beans = getCurrentManager().getBeans("whitefish");
        assert beans.size() == 1;
        assert getCurrentManager().resolve(beans).getTypes().contains(AlaskaPlaice.class);
        // Both Salmon and Sole are named "fish" - Sole is an enabled policy
        beans = getCurrentManager().getBeans("fish");
        assert beans.size() == 2;
        assert getCurrentManager().resolve(beans).getTypes().contains(Sole.class);
    }

    @Test
    @SpecAssertion(section = "3.13", id = "a")
    public void testFieldNameUsedAsBeanName() {
        assert getInstanceByType(FishingNet.class).isCarpInjected();
    }
}
