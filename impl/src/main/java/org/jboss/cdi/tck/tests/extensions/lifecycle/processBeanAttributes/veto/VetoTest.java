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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes.veto;

import static org.jboss.cdi.tck.cdi.Sections.PBA;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class VetoTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(VetoTest.class).withExtension(VetoingExtension.class).build();
    }

    @Test
    @SpecAssertion(section = PBA, id = "be")
    public void testBeanVetoed() {
        assertEquals(getCurrentManager().getBeans(Field.class).size(), 0);
        assertEquals(getCurrentManager().getBeans(Wheat.class).size(), 0);
        assertEquals(getCurrentManager().getBeans(Flower.class).size(), 1);
        assertEquals(getCurrentManager().getBeans(Factory.class).size(), 1);
        assertEquals(getCurrentManager().getBeans(Car.class).size(), 0);
    }
}
