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

package org.jboss.weld.lang.model.tck;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.lang.model.declarations.ClassInfo;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.cdi.lang.model.tck.LangModelVerifier;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * <p>
 * Executes CDI TCK for language model used in CDI Lite, current setup requires discovery mode ALL plus adding
 * {@link LangModelVerifier} into the deployment to discover it as a bean. Alternatively, this could be added
 * synthetically inside {@link LangModelExtension}.
 * </p>
 *
 * <p>
 * Actual test happens inside {@link LangModelExtension} by calling {@link LangModelVerifier#verify(ClassInfo)}.
 * </p>
 */
@RunWith(Arquillian.class)
public class LangModelTckTest {

    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class, LangModelTckTest.class.getSimpleName() + ".war")
                // beans.xml with discovery mode "all"
                .addAsWebInfResource(new BeansXml(BeanDiscoveryMode.ANNOTATED), "beans.xml")
                .addAsServiceProvider(BuildCompatibleExtension.class, LangModelExtension.class)
                .addClasses(LangModelExtension.class)
                // add this package into the deployment so that it's subject to discovery, including its dependencies
                .addPackage(LangModelVerifier.class.getPackage());
    }

    @Test
    public void testLangModel() {
        // test is executed in LangModelExtension; here we just assert that the relevant extension method was invoked
        Assert.assertTrue(LangModelExtension.ENHANCEMENT_INVOKED == 1);
    }
}
