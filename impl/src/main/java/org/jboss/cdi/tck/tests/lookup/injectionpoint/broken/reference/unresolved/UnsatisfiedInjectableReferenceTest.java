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

package org.jboss.cdi.tck.tests.lookup.injectionpoint.broken.reference.unresolved;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests retrieving an injectable reference for a bean which cannot be resolved.
 * 
 * @author David Allen
 * 
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class UnsatisfiedInjectableReferenceTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(UnsatisfiedInjectableReferenceTest.class).build();
    }

    @SuppressWarnings("unchecked")
    @Test(expectedExceptions = UnsatisfiedResolutionException.class)
    @SpecAssertions({ @SpecAssertion(section = "11.3.3", id = "bb") })
    public void testUnsatisfiedReference() {
        Bean<SimpleBean> bean = this.getBeans(SimpleBean.class).iterator().next();
        UnsatisfiedInjectionPoint injectionPoint = new UnsatisfiedInjectionPoint(bean);
        CreationalContext<SimpleBean> creationalContext = getCurrentManager().createCreationalContext(
                (Bean<SimpleBean>) injectionPoint.getBean());
        getCurrentManager().getInjectableReference(injectionPoint, creationalContext);
    }
}
