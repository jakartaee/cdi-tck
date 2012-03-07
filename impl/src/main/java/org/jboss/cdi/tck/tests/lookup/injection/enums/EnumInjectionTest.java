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
package org.jboss.cdi.tck.tests.lookup.injection.enums;

import static org.jboss.cdi.tck.TestGroups.ENUMS;
import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.NewLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.lookup.injection.enums.EnclosingClass.AdvancedEnum;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * SHRINKWRAP-369 - we use extension we have to mark this test as integration one.
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = { INTEGRATION, ENUMS })
@SpecVersion(spec = "cdi", version = "20091101")
public class EnumInjectionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnumInjectionTest.class).withExtension(VerifyingExtension.class)
                .build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.9", id = "c"), @SpecAssertion(section = "3.9.1", id = "aa"),
            @SpecAssertion(section = "3.10", id = "c") })
    public void testBasicEnum() {
        verifyBasicEnum(BasicEnum.FOO);
        verifyBasicEnum(BasicEnum.BAR);
        verifyBasicEnum(BasicEnum.BAZ);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.9", id = "c"), @SpecAssertion(section = "3.10", id = "c") })
    public void testAdvancedEnum() {
        assertEquals(EnclosingClass.AdvancedEnum.values().length, 2);
        for (EnclosingClass.AdvancedEnum item : EnclosingClass.AdvancedEnum.values()) {
            assertNotNull(item.getSuperclassCat());
            assertNotNull(item.getSuperclassDog());
            assertNotNull(item.getInitializerAbstractDog());
            assertNotNull(item.getSubclassCat());
            assertNotNull(item.getSubclassDog());
        }
        // FOO and BAR have the same app scoped bean injected
        EnclosingClass.AdvancedEnum.FOO.getSubclassDog().setName("Rex");
        assertEquals(EnclosingClass.AdvancedEnum.BAR.getSuperclassDog().getName(), "Rex");
    }

    /**
     * Not in spec right now - CDI-127 reopened.
     * 
     * @param manager
     */
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    public void testNewBeansCreated(BeanManager manager) {
        assertNotNull(EnumWithNewInjectionPoint.FOO.getCat());
        assertNotNull(EnumWithNewInjectionPoint.FOO.getDog());
        assertEquals(manager.getBeans(Cat.class, NewLiteral.INSTANCE).size(), 1);
        assertEquals(manager.getBeans(Dog.class, NewLiteral.INSTANCE).size(), 1);
    }

    /**
     * Not in spec right now - CDI-127 reopened.
     * 
     * @param extension
     */
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    public void testProcessAnnotatedTypeFiredForEnums(VerifyingExtension extension) {
        assertTrue(extension.getObservedEnums().contains(BasicEnum.class));
        assertTrue(extension.getObservedEnums().contains(AdvancedEnum.class));
        assertTrue(extension.getObservedEnums().contains(EnumWithNewInjectionPoint.class));
    }

    private static void verifyBasicEnum(BasicEnum e) {
        assertNotNull(e.getCat());
        assertNotNull(e.getDog());
        assertNotNull(e.getCat().getIp());
        assertNull(e.getCat().getIp().getBean());
    }
}
