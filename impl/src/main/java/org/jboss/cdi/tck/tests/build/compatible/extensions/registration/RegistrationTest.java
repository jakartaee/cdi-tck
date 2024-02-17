/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.build.compatible.extensions.registration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "4.0")
public class RegistrationTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(RegistrationTest.class)
                .withBuildCompatibleExtension(RegistrationExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.REGISTRATION_PHASE, id = "ac", note = "BeanInfo filtered to MyService")
    @SpecAssertion(section = Sections.REGISTRATION_PHASE, id = "ae", note = "ObserverInfo filtered to Object")
    @SpecAssertion(section = Sections.VALIDATION_PHASE, id = "a", note = "Validation determine test outcome")
    public void trigger() {
        // test is present in RegistrationExtension and if it fails, deployment should fail
    }
}
