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
package org.jboss.cdi.tck.tests.lookup.injection.non.contextual.ws;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.JAX_WS;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_RESOURCE;
import static org.jboss.cdi.tck.cdi.Sections.FIELDS_INITIALIZER_METHODS_EE;
import static org.jboss.cdi.tck.cdi.Sections.INJECTION_EE;
import static org.jboss.cdi.tck.cdi.Sections.RESOURCE_TYPES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.xml.ws.WebServiceRef;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0-EDR2")
public class WebServiceResourceTest extends AbstractTest {

    @WebServiceRef(value = SheepWSService.class)
    public SheepWS sheepWS;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(WebServiceResourceTest.class).build();
    }

    @Test(groups = { JAVAEE_FULL, JAX_WS })
    @SpecAssertions({ @SpecAssertion(section = INJECTION_EE, id = "ee"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "aq"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "ar") })
    public void testInjectionIntoWebServiceEndpoint() throws Exception {
        assertTrue(sheepWS.isSheepInjected());
    }

    @Test(groups = { JAVAEE_FULL, JAX_WS })
    @SpecAssertions({ @SpecAssertion(section = DECLARING_RESOURCE, id = "ff"),
            @SpecAssertion(section = RESOURCE_TYPES, id = "ae") })
    public void testResourceBeanTypes() {
        Bean<SheepWS> sheepWsBean = getUniqueBean(SheepWS.class);
        assertEquals(sheepWsBean.getTypes().size(), 3);
        typeSetMatches(sheepWsBean.getTypes(), Object.class, SheepWS.class, SheepWSEndPoint.class);

        @SuppressWarnings("serial")
        Bean<SheepWS> blackSheepWsBean = getUniqueBean(SheepWS.class, new AnnotationLiteral<Black>() {
        });
        // See bean types of a producer field
        assertEquals(blackSheepWsBean.getTypes().size(), 2);
        typeSetMatches(blackSheepWsBean.getTypes(), Object.class, SheepWS.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = { JAVAEE_FULL, JAX_WS })
    @SpecAssertion(section = DECLARING_RESOURCE, id = "ff")
    public void testResourceInvocation(@Black SheepWS sheepWS) {
        assertNotNull(sheepWS);
        assertTrue(sheepWS.isSheepInjected());
    }
}
