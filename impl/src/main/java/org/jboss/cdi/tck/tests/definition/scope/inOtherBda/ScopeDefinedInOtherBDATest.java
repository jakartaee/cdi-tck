/*
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.definition.scope.inOtherBda;

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_BEAN_SCOPE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class ScopeDefinedInOtherBDATest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        JavaArchive lib = ShrinkWrap.create(JavaArchive.class).addClass(ThirdPartyScope.class);
        return new WebArchiveBuilder().withTestClass(ScopeDefinedInOtherBDATest.class).withClasses(ThirdPartyScopeBean.class).withLibraries(lib).build();

    }

    @Test
    @SpecAssertion(section = DECLARING_BEAN_SCOPE, id = "a")
    public void testCustomScopeInOtherBDAisBeanDefiningAnnotation() {
        Bean<ThirdPartyScopeBean> thirdPartyScopeBean = getUniqueBean(ThirdPartyScopeBean.class);
        assertNotNull(thirdPartyScopeBean);
        assertEquals(thirdPartyScopeBean.getScope(), ThirdPartyScope.class);
    }
}
