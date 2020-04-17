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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.lang.annotation.Annotation;

import jakarta.inject.Inject;

import org.jboss.cdi.tck.AbstractTest;

public abstract class AbstractObserverNotificationTest extends AbstractTest {

    @Inject
    private ObserverExtension extension;

    /**
     * Template method so that we can test both events fired using BeanManager as well as the Event bean.
     */
    protected abstract void fireEvent(Giraffe payload, Annotation... qualifiers);

    protected void testNotifyInvokedInternal() {
        reset();

        Giraffe payload = new Giraffe();
        fireEvent(payload);
        verifyObserversNotNotified(extension.getFiveMeterTallGiraffeObserver(),
                extension.getSixMeterTallAngryGiraffeObserver(), extension.getAngryNubianGiraffeObserver());
        verifyObserversNotified(payload, extension.getAnyGiraffeObserver());
    }

    private void reset() {
        extension.getAnyGiraffeObserver().reset();
        extension.getFiveMeterTallGiraffeObserver().reset();
        extension.getSixMeterTallAngryGiraffeObserver().reset();
        extension.getAngryNubianGiraffeObserver().reset();
    }

    private void verifyObserversNotified(Giraffe payload, GiraffeObserver... observers) {
        for (GiraffeObserver observer : observers) {
            assertEquals(payload, observer.getReceivedPayload());
        }
    }

    private void verifyObserversNotNotified(GiraffeObserver... observers) {
        for (GiraffeObserver observer : observers) {
            assertNull(observer.getReceivedPayload());
        }
    }

}
