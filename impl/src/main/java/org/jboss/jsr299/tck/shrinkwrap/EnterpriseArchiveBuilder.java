package org.jboss.jsr299.tck.shrinkwrap;

import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * Shrinkwrap enterprise archive builderf for JSR299 TCK arquillian test.
 * 
 * Use enterprise archive while:
 * <ul>
 * <li>explicitly testing EAR structure</li>
 * <li>testing Java EE full profile (e.g. EJB timer service, MDBs)</li>
 * </ul>
 * 
 * Web module is not supported yet.
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
