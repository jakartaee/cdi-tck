/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.build.compatible.extensions.customContextForRequestScope;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertThrows;

import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.spi.Context;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class CustomRequestContextTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(CustomRequestContextTest.class)
                .withBuildCompatibleExtension(CustomRequestContextExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.DISCOVERY_PHASE, id = "a", note = "Register custom context for the request scope")
    public void test() {
        // deactivate the built-in request context
        setContextInactive(getCurrentBeanContainer().getContext(RequestScoped.class));

        CustomRequestContext customRequestContext = null;
        for (Context context : getCurrentBeanContainer().getContexts(RequestScoped.class)) {
            if (context instanceof CustomRequestContext crc) {
                customRequestContext = crc;
                break;
            }
        }
        assertNotNull(customRequestContext);

        MyService myService = getContextualReference(MyService.class);

        assertThrows(ContextNotActiveException.class, myService::hello);

        customRequestContext.activate();

        assertEquals(myService.hello(), "hello");

        customRequestContext.deactivate();

        assertThrows(ContextNotActiveException.class, myService::hello);
    }
}
