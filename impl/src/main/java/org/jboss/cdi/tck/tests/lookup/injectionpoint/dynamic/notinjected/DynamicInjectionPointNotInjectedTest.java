/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.lookup.injectionpoint.dynamic.notinjected;

import static org.jboss.cdi.tck.cdi.Sections.INJECTION_POINT;
import static org.jboss.cdi.tck.util.Assert.assertAnnotationsMatch;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "5.0")
public class DynamicInjectionPointNotInjectedTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DynamicInjectionPointNotInjectedTest.class).build();
    }

    @Inject
    Bar bar;

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "baa")
    public void testInjectionPointGetType() {
        assertEquals(bar.getFoo().getInjectionPoint().getType(), Foo.class);
        assertEquals(bar.getNiceFooByType().getInjectionPoint().getType(), NiceFoo.class);
        assertEquals(bar.getNiceFooByQualifier().getInjectionPoint().getType(), Foo.class);
        assertEquals(bar.getNiceFooByAllQualifiers().getInjectionPoint().getType(), Foo.class);
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "bca")
    public void testInjectionPointGetQualifiers() {
        assertAnnotationsMatch(bar.getFoo().getInjectionPoint().getQualifiers(), Default.Literal.INSTANCE);
        assertAnnotationsMatch(bar.getNiceFooByType().getInjectionPoint().getQualifiers(), Any.Literal.INSTANCE);
        assertAnnotationsMatch(bar.getNiceFooByQualifier().getInjectionPoint().getQualifiers(), new Nice.Literal());
        assertAnnotationsMatch(bar.getNiceFooByAllQualifiers().getInjectionPoint().getQualifiers(), Any.Literal.INSTANCE,
                new Nice.Literal());
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "aac")
    public void testInjectionPointGetBean() {
        assertNull(bar.getFoo().getInjectionPoint().getBean());
        assertNull(bar.getNiceFooByType().getInjectionPoint().getBean());
        assertNull(bar.getNiceFooByQualifier().getInjectionPoint().getBean());
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "cd")
    public void testInjectionPointGetMember() {
        assertNull(bar.getFoo().getInjectionPoint().getMember());
        assertNull(bar.getNiceFooByType().getInjectionPoint().getMember());
        assertNull(bar.getNiceFooByQualifier().getInjectionPoint().getMember());
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "dac")
    public void testInjectionPointGetAnnotated() {
        assertNull(bar.getFoo().getInjectionPoint().getAnnotated());
        assertNull(bar.getNiceFooByType().getInjectionPoint().getAnnotated());
        assertNull(bar.getNiceFooByQualifier().getInjectionPoint().getAnnotated());
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "dbb")
    public void testInjectionPointIsDelegate() {
        assertFalse(bar.getFoo().getInjectionPoint().isDelegate());
        assertFalse(bar.getNiceFooByType().getInjectionPoint().isDelegate());
        assertFalse(bar.getNiceFooByQualifier().getInjectionPoint().isDelegate());
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "dcc")
    public void testInjectionPointIsTransient() {
        assertFalse(bar.getFoo().getInjectionPoint().isTransient());
        assertFalse(bar.getNiceFooByType().getInjectionPoint().isTransient());
        assertFalse(bar.getNiceFooByQualifier().getInjectionPoint().isTransient());
    }
}
