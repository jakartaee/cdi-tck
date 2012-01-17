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
package org.jboss.cdi.tck.tests.implementation.producer.method.definition.enterprise;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;

import java.util.Set;

import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class EnterpriseProducerMethodDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseProducerMethodDefinitionTest.class).build();
    }

    @Test(groups = INTEGRATION, expectedExceptions = UnsatisfiedResolutionException.class)
    @SpecAssertion(section = "4.2", id = "dd")
    public void testNonStaticProducerMethodNotInheritedBySpecializingSubclass() {
        assert getBeans(Egg.class, new AnnotationLiteral<Yummy>() {
        }).size() == 0;
        getInstanceByType(Egg.class, new AnnotationLiteral<Yummy>() {
        }).getMother();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = "4.2", id = "dd")
    public void testNonStaticProducerMethodNotInherited() {
        assert getBeans(Apple.class, new AnnotationLiteral<Yummy>() {
        }).size() == 1;
        assert getInstanceByType(Apple.class, new AnnotationLiteral<Yummy>() {
        }).getTree() instanceof AppleTreeLocal;
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = "4.2", id = "dj")
    public void testNonStaticProducerMethodNotIndirectlyInherited() {
        Set<Bean<Pear>> beans = getBeans(Pear.class, new AnnotationLiteral<Yummy>() {
        });
        assert beans.size() == 2;
    }
}
