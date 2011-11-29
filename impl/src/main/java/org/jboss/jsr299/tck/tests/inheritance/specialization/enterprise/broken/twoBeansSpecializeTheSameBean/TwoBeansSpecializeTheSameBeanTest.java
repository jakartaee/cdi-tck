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
package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise.broken.twoBeansSpecializeTheSameBean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.jsr299.tck.tests.inheritance.specialization.simple.broken.two.TwoSpecializingBeansForOneSpecializedTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;

@SpecVersion(spec = "cdi", version = "20091018")
public class TwoBeansSpecializeTheSameBeanTest extends AbstractJSR299Test {

    @ShouldThrowException(Exception.class)
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(TwoBeansSpecializeTheSameBeanTest.class).withBeansXml("beans.xml")
                .build();
    }

    /**
     * This method tests session beans
     * 
     * @see org.jboss.jsr299.tck.tests.inheritance.specialization.producer.method.broken.twoBeansSpecializeTheSameBean.TwoBeansSpecializeTheSameBeanTest#testTwoBeansSpecializeTheSameBean()
     * @see TwoSpecializingBeansForOneSpecializedTest#testTwoBeansSpecializeTheSameBean()
     * 
     */
    // @Test
    // @SpecAssertion(section = "4.3.3", id = "c") removed from spec
    public void testTwoBeansSpecializeTheSameBean() {
    }
}
