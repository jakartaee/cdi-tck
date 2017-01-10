/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.lifecycle.broken.observerMethod;

import static org.jboss.cdi.tck.cdi.Sections.AFTER_BEAN_DISCOVERY;

import javax.enterprise.inject.spi.DeploymentException;

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
 * @author Tomas Remes
 */
@Test
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class ObserverMethodWithoutNotifyMethodTest extends AbstractTest {

    @Deployment
    @ShouldThrowException(DeploymentException.class)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(ObserverMethodWithoutNotifyMethodTest.class)
                .withClasses(AfterBeanDiscoveryObserver.class, Foo.class, FooObserver.class).withExtension(AfterBeanDiscoveryObserver.class)
                .build();
    }

    // unable to process ObserverMethodConfigurator due to missing notify method impl
    @Test
    @SpecAssertions({ @SpecAssertion(section = AFTER_BEAN_DISCOVERY, id = "ee") })
    public void unableToProcessObserverMethodConfigurator() {
    }
}
