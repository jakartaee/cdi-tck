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
package org.jboss.cdi.tck.tests.extensions.beanManager.bootstrap.unavailable.methods;

import static org.jboss.cdi.tck.cdi.Sections.BM_DECORATOR_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.BM_INTERCEPTOR_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBSERVER_METHOD_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_BEAN_BY_NAME;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_BEAN_BY_TYPE;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_CONTEXTUAL_REFERENCE;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_INJECTABLE_REFERENCE;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_PASSIVATION_CAPABLE_BEAN;
import static org.jboss.cdi.tck.cdi.Sections.BM_RESOLVE_AMBIGUOUS_DEP;
import static org.jboss.cdi.tck.cdi.Sections.BM_VALIDATE_IP;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of the Weld test suite.
 * <p>
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class UnavailableMethodsDuringApplicationInitializationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(UnavailableMethodsDuringApplicationInitializationTest.class)
                .withExtension(WrongExtension.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_CONTEXTUAL_REFERENCE, id = "d"),
            @SpecAssertion(section = BM_OBTAIN_INJECTABLE_REFERENCE, id = "d"),
            @SpecAssertion(section = BM_OBTAIN_BEAN_BY_TYPE, id = "g"),
            @SpecAssertion(section = BM_OBTAIN_BEAN_BY_NAME, id = "c"),
            @SpecAssertion(section = BM_OBTAIN_PASSIVATION_CAPABLE_BEAN, id = "b"),
            @SpecAssertion(section = BM_RESOLVE_AMBIGUOUS_DEP, id = "e"), @SpecAssertion(section = BM_VALIDATE_IP, id = "b"),
            @SpecAssertion(section = BM_OBSERVER_METHOD_RESOLUTION, id = "f"),
            @SpecAssertion(section = BM_DECORATOR_RESOLUTION, id = "f"),
            @SpecAssertion(section = BM_INTERCEPTOR_RESOLUTION, id = "e") })
    public void testUnavailableMethods() {
        // Test deployment does not fail
    }

}
