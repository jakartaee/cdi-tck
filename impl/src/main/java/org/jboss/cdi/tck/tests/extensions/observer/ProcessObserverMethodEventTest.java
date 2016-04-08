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

import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY_STEPS;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_METHOD_CONFIGURATOR;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_OBSERVER_METHOD;

import java.util.Set;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.ObserverMethod;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for the extensions provided by the ProcessObserverMethod events.
 *
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0-EDR1")
public class ProcessObserverMethodEventTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProcessObserverMethodEventTest.class)
                .withExtension(ProcessObserverMethodObserver.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_OBSERVER_METHOD, id = "aaa"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "kb") })
    public void testProcessObserverMethodEventsSent() {
        Assert.assertTrue(ProcessObserverMethodObserver.getEventtypes().contains(EventA.class));
    }

    @Test
    @SpecAssertion(section = PROCESS_OBSERVER_METHOD, id = "aba")
    public void testGetAnnotatedMethod() {
        Assert.assertEquals(ProcessObserverMethodObserver.getAnnotatedMethod().getParameters().iterator().next().getBaseType()
                , (EventA.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_OBSERVER_METHOD, id = "ba"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "kb") })
    public void testGetObserverMethod() {
        Assert.assertEquals(ProcessObserverMethodObserver.getObserverMethod().getObservedType(), (EventA.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_METHOD_CONFIGURATOR, id = "ac"), @SpecAssertion(section = PROCESS_OBSERVER_METHOD, id = "daa") })
    public void replaceWithSetObserverMethod() {
        Set<ObserverMethod<? super EventC>> observerMethods = getCurrentManager().resolveObserverMethods(new EventC(), Any.Literal.INSTANCE);
        Assert.assertEquals(observerMethods.size(), 1);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_OBSERVER_METHOD, id = "dac") })
    public void vetoEventD() {
        Set<ObserverMethod<? super EventD>> observerMethods = getCurrentManager().resolveObserverMethods(new EventD(), Any.Literal.INSTANCE);
        Assert.assertEquals(observerMethods.size(), 0);
    }

}
