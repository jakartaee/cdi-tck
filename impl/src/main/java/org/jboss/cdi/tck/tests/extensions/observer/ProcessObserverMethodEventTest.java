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

package org.jboss.cdi.tck.tests.extensions.observer;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for the extensions provided by the ProcessObserverMethod events.
 * 
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class ProcessObserverMethodEventTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProcessObserverMethodEventTest.class)
                .withExtension("javax.enterprise.inject.spi.Extension").build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.12", id = "aaa"), @SpecAssertion(section = "12.4", id = "i") })
    public void testProcessObserverMethodEventsSent() {
        assert ProcessObserverMethodObserver.getEventtypes().contains(EventA.class);
    }

    @Test
    @SpecAssertion(section = "11.5.12", id = "aba")
    public void testGetAnnotatedMethod() {
        assert ProcessObserverMethodObserver.getAnnotatedMethod().getParameters().iterator().next().getBaseType()
                .equals(EventA.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.12", id = "ba"), @SpecAssertion(section = "12.4", id = "i") })
    public void testGetObserverMethod() {
        assert ProcessObserverMethodObserver.getObserverMethod().getObservedType().equals(EventA.class);
    }

}
