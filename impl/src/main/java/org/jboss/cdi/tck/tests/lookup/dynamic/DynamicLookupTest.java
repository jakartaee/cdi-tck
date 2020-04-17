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
package org.jboss.cdi.tck.tests.lookup.dynamic;

import static org.jboss.cdi.tck.cdi.Sections.ANNOTATIONLITERAL_TYPELITERAL;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_INSTANCE;
import static org.jboss.cdi.tck.cdi.Sections.DYNAMIC_LOOKUP;
import static org.jboss.cdi.tck.cdi.Sections.NEW;
import static org.jboss.cdi.tck.cdi.Sections.PROGRAMMATIC_LOOKUP;
import static org.jboss.cdi.tck.cdi.Sections.PROVIDER;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import jakarta.enterprise.inject.AmbiguousResolutionException;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.UnsatisfiedResolutionException;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for dynamic lookup features
 *
 * @author Shane Bryzak
 * @author Jozef Hartinger
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class DynamicLookupTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DynamicLookupTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROGRAMMATIC_LOOKUP, id = "aa") })
    public void testObtainsInjectsInstanceOfInstance() {
        ObtainsInstanceBean injectionPoint = getContextualReference(ObtainsInstanceBean.class);
        assertNotNull(injectionPoint.getPaymentProcessor());
    }

    @Test
    @SpecAssertion(section = DYNAMIC_LOOKUP, id = "da")
    public void testDuplicateBindingsThrowsException() {
        try {
            ObtainsInstanceBean injectionPoint = getContextualReference(ObtainsInstanceBean.class);
            injectionPoint.getAnyPaymentProcessor().select(new PayByBinding(PayBy.PaymentMethod.CASH) {
            }, new PayByBinding(PayBy.PaymentMethod.CREDIT_CARD) {
            });
        } catch (Throwable t) {
            assertTrue(isThrowablePresent(IllegalArgumentException.class, t));
            return;
        }
        Assert.fail();
    }

    @Test
    @SpecAssertion(section = DYNAMIC_LOOKUP, id = "e")
    public void testNonBindingThrowsException() {
        try {
            ObtainsInstanceBean injectionPoint = getContextualReference(ObtainsInstanceBean.class);
            injectionPoint.getAnyPaymentProcessor().select(new AnnotationLiteral<NonBinding>() {
            });
        } catch (Throwable t) {
            assertTrue(isThrowablePresent(IllegalArgumentException.class, t));
            return;
        }
        Assert.fail();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROGRAMMATIC_LOOKUP, id = "ba"), @SpecAssertion(section = PROGRAMMATIC_LOOKUP, id = "ca"),
            @SpecAssertion(section = DYNAMIC_LOOKUP, id = "aa"), @SpecAssertion(section = DYNAMIC_LOOKUP, id = "ab"),
            @SpecAssertion(section = DYNAMIC_LOOKUP, id = "fa"), @SpecAssertion(section = DYNAMIC_LOOKUP, id = "fc") })
    public void testGetMethod() {
        // initial setup of contextual instance
        getContextualReference(AdvancedPaymentProcessor.class, Any.Literal.INSTANCE).setValue(10);

        Instance<AsynchronousPaymentProcessor> instance = getContextualReference(ObtainsInstanceBean.class).getPaymentProcessor();
        assertTrue(instance.get() instanceof AdvancedPaymentProcessor);
        assertEquals(instance.get().getValue(), 10);
    }

    @Test
    @SpecAssertion(section = DYNAMIC_LOOKUP, id = "fba")
    public void testUnsatisfiedDependencyThrowsException() {
        try {
            getContextualReference(ObtainsInstanceBean.class).getPaymentProcessor().select(RemotePaymentProcessor.class).get();
        } catch (Throwable t) {
            assertTrue(isThrowablePresent(UnsatisfiedResolutionException.class, t));
            return;
        }
        Assert.fail();
    }

    @Test
    @SpecAssertion(section = DYNAMIC_LOOKUP, id = "fbb")
    public void testAmbiguousDependencyThrowsException() {
        try {
            getContextualReference(ObtainsInstanceBean.class).getAnyPaymentProcessor().get();
        } catch (Throwable t) {
            assertTrue(isThrowablePresent(AmbiguousResolutionException.class, t));
            return;
        }
        Assert.fail();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DYNAMIC_LOOKUP, id = "aa"), @SpecAssertion(section = DYNAMIC_LOOKUP, id = "ba"),
            @SpecAssertion(section = DYNAMIC_LOOKUP, id = "ia"), @SpecAssertion(section = DYNAMIC_LOOKUP, id = "ib"),
            @SpecAssertion(section = ANNOTATIONLITERAL_TYPELITERAL, id = "a") })
    public void testIteratorMethod() {
        // initial setup of contextual instances
        getContextualReference(AdvancedPaymentProcessor.class, Any.Literal.INSTANCE).setValue(1);
        getContextualReference(RemotePaymentProcessor.class, Any.Literal.INSTANCE).setValue(2);

        Instance<PaymentProcessor> instance = getContextualReference(ObtainsInstanceBean.class).getAnyPaymentProcessor();
        Iterator<AsynchronousPaymentProcessor> iterator1 = instance.select(AsynchronousPaymentProcessor.class).iterator();

        AdvancedPaymentProcessor advanced = null;
        RemotePaymentProcessor remote = null;
        int instances = 0;
        while (iterator1.hasNext()) {
            PaymentProcessor processor = iterator1.next();
            if (processor instanceof AdvancedPaymentProcessor) {
                advanced = (AdvancedPaymentProcessor) processor;
            } else if (processor instanceof RemotePaymentProcessor) {
                remote = (RemotePaymentProcessor) processor;
            } else {
                throw new RuntimeException("Unexpected instance returned by iterator.");
            }
            instances++;
        }

        assertEquals(instances, 2);
        assertNotNull(advanced);
        assertEquals(advanced.getValue(), 1);
        assertNotNull(remote);
        assertEquals(remote.getValue(), 2);

        Iterator<RemotePaymentProcessor> iterator2 = instance.select(RemotePaymentProcessor.class, new PayByBinding(PayBy.PaymentMethod.CREDIT_CARD) {
        }).iterator();

        assertEquals(iterator2.next().getValue(), 2);
        assertFalse(iterator2.hasNext());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DYNAMIC_LOOKUP, id = "ia"), @SpecAssertion(section = DYNAMIC_LOOKUP, id = "ib"),
            @SpecAssertion(section = DYNAMIC_LOOKUP, id = "la"), @SpecAssertion(section = DYNAMIC_LOOKUP, id = "m") })
    public void testAlternatives() {
        Instance<Common> instance = getContextualReference(ObtainsInstanceBean.class).getCommon();
        assertFalse(instance.isAmbiguous());
        Iterator<Common> iterator = instance.iterator();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof Baz);
        assertFalse(iterator.hasNext());

        assertTrue(instance.get().ping());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DYNAMIC_LOOKUP, id = "la"), @SpecAssertion(section = DYNAMIC_LOOKUP, id = "l") })
    public void testIsUnsatisfied() {
        ObtainsInstanceBean injectionPoint = getContextualReference(ObtainsInstanceBean.class);
        assertFalse(injectionPoint.getAnyPaymentProcessor().isUnsatisfied());
        assertTrue(injectionPoint.getPaymentProcessor().select(RemotePaymentProcessor.class).isUnsatisfied());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROGRAMMATIC_LOOKUP, id = "da"), @SpecAssertion(section = DYNAMIC_LOOKUP, id = "la"),
            @SpecAssertion(section = DYNAMIC_LOOKUP, id = "m") })
    public void testIsAmbiguous() {
        ObtainsInstanceBean injectionPoint = getContextualReference(ObtainsInstanceBean.class);
        assertTrue(injectionPoint.getAnyPaymentProcessor().isAmbiguous());
        assertFalse(injectionPoint.getPaymentProcessor().isAmbiguous());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROGRAMMATIC_LOOKUP, id = "e"), @SpecAssertion(section = ANNOTATIONLITERAL_TYPELITERAL, id = "b"),
            @SpecAssertion(section = DYNAMIC_LOOKUP, id = "ma") })
    public void testNewBean() {
        Instance<String> string = getContextualReference(ObtainsNewInstanceBean.class).getString();
        assertFalse(string.isAmbiguous());
        assertFalse(string.isUnsatisfied());
        assertNotNull(string.get());
        assertTrue(string.get() instanceof String);

        Instance<Map<String, String>> map = getContextualReference(ObtainsNewInstanceBean.class).getMap();
        assertTrue(map.isResolvable());
        Map<String, String> instance = map.get();
        assertNotNull(instance);
        assertTrue(instance instanceof HashMap<?, ?>);
    }

    @Test
    @SpecAssertion(section = NEW, id = "xc")
    public void testNewBeanNotEnabledWithouInjectionPoint() {
        assertTrue(getContextualReference(ObtainsNewInstanceBean.class).getIae().isUnsatisfied());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = DYNAMIC_LOOKUP, id = "ja"), @SpecAssertion(section = DYNAMIC_LOOKUP, id = "jb"),
            @SpecAssertion(section = DYNAMIC_LOOKUP, id = "la"), @SpecAssertion(section = DYNAMIC_LOOKUP, id = "ma") })
    public void testStream(Instance<Uncommon> uncommonInstance) {
        assertFalse(uncommonInstance.isResolvable());
        Stream<Uncommon> stream = uncommonInstance.stream();
        assertEquals(stream.count(), 2);
        assertTrue(uncommonInstance.stream().filter(p -> p.getClass().equals(Garply.class)).findFirst().isPresent());
        assertTrue(uncommonInstance.stream().filter(p -> p.getClass().equals(Corge.class)).findFirst().isPresent());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_INSTANCE, id = "a"), @SpecAssertion(section = BM_OBTAIN_INSTANCE, id = "b") })
    public void beanManageCreateInstance() {
        Instance<Object> instance = getCurrentManager().createInstance();
        Instance<AsynchronousPaymentProcessor> asyncProcessors = instance.select(AsynchronousPaymentProcessor.class);
        // there's no AsynchronousPaymentProcessor with default qualifier
        assertTrue(asyncProcessors.isUnsatisfied());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROVIDER, id = "b") })
    public void cdiSelectLookup() {
        Instance<AsynchronousPaymentProcessor> asyncProcessors = CDI.current().select(AsynchronousPaymentProcessor.class);
        // there's no AsynchronousPaymentProcessor with default qualifier
        assertTrue(asyncProcessors.isUnsatisfied());
    }
}
