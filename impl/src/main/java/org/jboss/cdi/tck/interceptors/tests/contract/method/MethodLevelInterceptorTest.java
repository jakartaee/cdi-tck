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
package org.jboss.cdi.tck.interceptors.tests.contract.method;

import static org.jboss.cdi.tck.interceptors.InterceptorsSections.ASSOCIATING_INT_USING_INTERCEPTORS_ANNOTATION;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.CONSTRUCTOR_AND_METHOD_LEVEL_INT;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.EXCLUDING_INTERCEPTORS;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "interceptors", version = "1.2")
public class MethodLevelInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(MethodLevelInterceptorTest.class).build();
    }

    @Test
    @SpecAssertion(section = CONSTRUCTOR_AND_METHOD_LEVEL_INT, id = "d")
    @SpecAssertion(section = ASSOCIATING_INT_USING_INTERCEPTORS_ANNOTATION, id = "a")
    @SpecAssertion(section = CONSTRUCTOR_AND_METHOD_LEVEL_INT, id = "e")
    public void testInterceptorCanBeAppliedToMoreThanOneMethod() {
        Fish fish = getContextualReference(Fish.class);
        assertEquals(fish.foo(), "Intercepted bar");
        assertEquals(fish.ping(),"Intercepted pong");
        assertEquals(fish.getName(),"Salmon");
        assertEquals(FishInterceptor.getInstanceCount(), 1);
    }

    @Test
    @SpecAssertion(section = EXCLUDING_INTERCEPTORS, id = "b")
    public void testExcludeClassInterceptors() {
        assertEquals(getContextualReference(Dog.class).foo(), "Intercepted bar");
        assertEquals(getContextualReference(Dog.class).ping(), "pong");
    }
}
