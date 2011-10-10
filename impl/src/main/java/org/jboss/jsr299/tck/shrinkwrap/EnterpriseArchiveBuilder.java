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
package org.jboss.jsr299.tck.shrinkwrap;

import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * Shrinkwrap enterprise archive builder for JSR299 TCK arquillian test. This builder is intended to provide basic functionality
 * covering common TCK needs. Use shrinkwrap API to adapt archive to advanced scenarios.
 * <p>
 * Test classes are added to EJB module. Web module is not supported yet. The <code>application.xml</code> descriptor is not
 * supported as it is no longer required (Java EE 5, Java EE 6).
 * </p>
 * <p>
 * Use enterprise in TCK tests archive only while:
 * </p>
 * <ul>
 * <li>explicitly testing EAR structure</li>
 * <li>testing Java EE full profile (e.g. EJB timer service, MDBs)</li>
 * </ul>
 * 
 * @author Martin Kouba
 */
public class EnterpriseArchiveBuilder extends ArchiveBuilder<EnterpriseArchiveBuilder, EnterpriseArchive> {

    @Override
    public EnterpriseArchiveBuilder self() {
        return this;
    }

    @Override
    public EnterpriseArchive buildInternal() {
        EnterpriseArchive enterpriseArchive = ShrinkWrap.create(EnterpriseArchive.class, "test.ear");

        // EJB module - contains test package
        JavaArchive ejbArchive = ShrinkWrap.create(JavaArchive.class, "test.jar");

        ejbArchive.addClass(Dummy.class);

        processPackages(ejbArchive);
        processClasses(ejbArchive);
        processManifestResources(ejbArchive);
        processResources(ejbArchive);
        processLibraries(enterpriseArchive);

        // Add beans.xml to META-INF
        if (beansDescriptor != null) {
            ejbArchive.addAsManifestResource(new StringAsset(beansDescriptor.exportAsString()),
                    beansDescriptor.getDescriptorName());
        } else if (beansXml != null) {
            ejbArchive.addAsManifestResource(beansXml.getSource(), beansXml.getTarget());
        } else {
            ejbArchive.addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
        }

        enterpriseArchive.addAsModule(ejbArchive);

        return enterpriseArchive;
    }

}
