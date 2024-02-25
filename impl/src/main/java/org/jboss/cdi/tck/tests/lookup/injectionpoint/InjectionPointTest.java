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
package org.jboss.cdi.tck.tests.lookup.injectionpoint;

import static org.jboss.cdi.tck.cdi.Sections.CONTEXTUAL_REFERENCE;
import static org.jboss.cdi.tck.cdi.Sections.INJECTION_POINT;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedParameter;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Injection point metadata tests
 *
 * @author David Allen
 * @author Jozef Hartinger
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class InjectionPointTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InjectionPointTest.class)
                .build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = CONTEXTUAL_REFERENCE, id = "da"),
            @SpecAssertion(section = INJECTION_POINT, id = "aa") })
    public void testGetBean() {

        // Get an instance of the bean which has another bean injected into it
        FieldInjectionPointBean beanWithInjectedBean = getContextualReference(FieldInjectionPointBean.class);
        BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
        assert beanWithInjectionPoint.getInjectedMetadata() != null;

        Set<Bean<FieldInjectionPointBean>> resolvedBeans = getBeans(FieldInjectionPointBean.class);
        assert resolvedBeans.size() == 1;
        assert beanWithInjectionPoint.getInjectedMetadata().getBean().equals(resolvedBeans.iterator().next());
    }

    @Test
    @SpecAssertion(section = CONTEXTUAL_REFERENCE, id = "db")
    public void testNullInjectionPointInjectedIntoNonInjectedObject() {
        Foo foo = getContextualReference(Foo.class);
        assertTrue(foo.isEveryInjectionPointNull(),
                "A non-null value of InjectionPoint injected into a dependent object that is not being injected.");

        // Check for null injection point is done in BazProducer's producer method
        getContextualReference(Baz.class).ping();
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "ba")
    public void testGetType() {
        // Get an instance of the bean which has another bean injected into it
        FieldInjectionPointBean beanWithInjectedBean = getContextualReference(FieldInjectionPointBean.class);
        BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
        assert beanWithInjectionPoint.getInjectedMetadata() != null;
        assert beanWithInjectionPoint.getInjectedMetadata().getType().equals(BeanWithInjectionPointMetadata.class);
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "bc")
    public void testGetBindingTypes() {
        // Get an instance of the bean which has another bean injected into it
        FieldInjectionPointBean beanWithInjectedBean = getContextualReference(FieldInjectionPointBean.class);
        BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
        assert beanWithInjectionPoint.getInjectedMetadata() != null;
        Set<Annotation> bindingTypes = beanWithInjectionPoint.getInjectedMetadata().getQualifiers();
        assert bindingTypes.size() == 1;
        assert Default.class.isAssignableFrom(bindingTypes.iterator().next().annotationType());
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "ca")
    public void testGetMemberField() {
        // Get an instance of the bean which has another bean injected into it
        FieldInjectionPointBean beanWithInjectedBean = getContextualReference(FieldInjectionPointBean.class);
        BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
        assert beanWithInjectionPoint.getInjectedMetadata() != null;
        assert Field.class.isAssignableFrom(beanWithInjectionPoint.getInjectedMetadata().getMember().getClass());
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "cb")
    public void testGetMemberMethod() {
        // Get an instance of the bean which has another bean injected into it
        MethodInjectionPointBean beanWithInjectedBean = getContextualReference(MethodInjectionPointBean.class);
        BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
        assert beanWithInjectionPoint.getInjectedMetadata() != null;
        assert Method.class.isAssignableFrom(beanWithInjectionPoint.getInjectedMetadata().getMember().getClass());

        // Since the type and bindings must correspond to the parameter, check
        // them
        assert beanWithInjectionPoint.getInjectedMetadata().getType().equals(BeanWithInjectionPointMetadata.class);
        assert beanWithInjectionPoint.getInjectedMetadata().getQualifiers().contains(Default.Literal.INSTANCE);
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "cc")
    public void testGetMemberConstructor() {
        // Get an instance of the bean which has another bean injected into it
        ConstructorInjectionPointBean beanWithInjectedBean = getContextualReference(ConstructorInjectionPointBean.class);
        BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
        assert beanWithInjectionPoint.getInjectedMetadata() != null;
        assert Constructor.class.isAssignableFrom(beanWithInjectionPoint.getInjectedMetadata().getMember().getClass());

        // Since the type and bindings must correspond to the parameter, check
        // them
        assert beanWithInjectionPoint.getInjectedMetadata().getType().equals(BeanWithInjectionPointMetadata.class);
        assert beanWithInjectionPoint.getInjectedMetadata().getQualifiers().contains(Default.Literal.INSTANCE);
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "daa")
    public void testGetAnnotatedField() {
        // Get an instance of the bean which has another bean injected into it
        FieldInjectionPointBean beanWithInjectedBean = getContextualReference(FieldInjectionPointBean.class);
        BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
        assert beanWithInjectionPoint.getInjectedMetadata() != null;
        assert beanWithInjectionPoint.getInjectedMetadata().getAnnotated() instanceof AnnotatedField<?>;
        assert beanWithInjectionPoint.getInjectedMetadata().getAnnotated().isAnnotationPresent(Animal.class);
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "daa")
    public void testGetAnnotatedParameter() {
        // Get an instance of the bean which has another bean injected into it
        MethodInjectionPointBean beanWithInjectedBean = getContextualReference(MethodInjectionPointBean.class);
        BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
        assert beanWithInjectionPoint.getInjectedMetadata() != null;
        assert beanWithInjectionPoint.getInjectedMetadata().getAnnotated() instanceof AnnotatedParameter<?>;
        assert annotationSetMatches(beanWithInjectionPoint.getInjectedMetadata().getQualifiers(), Default.class);
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "ea")
    public void testDependentScope() {
        assert getBeans(InjectionPoint.class).size() == 1;
        assert getBeans(InjectionPoint.class).iterator().next().getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "ea")
    public void testApiTypeInjectionPoint() {
        // Get an instance of the bean which has another bean injected into it
        FieldInjectionPointBean beanWithInjectedBean = getContextualReference(FieldInjectionPointBean.class);
        BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
        assert beanWithInjectionPoint.getInjectedMetadata() != null;
        assert InjectionPoint.class.isAssignableFrom(beanWithInjectionPoint.getInjectedMetadata().getClass());
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "ea")
    public void testCurrentBinding() {
        // Get an instance of the bean which has another bean injected into it
        FieldInjectionPointBean beanWithInjectedBean = getContextualReference(FieldInjectionPointBean.class);
        BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
        assert beanWithInjectionPoint.getInjectedMetadata() != null;
        assert beanWithInjectionPoint.getInjectedMetadata().getQualifiers().contains(Default.Literal.INSTANCE);
    }

    @Test
    @SpecAssertion(section = INJECTION_POINT, id = "dca")
    public void testIsTransient() {
        FieldInjectionPointBean bean1 = getContextualReference(FieldInjectionPointBean.class);
        TransientFieldInjectionPointBean bean2 = getContextualReference(TransientFieldInjectionPointBean.class);
        InjectionPoint ip1 = bean1.getInjectedBean().getInjectedMetadata();
        InjectionPoint ip2 = bean2.getInjectedBean().getInjectedMetadata();
        assert !ip1.isTransient();
        assert ip2.isTransient();
    }
}
