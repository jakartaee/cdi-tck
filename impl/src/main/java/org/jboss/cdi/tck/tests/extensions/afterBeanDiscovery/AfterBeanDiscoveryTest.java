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
package org.jboss.cdi.tck.tests.extensions.afterBeanDiscovery;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.context.spi.Context;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class AfterBeanDiscoveryTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AfterBeanDiscoveryTest.class)
                .withExtension(AfterBeanDiscoveryObserver.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.2", id = "db") })
    public void testBeanIsAdded() {
        assert getBeans(Cockatoo.class).size() == 1;
        assert getInstanceByType(Cockatoo.class).getName().equals("Billy");
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = "", id = "") })
    public void testCustomDependentBeanInjectionPointIsAvailable(Cage cage) {
        assertNotNull(cage);
        assertNotNull(cage.getCockatoo());
        assertNotNull(cage.getCockatoo().getInjectionPoint());
        assertEquals(cage.getCockatoo().getInjectionPoint().getBean().getBeanClass(), Cage.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.5.2", id = "da") })
    public void testProcessBeanIsFired() {
        assert AfterBeanDiscoveryObserver.isProcessBeanFiredForCockatooBean;
    }

    @Test
    @SpecAssertion(section = "11.5.2", id = "ea")
    public void testProcessObserverMethodFiredWhileAddingObserverMethod() {
        assertTrue(AfterBeanDiscoveryObserver.isTalkProcessObserverMethodObserved);
    }

    @Test
    @SpecAssertion(section = "11.5.2", id = "eb")
    public void testObserverMethodRegistered() {
        getCurrentManager().fireEvent(new Talk("Hello"));
        assertTrue(AfterBeanDiscoveryObserver.addedObserverMethod.isObserved());
    }

    @Test
    @SpecAssertion(section = "11.5.2", id = "f")
    public void testAddContext() {
        Context context = getCurrentManager().getContext(SuperScoped.class);
        assertNotNull(context);
        assertTrue(context.isActive());
        assertTrue(context instanceof SuperContext);
    }

}
