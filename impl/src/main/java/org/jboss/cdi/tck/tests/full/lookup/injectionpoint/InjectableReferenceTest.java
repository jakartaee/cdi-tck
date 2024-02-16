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

package org.jboss.cdi.tck.tests.full.lookup.injectionpoint;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_CREATIONALCONTEXT;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_INJECTABLE_REFERENCE;
import static org.jboss.cdi.tck.cdi.Sections.INJECTABLE_REFERENCE;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InjectionPoint;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * InjectableReference tests for the bean manager. These tests are only here due to the fact that InjectionPoints are always
 * needed too.
 * 
 * @author David Allen
 * 
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = CDI_FULL)
public class InjectableReferenceTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InjectableReferenceTest.class)
                .withBeansXml(new BeansXml().decorators(TimestampLogger.class))
                .build();
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_INJECTABLE_REFERENCE, id = "a"), @SpecAssertion(section = BM_OBTAIN_INJECTABLE_REFERENCE, id = "ab"),
            @SpecAssertion(section = BM_OBTAIN_CREATIONALCONTEXT, id = "a"), @SpecAssertion(section = INJECTABLE_REFERENCE, id = "a") })
    public void testGetInjectableReferenceOnBeanManager() {

        BeanWithInjectionPointMetadata.reset();

        // Get an instance of the bean which has another bean injected into it
        FieldInjectionPointBean beanWithInjectedBean = getContextualReference(FieldInjectionPointBean.class);
        BeanWithInjectionPointMetadata beanWithInjectionPoint = beanWithInjectedBean.getInjectedBean();
        InjectionPoint ip = beanWithInjectionPoint.getInjectedMetadata();
        assert ip != null;
        CreationalContext<BeanWithInjectionPointMetadata> creationalContext = getCurrentManager().createCreationalContext(
                (Bean<BeanWithInjectionPointMetadata>) ip.getBean());
        Object beanInstance = getCurrentManager().getInjectableReference(ip, creationalContext);
        assert beanInstance instanceof BeanWithInjectionPointMetadata;

        // The second parameter is an instance of may be used to destroy any object with scope @Dependent that is created
        Bean<BeanWithInjectionPointMetadata> bean = getUniqueBean(BeanWithInjectionPointMetadata.class);
        bean.destroy((BeanWithInjectionPointMetadata) beanInstance, creationalContext);
        assert BeanWithInjectionPointMetadata.isDestroyed();
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_INJECTABLE_REFERENCE, id = "b") })
    public void testGetInjectableReferenceReturnsDelegateForDelegateInjectionPoint() {
        // Get hold of the correct IP by inspecting the ones the container created for LoggerConsumer
        assert getBeans(LoggerConsumer.class).size() == 1;
        Bean<LoggerConsumer> bean = getBeans(LoggerConsumer.class).iterator().next();
        InjectionPoint loggerInjectionPoint = null;
        for (InjectionPoint ip : bean.getInjectionPoints()) {
            if (ip.getAnnotated().getTypeClosure().contains(Logger.class) && ip.getQualifiers().size() == 1
                    && ip.getQualifiers().contains(Default.Literal.INSTANCE)) {
                loggerInjectionPoint = ip;
            }
        }

        // Now lookup an injectable reference and check that it is of type Logger
        CreationalContext<Logger> creationalContext = getCurrentManager().createCreationalContext(
                (Bean<Logger>) loggerInjectionPoint.getBean());
        Object injectedDelegateLogger = getCurrentManager().getInjectableReference(loggerInjectionPoint, creationalContext);
        assert injectedDelegateLogger instanceof Logger;
        Logger logger = (Logger) injectedDelegateLogger;

        // Use the logger
        String message = "foo123";
        TimestampLogger.reset();
        logger.log(message);
        assert message.equals(TimestampLogger.getLoggedMessage());
    }
}
