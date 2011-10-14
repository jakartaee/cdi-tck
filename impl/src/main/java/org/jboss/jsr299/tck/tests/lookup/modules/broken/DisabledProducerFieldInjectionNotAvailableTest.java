/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.jsr299.tck.tests.lookup.modules.broken;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.EnterpriseArchiveBuilder;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.se.manifest.ManifestDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test that bean in web module cannot inject disabled producer field from EJB module.
 * 
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class DisabledProducerFieldInjectionNotAvailableTest extends AbstractJSR299Test {

    @ShouldThrowException(Exception.class)
    @Deployment
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder().noDefaultWebModule()
                .withTestClassDefinition(DisabledProducerFieldInjectionNotAvailableTest.class)
                .withClasses(DisabledFooFieldProducer.class, BrokenProducedFoo.class).withBeanLibrary(BrokenFoo.class, BrokenBar.class).build();

        WebArchive webArchive = new WebArchiveBuilder().notTestArchive().withClasses(BrokenWebBar.class).build();
        webArchive.setManifest(new StringAsset(Descriptors.create(ManifestDescriptor.class)
                .addToClassPath(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME).exportAsString()));
        enterpriseArchive.addAsModule(webArchive);

        return enterpriseArchive;
    }

    @Test(groups = "javaee-full-only")
    @SpecAssertion(section = "5.1.4", id = "h")
    public void testInjection() throws Exception {
    }

}
