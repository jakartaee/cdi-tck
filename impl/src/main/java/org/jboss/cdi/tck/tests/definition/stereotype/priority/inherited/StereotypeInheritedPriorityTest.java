/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.definition.stereotype.priority.inherited;

import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.testng.annotations.Test;

public class StereotypeInheritedPriorityTest extends AbstractTest {

    @Deployment
    public static WebArchive deploy() {
        return new WebArchiveBuilder().withTestClassPackage(StereotypeInheritedPriorityTest.class)
                .withBeansXml(new BeansXml())
                .build();
    }

    @Inject
    BeanManager bm;

    @Test
    public void testPriorityWasInherited() {
        // if the inheritance works, FooAlternative will inherit the priority and therefore will be enabled
        Instance<FooAlternative> fooInstance = bm.createInstance().select(FooAlternative.class);
        assertTrue(fooInstance.isResolvable());
    }
}
