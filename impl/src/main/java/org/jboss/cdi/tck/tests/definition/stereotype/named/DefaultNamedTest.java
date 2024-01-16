/*
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

import static org.jboss.cdi.tck.cdi.Sections.DEFAULT_NAME;
import static org.jboss.cdi.tck.cdi.Sections.NAMED_STEREOTYPE;
import static org.jboss.cdi.tck.cdi.Sections.STEREOTYPES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.literal.NamedLiteral;
import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class DefaultNamedTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DefaultNamedTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = STEREOTYPES, id = "a"), @SpecAssertion(section = NAMED_STEREOTYPE, id = "aa"),
            @SpecAssertion(section = NAMED_STEREOTYPE, id = "ab"), @SpecAssertion(section = DEFAULT_NAME, id = "e") })
    public void testStereotypeDeclaringNamed() {
        Bean<FallowDeer> fallowBean = getUniqueBean(FallowDeer.class);
        assertEquals(fallowBean.getName(), "fallowDeer");
        assertTrue(annotationSetMatches(fallowBean.getQualifiers(), Any.Literal.INSTANCE, Default.Literal.INSTANCE));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = NAMED_STEREOTYPE, id = "aa") })
    public void testStereotypeNamedOverridenByBean() {
        // The bean name is overriden by the bean
        Bean<RoeDeer> roeBean = getUniqueBean(RoeDeer.class);
        assertEquals(roeBean.getName(), "roe");
        assertTrue(annotationSetMatches(roeBean.getQualifiers(), Any.Literal.INSTANCE, Default.Literal.INSTANCE,
                NamedLiteral.of("roe")));
    }

}
