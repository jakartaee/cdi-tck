/*
 * Copyright 2024, IBM Corp., and individual contributors
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
package org.jboss.cdi.tck.tests.build.compatible.extensions.changeInterceptorBinding;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.interceptors.InterceptorsSections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test interceptor bindings applied to methods via extension are used to bind the correct interceptor and returned from InvocationContext.getInterceptorBindings()
 */
@SpecVersion(spec = "cdi", version = "4.1")
public class ChangeInterceptorBindingTest extends AbstractTest {
    
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(ChangeInterceptorBindingTest.class)
                .withBuildCompatibleExtension(ChangeInterceptorBindingExtension.class)
                .build();
    }
    
    @Test
    @SpecAssertion(section = Sections.ENHANCEMENT_PHASE, id = "b")
    @SpecAssertion(section = InterceptorsSections.INVOCATIONCONTEXT, id="o")
    public void test() {
        assertEquals(getContextualReference(MyService.class).hello(), "Intercepted(foo): Hello!");
    }

}
