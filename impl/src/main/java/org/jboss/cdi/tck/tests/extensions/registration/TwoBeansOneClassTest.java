/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.cdi.tck.tests.extensions.registration;

import javax.enterprise.inject.spi.DeploymentException;
import static org.jboss.cdi.tck.cdi.Sections.BBD;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.INIT_EVENTS;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

@SpecVersion(spec = "cdi", version = "20091101")
public class TwoBeansOneClassTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(TwoBeansOneClassTest.class)
                .withLibrary(TwoBeansOneClassExtension.class, Beanie.class, BeanieType.class, BeanieTypeLiteral.class).build();
    }

    @Test
    @SpecAssertions({
        @SpecAssertion(section = INIT_EVENTS, id = "b"),
        @SpecAssertion(section = INIT_EVENTS, id = "bb"),
        @SpecAssertion(section = BEAN_ARCHIVE, id = "f"),
        @SpecAssertion(section = BBD, id = "af"),
        @SpecAssertion(section = BBD, id = "afa")})
    public void testTwoBeansWithOneBaseClass() {
        assertEquals(beanManager.getBeans(Beanie.class), 0);
        assertEquals(beanManager.getBeans(Beanie.class, new BeanieTypeLiteral() {
            @Override
            public String value() {
                return "basic";
            }
        }), 1);

        assertEquals(beanManager.getBeans(Beanie.class, new BeanieTypeLiteral() {
            @Override
            public String value() {
                return "propeller";
            }
        }), 1);
    }
}
