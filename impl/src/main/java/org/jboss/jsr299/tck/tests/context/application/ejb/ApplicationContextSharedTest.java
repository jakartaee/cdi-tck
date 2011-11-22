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
package org.jboss.jsr299.tck.tests.context.application.ejb;

import static org.jboss.jsr299.tck.TestGroups.CONTEXTS;
import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;
import static org.jboss.jsr299.tck.TestGroups.JAVAEE_FULL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * EJB and related tests with the built-in application context.
 * 
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class ApplicationContextSharedTest extends AbstractJSR299Test {

    @Deployment
    public static EnterpriseArchive createTestArchive() {
        return new EnterpriseArchiveBuilder().withTestClassPackage(ApplicationContextSharedTest.class).build();
    }

    @Test(groups = { JAVAEE_FULL, CONTEXTS, INTEGRATION })
    @SpecAssertion(section = "6.7.3", id = "e")
    public void testApplicationContextShared() throws Exception {
        FMSModelIII.reset();
        FMS flightManagementSystem = getInstanceByType(FMS.class);
        flightManagementSystem.climb();
        waitForClimbed();
        flightManagementSystem.descend();
        waitForDescended();
        assert flightManagementSystem.isSameBean();
    }

    private void waitForClimbed() throws Exception {
        for (int i = 0; !FMSModelIII.isClimbed() && i < 2000; i++) {
            Thread.sleep(10);
        }
    }

    @Test(groups = { JAVAEE_FULL, CONTEXTS, INTEGRATION })
    @SpecAssertion(section = "6.7.3", id = "dc")
    public void testApplicationScopeActiveDuringCallToEjbTimeoutMethod() throws Exception {
        FMS flightManagementSystem = getInstanceByType(FMS.class);
        flightManagementSystem.climb();
        waitForClimbed();
        assert flightManagementSystem.isApplicationScopeActive();
    }

    private void waitForDescended() throws Exception {
        for (int i = 0; !FMSModelIII.isDescended() && i < 2000; i++) {
            Thread.sleep(10);
        }
    }

}
