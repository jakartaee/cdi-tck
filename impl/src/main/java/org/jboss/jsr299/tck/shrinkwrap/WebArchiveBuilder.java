package org.jboss.jsr299.tck.shrinkwrap;

import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * Shrinkwrap web archive builder for JSR299 TCK arquillian test. This builder is intended to provide basic functionality
 * covering common TCK needs. Use shrinkwrap API to adapt archive to advanced scenarios.
 * 
 * @author Martin Kouba
 */
public class WebArchiveBuilder extends ArchiveBuilder<WebArchiveBuilder, WebArchive>
{

    @Override
    public WebArchiveBuilder self()
    {
        return this;
    }

    @Override
    public WebArchive buildInternal()
    {

        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "test.war");

        processPackages(webArchive);
        processClasses(webArchive);
        processLibraries(webArchive);
        processManifestResources(webArchive);
        processResources(webArchive);

        if (beansXml != null)
        {
            webArchive.addAsWebInfResource(beansXml.getSource(), beansXml.getTarget());
        } else
        {
            webArchive.addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
        }

        if (webXml != null)
        {
            webArchive.setWebXML(webXml.getSource());
        } else {
            webArchive.setWebXML(new StringAsset("<?xml version=\"1.0\" encoding=\"UTF-8\"?><web-app></web-app>"));
        }

        if (persistenceXml != null)
        {
            webArchive.addAsResource(persistenceXml.getSource(), "META-INF/persistence.xml");
        }

        if (webResources != null)
        {
            for (ResourceDescriptor resource : webResources)
            {
                if (resource.getTarget() == null)
                {
                    webArchive.addAsWebResource(resource.getSource());
                } else
                {
                    webArchive.addAsWebResource(resource.getSource(), resource.getTarget());
                }
            }
        }
        return webArchive;
    }

}
