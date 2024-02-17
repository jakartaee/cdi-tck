/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.decorators.definition.broken.nonDependent;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_BEAN;

import jakarta.enterprise.inject.spi.DefinitionException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test was originally part of the Weld testsuite.
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class NonDependentDecoratorTest extends AbstractTest {

    @Deployment
    @ShouldThrowException(DefinitionException.class)
    public static Archive<?> deploy() {
        return new WebArchiveBuilder().withTestClassPackage(NonDependentDecoratorTest.class).build();
    }

    @Test(groups = CDI_FULL)
    @SpecAssertion(section = DECORATOR_BEAN, id = "f")
    public void testDeploymentWithScopedDecorator() {

    }

}
