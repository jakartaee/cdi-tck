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

package org.jboss.jsr299.tck.tests.lookup.injectionpoint;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.DefaultLiteral;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
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
@SpecVersion(spec = "cdi", version = "20091101")
public class InjectableReferenceTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InjectableReferenceTest.class)
                .withBeansXml(Descriptors.create(BeansDescriptor.class).createDecorators().clazz(TimestampLogger.class.getName()).up()).build();
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = "11.3.3", id = "a"), @SpecAssertion(section = "11.3.3", id = "ab"),
            @SpecAssertion(section = "11.3.4", id = "a"), @SpecAssertion(section = "6.5.5", id = "a") })
    public void testGetInjectableReferenceOnBeanManager() {

        BeanWithInjectionPointMetadata.reset();

        // Get an instance of the bean which has another bean injected into it
        FieldInjectionPointBean beanWithInjectedBean = getInstanceByType(FieldInjectionPointBean.class);
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
    @SpecAssertions({ @SpecAssertion(section = "11.3.3", id = "b") })
    public void testGetInjectableReferenceReturnsDelegateForDelegateInjectionPoint() {
        // Get hold of the correct IP by inspecting the ones the container created for LoggerConsumer
        assert getBeans(LoggerConsumer.class).size() == 1;
        Bean<LoggerConsumer> bean = getBeans(LoggerConsumer.class).iterator().next();
        InjectionPoint loggerInjectionPoint = null;
        for (InjectionPoint ip : bean.getInjectionPoints()) {
            if (ip.getAnnotated().getTypeClosure().contains(Logger.class) && ip.getQualifiers().size() == 1
                    && ip.getQualifiers().contains(new DefaultLiteral())) {
                loggerInjectionPoint = ip;
            }
        }

        // Now lookup an injectable reference and check that it is of type Logger
        CreationalContext<Logger> creationalContext = getCurrentManager().createCreationalContext(
                (Bean<Logger>) loggerInjectionPoint.getBean());
        Object injectedDelegateLogger = getCurrentManager().getInjectableReference(loggerInjectionPoint, creationalContext);
        assert injectedDelegateLogger instanceof Logger;
        Logger logger = (Logger) injectedDelegateLogger;

        // User the logger
        String message = "foo123";
        TimestampLogger.reset();
        logger.log(message);
        assert message.equals(TimestampLogger.getLoggedMessage());
    }
}
