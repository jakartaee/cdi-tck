package org.jboss.cdi.tck.shrinkwrap;

import java.util.Map;

import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ArchiveAsset;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.ClassAsset;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.shrinkwrap.impl.base.path.BasicPath;

/**
 * A utility class that can convert a {@link org.jboss.shrinkwrap.api.spec.WebArchive} into a {@link org.jboss.shrinkwrap.api.spec.JavaArchive}.
 * This is useful for CDI-lite containers that cannot deploy a WebArchive. May need to move this into arquillian framework
 * and automatically convert based on the container configuration.
 */
public class ArchiveConverter {

    public static JavaArchive toJar(WebArchive war) {
        String name = war.getName();
        if(name.endsWith(".war")) {
            name = name.substring(0, name.length()-4);
        }
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, name + ".jar");
        Map<ArchivePath, Node> warContent =  war.getContent();
        for (ArchivePath apath: warContent.keySet()) {
            Node node = warContent.get(apath);
            Asset asset = node.getAsset();
            if(apath.get().startsWith("/WEB-INF/classes")) {
                apath = new BasicPath(apath.get().substring(16));
            }

            if(asset == null) {
                // Ignore
                // System.out.printf("Ignoring null assent: %s\n", apath.get());
            }
            else if(asset instanceof ArchiveAsset) {
                unpackLibrary((ArchiveAsset) asset, jar);
            } else if(asset instanceof ClassAsset) {
                jar.add(asset, apath);
            } else if(asset instanceof ClassLoaderAsset) {
                jar.add(asset, apath);
            } else if(asset instanceof BeansXml) {
                jar.add(asset, "META-INF/beans.xml");
            } else if(asset instanceof StringAsset) {
                if(!apath.get().equals("/WEB-INF/web.xml")) {
                    jar.add(asset, apath);
                }
            } else {
                throw new IllegalArgumentException("Unhandled asset type: "+asset.getClass());
            }
        }
        return jar;
    }
    static void unpackLibrary(ArchiveAsset lib, JavaArchive jar) {
        jar.merge(lib.getArchive());
    }
}
