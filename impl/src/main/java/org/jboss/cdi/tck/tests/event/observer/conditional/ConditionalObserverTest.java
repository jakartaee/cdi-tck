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
package org.jboss.cdi.tck.tests.event.observer.conditional;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.CONDITIONAL_OBSERVER_METHODS;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVERS_METHOD_INVOCATION;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_NOTIFICATION;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.spi.Context;
import javax.enterprise.event.Reception;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class ConditionalObserverTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ConditionalObserverTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVERS_METHOD_INVOCATION, id = "baa"), @SpecAssertion(section = CONDITIONAL_OBSERVER_METHODS, id = "a") })
    public void testConditionalObserver() {
        RecluseSpider.reset();
        getCurrentManager().fireEvent(new ConditionalEvent());
        // Should not be notified since bean is not instantiated yet
        assert !RecluseSpider.isNotified();

        // Now instantiate the bean and fire another event
        RecluseSpider bean = getInstanceByType(RecluseSpider.class);
        assert bean != null;
        // Must invoke a method to really create the instance
        assert !bean.isInstanceNotified();
        getCurrentManager().fireEvent(new ConditionalEvent());
        assert RecluseSpider.isNotified() && bean.isInstanceNotified();

        RecluseSpider.reset();
    }

    @Test
    @SpecAssertion(section = OBSERVERS_METHOD_INVOCATION, id = "baa")
    public void testObserverMethodInvokedOnReturnedInstanceFromContext() {
        RecluseSpider spider = getInstanceByType(RecluseSpider.class);
        spider.setWeb(new Web());
        getCurrentManager().fireEvent(new ConditionalEvent());
        assert spider.isInstanceNotified();
        assert spider.getWeb().getRings() == 1;
    }

    @Test
    @SpecAssertion(section = CONDITIONAL_OBSERVER_METHODS, id = "c")
    public void testNotifyEnumerationContainsNotifyValues() {
        assert Reception.values().length == 2;
        List<String> notifyValueNames = new ArrayList<String>();
        for (Reception value : Reception.values()) {
            notifyValueNames.add(value.name());
        }

        assert notifyValueNames.contains("IF_EXISTS");
        assert notifyValueNames.contains("ALWAYS");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = OBSERVER_NOTIFICATION, id = "bca")
    public void testConditionalObserverMethodNotInvokedIfNoActiveContext() {

        Tarantula.reset();
        Context requestContext = getCurrentConfiguration().getContexts().getRequestContext();
        Tarantula tarantula = getInstanceByType(Tarantula.class);
        tarantula.ping();

        try {
            // Instance exists but there is no context active for its scope
            setContextInactive(requestContext);
            getCurrentManager().fireEvent(new TarantulaEvent());
            // Observer method not called
            assertFalse(Tarantula.isNotified());
        } finally {
            setContextActive(requestContext);
        }
        // Context is active now
        getCurrentManager().fireEvent(new TarantulaEvent());
        assertTrue(Tarantula.isNotified());
    }

}
