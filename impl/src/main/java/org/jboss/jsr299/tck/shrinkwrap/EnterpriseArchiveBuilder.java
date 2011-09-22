package org.jboss.jsr299.tck.shrinkwrap;

import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
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
public class EnterpriseArchiveBuilder extends ArchiveBuilder<EnterpriseArchiveBuilder, EnterpriseArchive>
{

    @Override
    public EnterpriseArchiveBuilder self()
    {
        return this;
    }

    @Override
    public EnterpriseArchive buildInternal()
    {
        EnterpriseArchive enterpriseArchive = ShrinkWrap.create(EnterpriseArchive.class, "test.ear");

        // EJB module - contains test package
        JavaArchive ejbArchive = ShrinkWrap.create(JavaArchive.class, "test.jar");

        processPackages(ejbArchive);
        processClasses(ejbArchive);
        processManifestResources(ejbArchive);
        processResources(ejbArchive);
        processLibraries(enterpriseArchive);

        // Add beans.xml to META-INF
        if (beansXml != null)
        {
            ejbArchive.addAsManifestResource(beansXml.getSource(), beansXml.getTarget());
        } else
        {
            ejbArchive.addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
        }

        enterpriseArchive.addAsModule(ejbArchive);

        return enterpriseArchive;
    }

}
