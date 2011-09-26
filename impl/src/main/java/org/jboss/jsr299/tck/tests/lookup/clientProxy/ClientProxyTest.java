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
package org.jboss.jsr299.tck.tests.lookup.clientProxy;

import java.io.IOException;

import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.spi.Context;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class ClientProxyTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ClientProxyTest.class).build();
    }

    @Test
    @SpecAssertion(section = "5.4", id = "b")
    public void testClientProxyUsedForNormalScope() {
        Tuna tuna = getInstanceByType(Tuna.class);
        assert getCurrentConfiguration().getBeans().isProxy(tuna);
    }

    @Test
    @SpecAssertion(section = "5.4", id = "c")
    public void testSimpleBeanClientProxyIsSerializable() throws IOException, ClassNotFoundException {
        TunedTuna tuna = getInstanceByType(TunedTuna.class);
        assert getCurrentConfiguration().getBeans().isProxy(tuna);
        byte[] bytes = serialize(tuna);
        tuna = (TunedTuna) deserialize(bytes);
        assert getCurrentConfiguration().getBeans().isProxy(tuna);
        assert tuna.getState().equals("tuned");
    }

    @Test
    @SpecAssertion(section = "5.4.2", id = "aa")
    public void testClientProxyInvocation() {
        TunedTuna tuna = getInstanceByType(TunedTuna.class);
        assert getCurrentConfiguration().getBeans().isProxy(tuna);
        assert tuna.getState().equals("tuned");
    }

    @Test(expectedExceptions = { ContextNotActiveException.class, IllegalStateException.class })
    @SpecAssertions({ @SpecAssertion(section = "5.4.2", id = "ab"), @SpecAssertion(section = "6.5.4", id = "a") })
    public void testInactiveScope() throws Exception {
        assert getCurrentConfiguration().getContexts().getRequestContext().isActive();
        Context ctx = getCurrentConfiguration().getContexts().getRequestContext();
        setContextInactive(ctx);
        assert !getCurrentConfiguration().getContexts().getRequestContext().isActive();
        try {
            getInstanceByType(TunedTuna.class).getState();
        } finally {
            // need to set request scope active again, some other tests will fail otherwise
            setContextActive(ctx);
        }
    }
}
