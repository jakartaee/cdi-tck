package org.jboss.cdi.tck.shrinkwrap;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * A JavaArchive builder extension for CDI-lite deployments. This makes use of the {@link ArchiveBuilder#processSources(Archive)}
 * phase
 */
public class JarArchiveBuilder extends ArchiveBuilder<JarArchiveBuilder, JavaArchive> {
    @Override
    public JarArchiveBuilder self() {
        return this;
    }


    @Override
    protected JavaArchive buildInternal() {
        JavaArchive jar = null;

        if (getName() == null) {
            // Let arquillian generate archive name in order to avoid reload issues in AS7 (AS7-1638)
            String hash = getSha1OfTestClass();
            if (hash != null) {
                jar = ShrinkWrap.create(JavaArchive.class, hash + ".jar");
            } else {
                jar = ShrinkWrap.create(JavaArchive.class);
            }
        } else {
            jar = ShrinkWrap.create(JavaArchive.class, getName());
        }

        processPackages(jar);
        processClasses(jar);
        processSources(jar);
        processManifestResources(jar);
        processResources(jar);
        return jar;
    }
}
