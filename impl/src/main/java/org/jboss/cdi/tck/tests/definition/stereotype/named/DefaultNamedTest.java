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
package org.jboss.cdi.tck.tests.definition.stereotype.named;

import static org.testng.Assert.assertEquals;

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
public class DefaultNamedTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DefaultNamedTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.7", id = "a"), @SpecAssertion(section = "2.7.1.3", id = "aaa"),
            @SpecAssertion(section = "2.5.2", id = "e") })
    public void testStereotypeDeclaringNamed() {

        Set<Bean<FallowDeer>> fallowBeans = getBeans(FallowDeer.class);
        assertEquals(fallowBeans.size(), 1);
        assertEquals(fallowBeans.iterator().next().getName(), "fallowDeer");

        // The name is overriden by the bean
        Set<Bean<RoeDeer>> roeBeans = getBeans(RoeDeer.class);
        assertEquals(roeBeans.size(), 1);
        assertEquals(roeBeans.iterator().next().getName(), "roe");
    }

}
