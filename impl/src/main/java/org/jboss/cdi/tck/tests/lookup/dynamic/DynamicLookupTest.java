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
import static org.jboss.cdi.tck.cdi.Sections.DYNAMIC_LOOKUP;
import static org.jboss.cdi.tck.cdi.Sections.NEW;
import static org.jboss.cdi.tck.cdi.Sections.PROGRAMMATIC_LOOKUP;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.enterprise.inject.AmbiguousResolutionException;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.literals.DefaultLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for dynamic lookup features
 * 
 * @author Shane Bryzak
 * @author Jozef Hartinger
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class DynamicLookupTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DynamicLookupTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROGRAMMATIC_LOOKUP, id = "aa") })
    public void testObtainsInjectsInstanceOfInstance() {
        ObtainsInstanceBean injectionPoint = getContextualReference(ObtainsInstanceBean.class);
        assert injectionPoint.getPaymentProcessor() != null;
    }

    @Test
    @SpecAssertion(section = DYNAMIC_LOOKUP, id = "da")
    public void testDuplicateBindingsThrowsException() {
        try {
            ObtainsInstanceBean injectionPoint = getContextualReference(ObtainsInstanceBean.class);
            injectionPoint.getAnyPaymentProcessor().select(new DefaultLiteral(), new DefaultLiteral());
        } catch (Throwable t) {
            assert isThrowablePresent(IllegalArgumentException.class, t);
            return;
        }
        assert false;
    }

    @Test
    @SpecAssertion(section = DYNAMIC_LOOKUP, id = "e")
    public void testNonBindingThrowsException() {
        try {
            ObtainsInstanceBean injectionPoint = getContextualReference(ObtainsInstanceBean.class);
            injectionPoint.getAnyPaymentProcessor().select(new AnnotationLiteral<NonBinding>() {
            });
        } catch (Throwable t) {
            assert isThrowablePresent(IllegalArgumentException.class, t);
            return;
        }
        assert false;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROGRAMMATIC_LOOKUP, id = "ba"), @SpecAssertion(section = PROGRAMMATIC_LOOKUP, id = "ca"),
            @SpecAssertion(section = DYNAMIC_LOOKUP, id = "aa"), @SpecAssertion(section = DYNAMIC_LOOKUP, id = "ab"),
            @SpecAssertion(section = DYNAMIC_LOOKUP, id = "fa"), @SpecAssertion(section = DYNAMIC_LOOKUP, id = "fc") })
    public void testGetMethod() {
        // initial setup of contextual instance
        getContextualReference(AdvancedPaymentProcessor.class, AnyLiteral.INSTANCE).setValue(10);

        Instance<AsynchronousPaymentProcessor> instance = getContextualReference(ObtainsInstanceBean.class).getPaymentProcessor();
        assert instance.get() instanceof AdvancedPaymentProcessor;
        assert instance.get().getValue() == 10;
    }

    @Test
    @SpecAssertion(section = DYNAMIC_LOOKUP, id = "fba")
    public void testUnsatisfiedDependencyThrowsException() {
        try {
            getContextualReference(ObtainsInstanceBean.class).getPaymentProcessor().select(RemotePaymentProcessor.class).get();
        } catch (Throwable t) {
            assert isThrowablePresent(UnsatisfiedResolutionException.class, t);
            return;
        }
        assert false;
    }

    @Test
    @SpecAssertion(section = DYNAMIC_LOOKUP, id = "fbb")
    public void testAmbiguousDependencyThrowsException() {
        try {
            getContextualReference(ObtainsInstanceBean.class).getAnyPaymentProcessor().get();
        } catch (Throwable t) {
            assert isThrowablePresent(AmbiguousResolutionException.class, t);
            return;
        }
        assert false;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DYNAMIC_LOOKUP, id = "aa"), @SpecAssertion(section = DYNAMIC_LOOKUP, id = "ba"),
            @SpecAssertion(section = DYNAMIC_LOOKUP, id = "ja"), @SpecAssertion(section = DYNAMIC_LOOKUP, id = "ka"),
            @SpecAssertion(section = ANNOTATIONLITERAL_TYPELITERAL, id = "a") })
    public void testIteratorMethod() {
        // initial setup of contextual instances
        getContextualReference(AdvancedPaymentProcessor.class, AnyLiteral.INSTANCE).setValue(1);
        getContextualReference(RemotePaymentProcessor.class, AnyLiteral.INSTANCE).setValue(2);

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

        assert instances == 2;
        assert advanced != null;
        assert advanced.getValue() == 1;
        assert remote != null;
        assert remote.getValue() == 2;

        Iterator<RemotePaymentProcessor> iterator2 = instance.select(RemotePaymentProcessor.class, new PayByBinding() {
            public PaymentMethod value() {
                return PaymentMethod.CREDIT_CARD;
            }
        }).iterator();

        assert iterator2.next().getValue() == 2;
        assert !iterator2.hasNext();
    }

    @Test
    @SpecAssertion(section = DYNAMIC_LOOKUP, id = "l")
    public void testIsUnsatisfied() {
        ObtainsInstanceBean injectionPoint = getContextualReference(ObtainsInstanceBean.class);
        assert !injectionPoint.getAnyPaymentProcessor().isUnsatisfied();
        assert injectionPoint.getPaymentProcessor().select(RemotePaymentProcessor.class).isUnsatisfied();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROGRAMMATIC_LOOKUP, id = "da"), @SpecAssertion(section = DYNAMIC_LOOKUP, id = "m") })
    public void testIsAmbiguous() {
        ObtainsInstanceBean injectionPoint = getContextualReference(ObtainsInstanceBean.class);
        assert injectionPoint.getAnyPaymentProcessor().isAmbiguous();
        assert !injectionPoint.getPaymentProcessor().isAmbiguous();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROGRAMMATIC_LOOKUP, id = "e"), @SpecAssertion(section = ANNOTATIONLITERAL_TYPELITERAL, id = "b") })
    public void testNewBean() {
        Instance<String> string = getContextualReference(ObtainsNewInstanceBean.class).getString();
        assert !string.isAmbiguous();
        assert !string.isUnsatisfied();
        assert string.get() != null;
        assert string.get() instanceof String;

        Instance<Map<String, String>> map = getContextualReference(ObtainsNewInstanceBean.class).getMap();
        assert !map.isAmbiguous();
        assert !map.isUnsatisfied();
        Map<String, String> instance = map.get();
        assert instance != null;
        assert instance instanceof HashMap<?, ?>;
    }

    @Test
    @SpecAssertion(section = NEW, id = "xc")
    public void testNewBeanNotEnabledWithouInjectionPoint() {
        assert getContextualReference(ObtainsNewInstanceBean.class).getIae().isUnsatisfied();
    }
}
