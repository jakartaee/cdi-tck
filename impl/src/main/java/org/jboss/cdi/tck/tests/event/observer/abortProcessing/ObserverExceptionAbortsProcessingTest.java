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
package org.jboss.cdi.tck.tests.event.observer.abortProcessing;

import javax.enterprise.event.Observes;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class ObserverExceptionAbortsProcessingTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ObserverExceptionAbortsProcessingTest.class).build();
    }

    public static class AnEventType {
    }

    public static class AnObserverWithException {
        public static boolean wasNotified = false;
        public static final RuntimeException theException = new RuntimeException("RE1");

        public void observer(@Observes AnEventType event) {
            wasNotified = true;
            throw theException;
        }
    }

    public static class AnotherObserverWithException {
        public static boolean wasNotified = false;
        public static final RuntimeException theException = new RuntimeException("RE2");

        public void observer(@Observes AnEventType event) {
            wasNotified = true;
            throw theException;
        }
    }

    @Test
    @SpecAssertion(section = "10.5", id = "cb")
    public void testObserverThrowsExceptionAbortsNotifications() {
        // Fire an event that will be delivered to the two above observers
        AnEventType anEvent = new AnEventType();
        boolean fireFailed = false;
        try {
            getCurrentManager().fireEvent(anEvent);
        } catch (Exception e) {
            if (e.equals(AnObserverWithException.theException)) {
                fireFailed = true;
                assert AnObserverWithException.wasNotified;
                assert !AnotherObserverWithException.wasNotified;
            } else if (e.equals(AnotherObserverWithException.theException)) {
                fireFailed = true;
                assert !AnObserverWithException.wasNotified;
                assert AnotherObserverWithException.wasNotified;
            }
        }
        assert fireFailed;
    }
}
