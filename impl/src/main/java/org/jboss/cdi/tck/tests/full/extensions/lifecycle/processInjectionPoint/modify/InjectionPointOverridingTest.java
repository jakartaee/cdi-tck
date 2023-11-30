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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle.processInjectionPoint.modify;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_INJECTION_POINT;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * </p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class InjectionPointOverridingTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InjectionPointOverridingTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL).decorators(AnimalDecorator.class))
                .withExtension(ModifyingExtension.class).build();
    }

    @Inject
    InjectingBean bean;

    @Inject
    @Fast
    Hound hound;

    @Inject
    @Lazy
    Dog dog;

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "a"), @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "bb"),
            @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "c") })
    public void testOverridingFieldInjectionPoint() {
        assertTrue(bean.getDog() instanceof Hound);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "a"), @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "bb"),
            @SpecAssertion(section = PROCESS_INJECTION_POINT, id = "c") })
    public void testDelegateInjectionPoint() {
        assertNotNull(hound);
        assertTrue(hound.decorated());
        assertNotNull(dog);
        assertTrue(dog.decorated());
    }
}
