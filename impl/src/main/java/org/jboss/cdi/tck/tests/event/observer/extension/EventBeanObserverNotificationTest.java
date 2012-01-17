/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cdi.tck.tests.event.observer.extension;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;

import java.lang.annotation.Annotation;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * Note that we have to declare test methods on test class (not abstract superclass) because of the way we analyze assertions
 * coverage.
 * 
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class EventBeanObserverNotificationTest extends AbstractObserverNotificationTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EventBeanObserverNotificationTest.class)
                .withExtension(ObserverExtension.class).build();
    }

    @Inject
    @Any
    private Event<Giraffe> event;

    @Override
    protected void fireEvent(Giraffe payload, Annotation... qualifiers) {
        event.select(qualifiers).fire(payload);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = "10.5", id = "fa")
    public void testNoQualifier() {
        testNoQualifierInternal();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = "10.5", id = "fa")
    public void testSingleQualifier() {
        testSingleQualifierInternal();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = "10.5", id = "fa")
    public void testMultipleQualifiers() {
        testMultipleQualifiersInternal();
    }
}
