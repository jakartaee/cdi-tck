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
package org.jboss.cdi.tck.tests.lookup.el;

import static org.jboss.cdi.tck.cdi.Sections.CONTEXTUAL_INSTANCE;
import static org.jboss.cdi.tck.cdi.Sections.DEPENDENT_SCOPE_EL;
import static org.jboss.cdi.tck.cdi.Sections.EL;
import static org.jboss.cdi.tck.cdi.Sections.NAMES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0-PFD")
public class ResolutionByNameTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ResolutionByNameTest.class).build();
    }

    @Test
    @SpecAssertion(section = DEPENDENT_SCOPE_EL, id = "a")
    public void testQualifiedNameLookup() {
        assertTrue(getCurrentConfiguration().getEl()
                .evaluateValueExpression(getCurrentManager(), "#{(game.value == 'foo' and game.value == 'foo') ? game.value == 'foo' : false}", Boolean.class));
        assertEquals(getContextualReference(Counter.class).getCount(), 1);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = CONTEXTUAL_INSTANCE, id = "a"), @SpecAssertion(section = CONTEXTUAL_INSTANCE, id = "b") })
    public void testContextCreatesNewInstanceForInjection() {
        Context requestContext = getCurrentManager().getContext(RequestScoped.class);
        Bean<Tuna> tunaBean = getBeans(Tuna.class).iterator().next();
        assertNull(requestContext.get(tunaBean));
        TunaFarm tunaFarm = getCurrentConfiguration().getEl().evaluateValueExpression(getCurrentManager(), "#{tunaFarm}",
                TunaFarm.class);
        assertNotNull(tunaFarm.tuna);
        long timestamp = tunaFarm.tuna.getTimestamp();
        // Lookup once again - do not create new instance - contextual instance already exists
        Tuna tuna = requestContext.get(tunaBean);
        assertNotNull(tuna);
        assertEquals(timestamp, tuna.getTimestamp());
    }

    @Test
    @SpecAssertion(section = EL, id = "c")
    public void testUnresolvedNameReturnsNull() {
        assertNull(
                getCurrentManager().getELResolver().getValue(getCurrentConfiguration().getEl().createELContext(getCurrentManager()), null, "nonExistingTuna"));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = EL, id = "d"), @SpecAssertion(section = NAMES, id = "a") })
    public void testELResolverReturnsContextualInstance() {
        Salmon salmon = getContextualReference(Salmon.class);
        salmon.setAge(3);
        assertEquals(getCurrentConfiguration().getEl().evaluateValueExpression(getCurrentManager(), "#{salmon.age}", Integer.class), new Integer(3));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = NAMES, id = "a") })
    public void testBeanNameWithSeparatedListOfELIdentifiers() {
        assertNotNull(getCurrentConfiguration().getEl().evaluateValueExpression(getCurrentManager(), "#{magic.golden.fish}", GoldenFish.class));
    }
}
