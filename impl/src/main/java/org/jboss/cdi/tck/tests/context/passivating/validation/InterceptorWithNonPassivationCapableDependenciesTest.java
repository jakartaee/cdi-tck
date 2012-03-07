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

import static org.testng.Assert.assertEquals;

import javax.enterprise.inject.spi.InterceptionType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Verifies that an interceptor that is passivation capable while having non-passivation capable dependencies is allowed
 * provided it does not intercept a bean declaring passivation scope.
 * 
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class InterceptorWithNonPassivationCapableDependenciesTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(InterceptorWithNonPassivationCapableDependenciesTest.class)
                .withClasses(Engine.class, EnginePowered.class, EnginePoweredInterceptor.class, Ferry.class, Vessel.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors()
                                .clazz(EnginePoweredInterceptor.class.getName()).up()).build();
    }

    @Test
    @SpecAssertion(section = "6.6.4", id = "k")
    public void test() {
        // it is enough to verify that the deployment passes validation and deploys
        assertEquals(
                1,
                getCurrentManager().resolveInterceptors(InterceptionType.AROUND_INVOKE,
                        new EnginePowered.EnginePoweredLiteral()).size());
    }
}
