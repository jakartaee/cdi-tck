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
package org.jboss.cdi.tck.tests.context.passivating.validation;

import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_VALIDATION;
import static org.testng.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.Collections;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Verifies that a decorator that is passivation capable while having non-passivation capable dependencies is allowed provided
 * it does not decorate a bean declaring passivation scope.
 * 
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class DecoratorWithNonPassivationCapableDependenciesTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(DecoratorWithNonPassivationCapableDependenciesTest.class)
                .withClasses(Engine.class, Ferry.class, Vessel.class, VesselDecorator.class)
                .withBeansXml(new BeansXml().decorators(VesselDecorator.class)).build();
    }

    @Test
    @SpecAssertion(section = PASSIVATION_VALIDATION, id = "l")
    public void testDeploymentValid() {
        // it is enough to verify that the deployment passes validation and deploys
        assertEquals(1, getCurrentManager().resolveDecorators(Collections.<Type> singleton(Ferry.class)).size());
    }
}
