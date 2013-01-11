/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.interceptors.ordering.global;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.ENABLED_INTERCEPTORS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.descriptors.Beans11DescriptorImpl;
import org.jboss.cdi.tck.shrinkwrap.descriptors.BeansXmlClass;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test interceptor enablement and ordering.
 * 
 * See also CDI-18
 * 
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class GlobalInterceptorOrderingTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(GlobalInterceptorOrderingTest.class)
                // WEB-INF/classes
                .withClasses(Dao.class, LegacyInterceptor1.class, LegacyInterceptor2.class, LegacyInterceptor3.class,
                        WebApplicationGlobalInterceptor1.class)
                .withBeansXml(
                        new Beans11DescriptorImpl().interceptors(
                                //
                                new BeansXmlClass(LegacyInterceptor1.class), new BeansXmlClass(LegacyInterceptor2.class),
                                new BeansXmlClass(LegacyInterceptor3.class),
                                // Disable GEI3
                                new BeansXmlClass(GloballyEnabledInterceptor3.class, false),
                                // Enabled GPI1
                                new BeansXmlClass(GloballyPrioritizedInterceptor1.class, true),
                                // Override global configuration
                                new BeansXmlClass(GloballyEnabledInterceptor5.class),
                                // Enabled WAGI globally
                                new BeansXmlClass(WebApplicationGlobalInterceptor1.class, 1008)))
                .withBeanLibrary(
                        new Beans11DescriptorImpl().interceptors(
                        // globally enabled interceptors
                                new BeansXmlClass(GloballyEnabledInterceptor4.class, 1025), new BeansXmlClass(
                                        GloballyEnabledInterceptor5.class, 800), new BeansXmlClass(
                                        GloballyEnabledInterceptor1.class, 995), new BeansXmlClass(
                                        GloballyEnabledInterceptor3.class, 1015),
                                // decorators with globally set priority (but disabled)
                                new BeansXmlClass(GloballyPrioritizedInterceptor1.class, false, 1015), new BeansXmlClass(
                                        GloballyPrioritizedInterceptor2.class, false, 1020)), Transactional.class,
                        AbstractInterceptor.class, Service.class, GloballyEnabledInterceptor1.class,
                        GloballyEnabledInterceptor3.class, GloballyEnabledInterceptor4.class,
                        GloballyEnabledInterceptor5.class, GloballyPrioritizedInterceptor1.class,
                        GloballyPrioritizedInterceptor2.class)
                .withBeanLibrary(
                        // globally enabled interceptors
                        new Beans11DescriptorImpl().interceptors(new BeansXmlClass(GloballyEnabledInterceptor2.class, 1005)),
                        GloballyEnabledInterceptor2.class).build();
    }

    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "ba"), @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "bb"),
            @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "bc"), @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "bd"),
            @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "be") })
    public void testOrderingInWebInfClasses(Dao dao) {

        assertNotNull(dao);
        ActionSequence.reset();
        dao.ping();

        List<String> sequence = new ArrayList<String>();
        // 995
        sequence.add(GloballyEnabledInterceptor1.class.getName());
        // 1000
        sequence.add(LegacyInterceptor1.class.getName());
        // 1005
        sequence.add(GloballyEnabledInterceptor2.class.getName());
        // 1008
        sequence.add(WebApplicationGlobalInterceptor1.class.getName());
        // 1000 + 10
        sequence.add(LegacyInterceptor2.class.getName());
        // 1015
        sequence.add(GloballyPrioritizedInterceptor1.class.getName());
        // 1000 + 20
        sequence.add(LegacyInterceptor3.class.getName());
        // 1025
        sequence.add(GloballyEnabledInterceptor4.class.getName());
        // 1000 + 30 (originally 800)
        sequence.add(GloballyEnabledInterceptor5.class.getName());

        assertEquals(ActionSequence.getSequenceData(), sequence);
    }

    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "ba"), @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "bd") })
    public void testOrderingInLib(Service service) {

        assertNotNull(service);
        ActionSequence.reset();
        service.ping();

        List<String> sequence = new ArrayList<String>();
        // 800
        sequence.add(GloballyEnabledInterceptor5.class.getName());
        // 995
        sequence.add(GloballyEnabledInterceptor1.class.getName());
        // 1005
        sequence.add(GloballyEnabledInterceptor2.class.getName());
        // 1008
        sequence.add(WebApplicationGlobalInterceptor1.class.getName());
        // 1015
        sequence.add(GloballyEnabledInterceptor3.class.getName());
        // 1025
        sequence.add(GloballyEnabledInterceptor4.class.getName());

        assertEquals(ActionSequence.getSequenceData(), sequence);
    }
}
