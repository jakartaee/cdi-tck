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

package org.jboss.cdi.tck.tests.deployment.lifecycle.broken.failsDuringValidation;

import static org.jboss.cdi.tck.TestGroups.REWRITE;

import javax.enterprise.inject.spi.DeploymentException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * FIXME This test needs refactoring
 * 
 * Tests a failure that occurs during validation of beans, which occurs after the AfterBeanDiscovery event but before the
 * AfterDeploymentValidation event is raised.
 * 
 * @author David Allen
 * @author Dan Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class AfterBeanDiscoveryFailureTest extends AbstractTest {

    @ShouldThrowException(DeploymentException.class)
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AfterBeanDiscoveryFailureTest.class)
                .withExtension("javax.enterprise.inject.spi.Extension").build();
    }

    @Test(groups = { REWRITE })
    // @SpecAssertions({ @SpecAssertion(section = "11.5.2", id = "a"), @SpecAssertion(section = "12.2", id = "e"),
    // @SpecAssertion(section = "12.2", id = "f") })
    // WBRI-312
    public void testDeploymentFailsDuringValidation() {
    }

    // Need to communicate state of container at the time of failure
    // @Override
    // @AfterClass(alwaysRun = true, groups = "scaffold")
    // public void afterClass() throws Exception
    // {
    // super.afterClass();
    // assert BeanDiscoveryObserver.isManagerInitialized();
    // }

}
