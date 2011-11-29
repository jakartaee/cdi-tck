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
package org.jboss.jsr299.tck.tests.alternative.enterprise;

import static org.jboss.jsr299.tck.TestGroups.ALTERNATIVES;
import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class SessionBeanAlternativeTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SessionBeanAlternativeTest.class).withBeansXml("beans.xml").build();
    }

    @Test(groups = { INTEGRATION, ALTERNATIVES })
    @SpecAssertion(section = "5.1.1", id = "eb")
    public void testEnabledPolicyAvailable() {
        assert getBeans(EjbInterface.class).size() == 1;
        assert getBeans(EjbInterface.class).iterator().next().getTypes().contains(EjbInterface.class);
    }
}
