/*
 * Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual
 * contributors
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

package org.jboss.cdi.tck.tests.full.interceptors.definition.conflictingenablement;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.ENABLED_DECORATORS;
import static org.jboss.cdi.tck.cdi.Sections.ENABLED_INTERCEPTORS;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;

import java.util.List;

/**
 * This test was originally part of Weld testsuite
 *
 * @author Martin Kouba
 * @see <a>WELD-1780</a>
 */

@SpecVersion(spec = "cdi", version = "2.0")
public class InterceptorConflictingEnablementTest extends AbstractTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InterceptorConflictingEnablementTest.class)
                .withBeansXml(new BeansXml()
                        .interceptors(TransactionalInterceptor.class, LoggingInterceptor.class)
                        .decorators(TestDecorator.class, AnotherTestDecorator.class))
                .build();
    }

    @org.testng.annotations.Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = CDI_FULL)
    @SpecAssertions({ @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "k"), @SpecAssertion(section = ENABLED_DECORATORS, id = "d") })
    public void testInterception(TestBean testBean) {
        ActionSequence.reset();
        testBean.ping();
        List<String> data = ActionSequence.getSequenceData();
        assertEquals(4, data.size());
        assertEquals(LoggingInterceptor.class.getName(), data.get(0));
        assertEquals(TransactionalInterceptor.class.getName(), data.get(1));
        assertEquals(AnotherTestDecorator.class.getName(), data.get(2));
        assertEquals(TestDecorator.class.getName(), data.get(3));
    }

}
