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
package org.jboss.cdi.tck.tests.lookup.typesafe.resolution.primitive;

import static org.jboss.cdi.tck.cdi.Sections.PRIMITIVE_TYPES_AND_NULL_VALUES;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test varios types of injection points of primitive types that resolve to a producer method or producer field that returns a
 * null value at runtime. The container must inject the primitive type's default value as defined by the JSL 4.12.5
 * "Initial Values of Variables".
 * 
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class PrimitiveInjectionPointTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(PrimitiveInjectionPointTest.class).build();
    }

    @Inject
    Game game;

    @Test
    @SpecAssertion(section = PRIMITIVE_TYPES_AND_NULL_VALUES, id = "b")
    public void testPrimitiveInjectionPointResolvedToNonPrimitiveProducerMethod() {
        assertTrue(game.getInjectedByte() == 0);
        assertTrue(game.getInjectedShort() == 0);
        assertTrue(game.getInjectedInt() == 0);
        assertTrue(game.getInjectedLong() == 0L);
        assertTrue(game.getInjectedFloat() == 0.0f);
        assertTrue(game.getInjectedDouble() == 0.0d);
        assertTrue(game.getInjectedChar() == '\u0000');
        assertFalse(game.isInjectedBoolean());
    }
}
