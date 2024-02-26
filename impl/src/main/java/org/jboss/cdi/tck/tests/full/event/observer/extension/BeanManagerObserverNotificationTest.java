/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.event.observer.extension;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.AFTER_BEAN_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_NOTIFICATION;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_RESOLUTION;

import java.lang.annotation.Annotation;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * </p>
 *
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class BeanManagerObserverNotificationTest extends AbstractObserverNotificationTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeanManagerObserverNotificationTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withExtension(ObserverExtension.class).build();
    }

    @Override
    public void fireEvent(Giraffe payload, Annotation... qualifiers) {
        getCurrentManager().getEvent().select(Giraffe.class, qualifiers).fire(payload);
    }

    @Test(groups = CDI_FULL)
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_NOTIFICATION, id = "fb"),
            @SpecAssertion(section = AFTER_BEAN_DISCOVERY, id = "eb"),
            @SpecAssertion(section = OBSERVER_RESOLUTION, id = "k") })
    public void testNotifyInvoked() {
        testNotifyInvokedInternal();
    }

}
