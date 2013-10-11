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
package org.jboss.cdi.tck.tests.extensions.alternative.metadata.interceptor;

import static org.jboss.cdi.tck.cdi.Sections.BBD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;


/**
 * This test class contains tests for adding meta data using extensions.
 * 
 * @author Matej Briskar
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class AlternativeMetadataInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(AlternativeMetadataInterceptorTest.class)
                .withClasses(InterceptorExtension.class, Login.class, LoginInterceptor.class, LoginInterceptorBinding.class, Secured.class)
                .withBeansXml(Descriptors.create(BeansDescriptor.class).createInterceptors().clazz(LoginInterceptor.class.getName()).up())
                .withExtension(InterceptorExtension.class).build();
    }

    @Inject
    private Login login;

    @Inject
    @Secured
    private Login securedLogin;

    @Test
    @SpecAssertions({ @SpecAssertion(section = BBD, id = "af") })
    public void testInterceptorInterceptsOnlyBindedClass() {
        assertTrue(login instanceof Login);
        assertTrue(securedLogin instanceof Login);
        assertEquals(login.login(), "logged");
        assertEquals(securedLogin.login(), "intercepted");
    }


}

