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
package org.jboss.cdi.tck.tests.event.observer.extension;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.ABD;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_NOTIFICATION;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_RESOLUTION;

import java.lang.annotation.Annotation;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class BeanManagerObserverNotificationTest extends AbstractObserverNotificationTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeanManagerObserverNotificationTest.class)
                .withExtension(ObserverExtension.class).build();
    }

    @Override
    public void fireEvent(Giraffe payload, Annotation... qualifiers) {
        getCurrentManager().fireEvent(payload, qualifiers);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_NOTIFICATION, id = "fb"), @SpecAssertion(section = ABD, id = "eb"),
            @SpecAssertion(section = OBSERVER_RESOLUTION, id = "k") })
    public void testNotifyInvoked() {
        testNotifyInvokedInternal();
    }

}
