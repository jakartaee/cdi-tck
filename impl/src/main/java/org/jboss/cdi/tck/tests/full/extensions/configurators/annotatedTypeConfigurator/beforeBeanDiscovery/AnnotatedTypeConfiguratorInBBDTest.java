/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.configurators.annotatedTypeConfigurator.beforeBeanDiscovery;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BEFORE_BEAN_DISCOVERY;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@Test(groups = CDI_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class AnnotatedTypeConfiguratorInBBDTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AnnotatedTypeConfiguratorInBBDTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withExtensions(BBDObservingExtension.class)
                .build();
    }
    
    @Inject
    @Any
    Instance<Object> instance;
    
    @Test
    @SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "c")
    public void testQualifierAddition() {
        Instance<Foo> fooInstance = instance.select(Foo.class, CustomQualifier.CustomQualifierLiteral.INSTANCE);
        Assert.assertFalse(fooInstance.isUnsatisfied());
        Assert.assertNotNull(fooInstance.get());
    }
    
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "d")
    public void testInterceptorBindingAddition(@Any Foo foo) {
        foo.shouldTriggerInterceptor();
        Assert.assertTrue(FooInterceptor.interceptorInvoked);
    }
}
