/*
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors
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
package org.jboss.cdi.tck.tests.event.observer.broken.validation.unsatisfied;

import static org.jboss.cdi.tck.cdi.Sections.OBSERVES;
import static org.jboss.cdi.tck.cdi.Sections.UNSATISFIED_AND_AMBIG_DEPENDENCIES;

import jakarta.enterprise.inject.spi.DeploymentException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Validates that injection points on observer methods are validated.
 *
 * <p>
 * This test was originally part of Seam Compatibility project.
 * </p>
 *
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ObserverMethodParameterInjectionValidationTest extends AbstractTest {

    @ShouldThrowException(DeploymentException.class)
    @Deployment
    public static WebArchive getDeployment() {
        return new WebArchiveBuilder().withTestClassPackage(ObserverMethodParameterInjectionValidationTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVES, id = "i"),
            @SpecAssertion(section = UNSATISFIED_AND_AMBIG_DEPENDENCIES, id = "ab") })
    public void test() {
    }
}
