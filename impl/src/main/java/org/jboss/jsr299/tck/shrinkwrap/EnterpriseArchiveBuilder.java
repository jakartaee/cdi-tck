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

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.shrinkwrap.descriptor.api.spec.se.manifest.ManifestDescriptor;

/**
 * Shrinkwrap enterprise archive builder for JSR299 TCK arquillian test. This builder is intended to provide basic functionality
 * covering common TCK needs. Use shrinkwrap API to adapt archive to advanced scenarios.
 * <p>
 * Test classes are added to EJB module. The default archive name defined with {@link #withName(String)} is used for this EJB
 * module not EAR itself.
 * </p>
 * <h2>Default and custom web module</h2>
 * <p>
 * The default web module is defined in order to build portable EE archive - it has to contain Class-Path entry in the
 * MANIFEST.MF to state module dependency (see Class Loading Requirements in Java EE spec). Custom web module is not supported
 * directly however its possible to build such module with shrinkwrap API and add it to final enterprise archive. It is
 * necessary to omit default web module using {@link #noDefaultWebModule()} as long as custom web module is used since
 * arquillian cannot handle in-container test packaged as EAR with multiple web modules at the moment (cannot determine which
 * web module to enrich).
 * </p>
 * <h2>application.xml</h2>
 * <p>
 * The <code>application.xml</code> descriptor is not supported as it is no longer required (Java EE 5, Java EE 6). Use
 * shrinkwrap API to add custom <code>application.xml</code> descriptor.
 * </p>
 * <h2>Use case</h2>
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

    public static final String DEFAULT_EAR_NAME = "test.ear";

    public static final String DEFAULT_EJB_MODULE_NAME = "test.jar";

    private boolean hasDefaultWebModule = true;

    /**
     * Do not add default web module.
     * 
     * @return self
     */
    public EnterpriseArchiveBuilder noDefaultWebModule() {
        this.hasDefaultWebModule = false;
        return this;
    }

    @Override
    public EnterpriseArchiveBuilder self() {
        return this;
    }

    @Override
    public EnterpriseArchive buildInternal() {

        EnterpriseArchive enterpriseArchive = ShrinkWrap.create(EnterpriseArchive.class, DEFAULT_EAR_NAME);

        // EJB module - contains test package
        JavaArchive ejbArchive = null;

        if (getName() == null) {
            ejbArchive = ShrinkWrap.create(JavaArchive.class, DEFAULT_EJB_MODULE_NAME);
        } else {
            ejbArchive = ShrinkWrap.create(JavaArchive.class, getName());
        }

        // CDITCK-56
        ejbArchive.addClass(Dummy.class);

        processPackages(ejbArchive);
        processClasses(ejbArchive);
        processManifestResources(ejbArchive);
        processResources(ejbArchive);
        processLibraries(enterpriseArchive);

        if (beansDescriptor != null) {
            ejbArchive.addAsManifestResource(new StringAsset(beansDescriptor.exportAsString()),
                    beansDescriptor.getDescriptorName());
        } else if (beansXml != null) {
            ejbArchive.addAsManifestResource(beansXml.getSource(), beansXml.getTarget());
        } else {
            ejbArchive.addAsManifestResource(new StringAsset(Descriptors.create(BeansDescriptor.class).exportAsString()),
                    "beans.xml");
        }

        if (persistenceDescriptor != null) {
            ejbArchive.addAsManifestResource(new StringAsset(persistenceDescriptor.exportAsString()), "persistence.xml");
        }

        enterpriseArchive.addAsModule(ejbArchive);

        // Default web module
        if (this.hasDefaultWebModule) {

            WebArchive webArchive = ShrinkWrap.create(WebArchive.class).setManifest(
                    new StringAsset(Descriptors.create(ManifestDescriptor.class)
                            .addToClassPath(getName() != null ? getName() : DEFAULT_EJB_MODULE_NAME).exportAsString()));
            enterpriseArchive.addAsModule(webArchive);
        }
        return enterpriseArchive;
    }
}
