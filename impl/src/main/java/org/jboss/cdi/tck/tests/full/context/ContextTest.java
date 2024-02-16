/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.context;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.ACTIVE_CONTEXT;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_ACTIVE_CONTEXT;
import static org.jboss.cdi.tck.cdi.Sections.BUILTIN_SCOPES;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.context.spi.Context;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class ContextTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ContextTest.class).withExtension(AfterBeanDiscoveryObserver.class)
                .build();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    @SpecAssertion(section = ACTIVE_CONTEXT, id = "b")
    public void testGetContextWithTooManyActiveContextsFails() {
        getCurrentManager().getContext(DummyScoped.class);
    }

    @Test(expectedExceptions = { ContextNotActiveException.class })
    @SpecAssertion(section = ACTIVE_CONTEXT, id = "a")
    public void testGetContextWithNoRegisteredContextsFails() {
        getCurrentManager().getContext(Unregistered.class);
    }

    @Test
    @SpecAssertion(section = BUILTIN_SCOPES, id = "a")
    @SpecAssertion(section = BUILTIN_SCOPES, id = "ca")
    @SpecAssertion(section = BM_OBTAIN_ACTIVE_CONTEXT, id = "a")
    public void testBuiltInContexts() {
        Context context = getCurrentManager().getContext(Dependent.class);
        assert context != null;
        context = getCurrentManager().getContext(RequestScoped.class);
        assert context != null;
        context = getCurrentManager().getContext(SessionScoped.class);
        assert context != null;
        context = getCurrentManager().getContext(ApplicationScoped.class);
        assert context != null;
        // Can't test conversations here, they are only available for a JSF
        // request. Standalone container only simulates a servlet request
    }
}
