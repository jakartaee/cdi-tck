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
package org.jboss.jsr299.tck.tests.implementation.initializer;

import static org.jboss.jsr299.tck.TestGroups.INITIALIZER_METHOD;
import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class EjbInitializerMethodTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EjbInitializerMethodTest.class)
                .withExcludedClass(InitializerMethodTest.class).build();
    }

    @Test(groups = { INITIALIZER_METHOD, INTEGRATION })
    @SpecAssertions({ @SpecAssertion(section = "3.10", id = "e") })
    // This DOES NOT TEST initializer methods on Java EE component classes PLM
    public void testInitializerMethodNotABusinessMethod() {
        AndalusianChicken.nonBusinessMethodCalled = false;
        getInstanceByType(LocalChicken.class).cluck();
        assert AndalusianChicken.nonBusinessMethodCalled;
    }
}
