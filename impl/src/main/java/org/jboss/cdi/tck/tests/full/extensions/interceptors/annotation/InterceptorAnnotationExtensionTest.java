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
package org.jboss.cdi.tck.tests.full.extensions.interceptors.annotation;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.ASSOCIATING_INT_USING_INTERCEPTORS_ANNOTATION;
import static org.testng.Assert.assertEquals;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "interceptors", version = "1.2")
@Test(groups = CDI_FULL)
public class InterceptorAnnotationExtensionTest extends AbstractTest {

    @Inject
    SimpleBean bean;

    @Deployment
    public static WebArchive getDeployment() {
        return new WebArchiveBuilder().withTestClass(InterceptorAnnotationExtensionTest.class)
                .withClasses(SimpleBean.class, BooInterceptor.class, InterceptorsExtension.class, InterceptorsLiteral.class)
                .withExtension(InterceptorsExtension.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL)).build();
    }

    @Test
    @SpecAssertion(section = ASSOCIATING_INT_USING_INTERCEPTORS_ANNOTATION, id = "a")
    public void testInterceptorAddedByExtension() {
        assertEquals("intercepted", bean.simpleMethod());
    }
}
