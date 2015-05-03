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
package org.jboss.cdi.tck.tests.extensions.alternative.metadata.interceptor;

import static org.jboss.cdi.tck.cdi.Sections.ALTERNATIVE_METADATA_SOURCES;
import static org.testng.Assert.assertEquals;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanAttributes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.InjectionTargetFactory;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ForwardingAnnotatedType;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "1.1 Final Release")
public class AlternativeMetadataInterceptorInjectionTargetTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(AlternativeMetadataInterceptorInjectionTargetTest.class)
                .withClasses(Login.class, LoginInterceptor.class, LoginInterceptorBinding.class, Secured.class)
                .withBeansXml(Descriptors.create(BeansDescriptor.class).getOrCreateInterceptors().clazz(LoginInterceptor.class.getName()).up()).build();
    }

    @Inject
    private BeanManager manager;

    @Test
    @SpecAssertions({ @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES, id = "p") })
    public void testInterceptorInterceptsOnlyBindedClass() {
        final AnnotatedType<Login> interceptedLogin = manager.createAnnotatedType(Login.class);
        AnnotatedType<Login> modifiedInterceptedLogin = new ForwardingAnnotatedType<Login>() {
            @SuppressWarnings("serial")
            private final AnnotationLiteral<LoginInterceptorBinding> interceptorBinding = new AnnotationLiteral<LoginInterceptorBinding>() {
            };
            @SuppressWarnings("serial")
            private final AnnotationLiteral<Secured> securedAnnotation = new AnnotationLiteral<Secured>() {
            };

            @Override
            public AnnotatedType<Login> delegate() {
                return interceptedLogin;
            }

            @Override
            public Set<Annotation> getAnnotations() {
                Set<Annotation> annotations = new HashSet<Annotation>();
                annotations.add(interceptorBinding);
                annotations.add(securedAnnotation);
                return annotations;
            }

        };

        BeanAttributes<Login> beanAttributes = manager.createBeanAttributes(modifiedInterceptedLogin);
        InjectionTargetFactory<Login> factory = manager.getInjectionTargetFactory(modifiedInterceptedLogin);
        Bean<Login> bean = manager.createBean(beanAttributes, Login.class, factory);
        InjectionTarget<Login> it = manager.createInjectionTarget(modifiedInterceptedLogin);
        CreationalContext<Login> ctx = manager.createCreationalContext(bean);
        Login login = it.produce(ctx);
        assertEquals(login.login(), "intercepted");
    }

}
