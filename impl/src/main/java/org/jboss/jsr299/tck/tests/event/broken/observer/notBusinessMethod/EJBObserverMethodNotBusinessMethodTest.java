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
package org.jboss.jsr299.tck.tests.event.broken.observer.notBusinessMethod;

import static org.jboss.jsr299.tck.TestGroups.EVENTS;
import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test an observer method on an EJB that is neither a business method nor a static method of the class.
 * 
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class EJBObserverMethodNotBusinessMethodTest extends AbstractJSR299Test {

    @ShouldThrowException(Exception.class)
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EJBObserverMethodNotBusinessMethodTest.class).build();
    }

    @Test(groups = { INTEGRATION, EVENTS })
    @SpecAssertions({ @SpecAssertion(section = "10.4", id = "d"), @SpecAssertion(section = "10.4.2", id = "g") })
    public void testObserverMethodOnEnterpriseBeanNotBusinessMethodOrStaticFails() {
    }
}
