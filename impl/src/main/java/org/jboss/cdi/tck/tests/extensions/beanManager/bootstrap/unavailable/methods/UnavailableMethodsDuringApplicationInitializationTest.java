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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.cdi.Sections.BEANMANAGER;

/**
 * <p/>
 * This test was originally part of the Weld test suite.
 * <p/>
 *
 * @author Martin Kouba
 * @author Tomas Remes
 */
@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class UnavailableMethodsDuringApplicationInitializationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(UnavailableMethodsDuringApplicationInitializationTest.class)
                .withExtension(WrongExtension.class).build();
    }

    @Test
    @SpecAssertions({@SpecAssertion(section = BEANMANAGER, id = "ad"),
            @SpecAssertion(section = BEANMANAGER, id = "ae"),
            @SpecAssertion(section = BEANMANAGER, id = "af"),
            @SpecAssertion(section = BEANMANAGER, id = "ag"),
            @SpecAssertion(section = BEANMANAGER, id = "ah"),
            @SpecAssertion(section = BEANMANAGER, id = "ai"),
            @SpecAssertion(section = BEANMANAGER, id = "aj"),
            @SpecAssertion(section = BEANMANAGER, id = "ak"),
            @SpecAssertion(section = BEANMANAGER, id = "al"),
            @SpecAssertion(section = BEANMANAGER, id = "am")})
    public void testUnavailableMethods() {
        // Test deployment does not fail
    }

}
