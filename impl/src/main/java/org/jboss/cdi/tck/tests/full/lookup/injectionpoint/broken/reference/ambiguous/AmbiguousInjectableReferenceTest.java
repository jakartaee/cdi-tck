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

package org.jboss.cdi.tck.tests.full.lookup.injectionpoint.broken.reference.ambiguous;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_INJECTABLE_REFERENCE;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.AmbiguousResolutionException;
import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests a custom injection point that resolves to two different bean instances which should always result in an exception.
 *
 * @author David Allen
 *
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class AmbiguousInjectableReferenceTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AmbiguousInjectableReferenceTest.class).build();
    }

    @SuppressWarnings("unchecked")
    @Test(groups = CDI_FULL, expectedExceptions = AmbiguousResolutionException.class)
    @SpecAssertions({ @SpecAssertion(section = BM_OBTAIN_INJECTABLE_REFERENCE, id = "bc") })
    public void testUnsatisfiedReference() {
        Bean<SimpleBean> bean = this.getBeans(SimpleBean.class).iterator().next();
        AmbiguousInjectionPoint injectionPoint = new AmbiguousInjectionPoint(bean);
        CreationalContext<SimpleBean> creationalContext = getCurrentManager().createCreationalContext(
                (Bean<SimpleBean>) injectionPoint.getBean());
        getCurrentManager().getInjectableReference(injectionPoint, creationalContext);
    }
}
