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
package org.jboss.cdi.tck.tests.context;

import static org.jboss.cdi.tck.cdi.Sections.ACTIVE_CONTEXT;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_ACTIVE_CONTEXT;
import static org.jboss.cdi.tck.cdi.Sections.BUILTIN_SCOPES;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Context;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "1.1 Final Release")
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
    @SpecAssertions({ @SpecAssertion(section = BUILTIN_SCOPES, id = "a"),@SpecAssertion(section = BUILTIN_SCOPES, id = "ca"),
            @SpecAssertion(section = BM_OBTAIN_ACTIVE_CONTEXT, id = "a") })
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
