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
package org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.Any;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class ProducerMethodSpecializationTest extends AbstractJSR299Test {

    private static Annotation EXPENSIVE_LITERAL = new AnnotationLiteral<Expensive>() {
    };
    private static Annotation SPARKLY_LITERAL = new AnnotationLiteral<Sparkly>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProducerMethodSpecializationTest.class).withBeansXml("beans.xml")
                .build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "5.5.4", id = "c"), @SpecAssertion(section = "3.3.3", id = "aa") })
    public void testSpecializingBeanHasBindingsOfSpecializedAndSpecializingBean() {
        assert getCurrentManager().getBeans(Necklace.class, EXPENSIVE_LITERAL, SPARKLY_LITERAL).size() == 1;
        assert getCurrentManager().getBeans(Necklace.class, EXPENSIVE_LITERAL, SPARKLY_LITERAL).iterator().next()
                .getQualifiers().size() == 4;
        assert annotationSetMatches(getCurrentManager().getBeans(Necklace.class, EXPENSIVE_LITERAL, SPARKLY_LITERAL).iterator()
                .next().getQualifiers(), Expensive.class, Sparkly.class, Any.class, Named.class);
        assert getBeans(Necklace.class, new AnnotationLiteral<Sparkly>() {
        }).size() == 1;
        assert getBeans(Necklace.class, new AnnotationLiteral<Sparkly>() {
        }).iterator().next().getName().equals("expensiveGift");
        Product product = getInstanceByType(Product.class, EXPENSIVE_LITERAL, SPARKLY_LITERAL);
        assert product instanceof Necklace;
    }

}
