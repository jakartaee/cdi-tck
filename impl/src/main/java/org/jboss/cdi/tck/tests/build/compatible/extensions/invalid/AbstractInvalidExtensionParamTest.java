/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.build.compatible.extensions.invalid;

import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.impl.BeansXml;

public abstract class AbstractInvalidExtensionParamTest extends AbstractTest {

    /**
     * Prepared archive builder minus the extension being tested
     */
    static WebArchiveBuilder prepareArchiveBuilder() {
        return new WebArchiveBuilder()
                // add some class that will be type-discovered
                .withClasses(SomeBean.class)
                .withTestClass(AbstractInvalidExtensionParamTest.class)
                // annotated discovery mode
                .withBeansXml(new BeansXml());
    }


}
