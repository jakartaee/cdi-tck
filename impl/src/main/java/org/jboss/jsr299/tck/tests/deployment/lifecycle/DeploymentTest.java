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

package org.jboss.jsr299.tck.tests.deployment.lifecycle;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests related to the final deployment phase of the lifecycle.
 * 
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class DeploymentTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DeploymentTest.class).withBeansXml("beans.xml")
                .withExtension("javax.enterprise.inject.spi.Extension.DeploymentTest").build();
    }

    @Test(groups = "rewrite")
    @SpecAssertions({ @SpecAssertion(section = "11.5.2", id = "a"), @SpecAssertion(section = "11.5.3", id = "a"),
            @SpecAssertion(section = "12.2", id = "g") })
    public void testDeployedManagerEvent() {
        assert ManagerObserver.isAfterDeploymentValidationCalled();
        // Make sure the manager does accept requests now
        getCurrentManager().fireEvent("event");
    }

    @Test(groups = {})
    @SpecAssertions({ @SpecAssertion(section = "11.1", id = "f") })
    public void testOnlyEnabledBeansDeployed() {
        assert !getBeans(User.class).isEmpty();
        assert getBeans(DataAccessAuthorizationDecorator.class).isEmpty();
        assert getBeans(Interceptor1.class).isEmpty();
        assert getBeans(DisabledBean.class).isEmpty();
    }
}
