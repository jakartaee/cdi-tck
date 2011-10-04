package org.jboss.jsr299.tck.shrinkwrap;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.api.JSR299Configuration;
import org.jboss.jsr299.tck.impl.ConfigurationFactory;
import org.jboss.jsr299.tck.impl.JSR299ConfigurationImpl;
import org.jboss.jsr299.tck.impl.JSR299PropertiesBasedConfigurationBuilder;
import org.jboss.jsr299.tck.impl.MockCreationalContext;
import org.jboss.jsr299.tck.impl.OldSPIBridge;
import org.jboss.jsr299.tck.literals.AnyLiteral;
import org.jboss.jsr299.tck.spi.Beans;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.container.ClassContainer;
import org.jboss.shrinkwrap.api.container.LibraryContainer;
import org.jboss.shrinkwrap.api.container.ManifestContainer;
import org.jboss.shrinkwrap.api.container.ResourceContainer;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.URLPackageScanner;

/**
 * Abstract shrinkwrap archive builder for JSR299 TCK arquillian test.
 * 
 * <p>
 * Note that all arquillian tests running in as-client mode (including tests using {@link ShouldThrowException}) cannot contain
 * testing related stuff like test class itself while arquillian is not repackaging test archive. That's why
 * {@link #isAsClientMode} has to be properly set.
 * </p>
 * 
 * @param <T> Self type to enable abstract builder pattern
 * @param <A> Final shrinkwrap archive
 * @author Martin Kouba
 */
public abstract class ArchiveBuilder<T extends ArchiveBuilder<T, A>, A extends Archive<A>> {

    private boolean isAsClientMode = false;

    private Class<?> testClazz = null;

    protected ResourceDescriptor beansXml = null;

    protected ResourceDescriptor webXml = null;

    protected ResourceDescriptor persistenceXml = null;

    protected List<ResourceDescriptor> manifestResources = null;

    protected List<ResourceDescriptor> resources = null;

    protected List<ResourceDescriptor> webResources = null;

    protected List<String> packages = null;

    protected List<String> classes = null;

    protected List<String> excludedClasses = null;

    protected List<LibraryDescriptor> libraries = null;

    /**
     * Add <code>beans.xml</code> located in src/main/resource/{testPackagePath}
     * 
     * @param beansXml
     * @return self
     */
    public T withBeansXml(String beansXml) {
        this.beansXml = new ResourceDescriptor(beansXml, "beans.xml");
        return self();
    }

    /**
     * Add CDI extension located in src/main/resource/{testPackagePath} to
     * META-INF/services/javax.enterprise.inject.spi.Extension.
     * 
     * @param extension
     * @return self
     */
    public T withExtension(String extension) {
        return withManifestResource(extension, "services/javax.enterprise.inject.spi.Extension", true);
    }

    /**
     * Add class to archive.
     * 
     * @param clazz
     * @return self
     */
    public T withClass(Class<?> clazz) {
        if (this.classes == null)
            this.classes = new ArrayList<String>();

        this.classes.add(clazz.getName());
        return self();
    }

    /**
     * Add classes to archive.
     * 
     * @param classes
     * @return self
     */
    public T withClasses(Class<?>... classes) {

        for (Class<?> clazz : classes) {
            withClass(clazz);
        }
        return self();
    }

    /**
     * Specified class must be excluded from final archive unless also added via {@link #withClass(Class)} or
     * {@link #withClasses(Class...)}. Useful for exluding some classes from package added via {@link #withPackage(Package)}.
     * 
     * @param clazz
     * @return self
     */
    public T withExcludedClass(Class<?> clazz) {
        if (this.excludedClasses == null)
            this.excludedClasses = new ArrayList<String>();

        this.excludedClasses.add(clazz.getName());
        return self();
    }

    /**
     * Specified classes must be excluded from final archive unless also added via {@link #withClass(Class)} or
     * {@link #withClasses(Class...)}. Useful for exluding some classes from package added via {@link #withPackage(Package)}.
     * 
     * 
     * @param classes
     * @return self
     */
    public T withExcludedClasses(Class<?>... classes) {

        for (Class<?> clazz : classes) {
            withExcludedClass(clazz);
        }
        return self();
    }

    /**
     * Add all classes in the test class package to archive and set test class definition for configuration purpose.
     * 
     * @param testClazz
     * @return self
     */
    public T withTestClassPackage(Class<?> testClazz) {
        return withTestClassDefinition(testClazz).withPackage(testClazz.getPackage());
    }

    /**
     * Add test class to archive and set test class definition for configuration purpose.
     * 
     * @param testClazz
     * @return self
     */
    public T withTestClass(Class<?> testClazz) {
        return withTestClassDefinition(testClazz).withClass(testClazz);
    }

    /**
     * Set test class definition for configuration purpose. Do not add it to final archive.
     * 
     * @param test
     * @return self
     */
    public T withTestClassDefinition(Class<?> testClazz) {

        if (this.testClazz != null)
            throw new IllegalStateException("Cannot set more than one test class definition!");

        this.testClazz = testClazz;
        return self();
    }

    /**
     * Add package (that is its content).
     * 
     * @param pack
     * @param isTestPackage
     * @return self
     */
    private T withPackage(Package pack) {

        if (this.packages == null)
            this.packages = new ArrayList<String>();

        this.packages.add(pack.getName());
        return self();
    }

    /**
     * Add resource to META-INF.
     * 
     * @param source
     * @return self
     */
    public T withManifestResource(String source) {
        return withManifestResource(source, null, true);
    }

    /**
     * Add resource to META-INF.
     * 
     * @param source
     * @param useTestPackageToLocateSource
     * @return self
     */
    public T withManifestResource(String source, boolean useTestPackageToLocateSource) {
        return withManifestResource(source, null, useTestPackageToLocateSource);
    }

    /**
     * Add resource to META-INF.
     * 
     * @param source
     * @param target
     * @param useTestPackageToLocateSource
     * @return self
     */
    public T withManifestResource(String source, String target, boolean useTestPackageToLocateSource) {

        if (this.manifestResources == null)
            this.manifestResources = new ArrayList<ResourceDescriptor>();

        this.manifestResources.add(new ResourceDescriptor(source, target, useTestPackageToLocateSource));
        return self();
    }

    /**
     * Add resource.
     * 
     * @param source
     * @return self
     */
    public T withResource(String source) {
        return withResource(source, null, true);
    }

    /**
     * Add resource.
     * 
     * @param source
     * @param useTestPackageToLocateSource
     * @return self
     */
    public T withResource(String source, boolean useTestPackageToLocateSource) {
        return withResource(source, null, useTestPackageToLocateSource);
    }

    /**
     * Add resource.
     * 
     * @param source
     * @param target
     * @param useTestPackageToLocateSource
     * @return self
     */
    public T withResource(String source, String target, boolean useTestPackageToLocateSource) {

        if (this.resources == null)
            this.resources = new ArrayList<ResourceDescriptor>();

        this.resources.add(new ResourceDescriptor(source, target, useTestPackageToLocateSource));
        return self();
    }

    /**
     * Add web resource.
     * 
     * @param source
     * @return self
     */
    public T withWebResource(String source) {
        return withWebResource(source, null, true);
    }

    /**
     * Add web resource.
     * 
     * @param source
     * @param target
     * @return self
     */
    public T withWebResource(String source, String target) {
        return withWebResource(source, target, true);
    }

    /**
     * Add web resource.
     * 
     * @param source
     * @param useTestPackageToLocateSource
     * @return self
     */
    public T withWebResource(String source, boolean useTestPackageToLocateSource) {
        return withWebResource(source, null, useTestPackageToLocateSource);
    }

    /**
     * Add web resource.
     * 
     * @param source
     * @return
     */
    public T withWebResource(String source, String target, boolean useTestPackageToLocateSource) {

        if (this.webResources == null)
            this.webResources = new ArrayList<ResourceDescriptor>();

        this.webResources.add(new ResourceDescriptor(source, target, useTestPackageToLocateSource));
        return self();
    }

    /**
     * Add ejb-jar.xml located in src/main/resource/{testPackagePath} to META-INF/ejb-jar.xml.
     * 
     * @param ejbJarXml
     * @return
     */
    public T withEjbJarXml(String ejbJarXml) {
        return withManifestResource(ejbJarXml, "ejb-jar.xml", true);
    }

    /**
     * Add web.xml.
     * 
     * @param webXml
     * @return
     */
    public T withWebXml(String webXml) {
        this.webXml = new ResourceDescriptor(webXml);
        return self();
    }

    /**
     * Add persistence.xml.
     * 
     * @param persistenceXml
     * @return
     */
    public T withPersistenceXml(String persistenceXml) {
        this.persistenceXml = new ResourceDescriptor(persistenceXml);
        return self();
    }

    /**
     * Add library.
     * 
     * @param library
     * @return
     */
    public T withLibrary(File library) {

        if (this.libraries == null)
            this.libraries = new ArrayList<LibraryDescriptor>();

        this.libraries.add(new LibraryDescriptor(library));
        return self();
    }

    /**
     * Add bean library that consists of defined bean classes; automatically include empty beans.xml.
     * 
     * @param beanClasses
     * @return
     */
    public T withBeanLibrary(Class<?>... beanClasses) {
        return withLibrary(true, beanClasses);
    }

    /**
     * Add library that consists of defined classes.
     * 
     * @param classes
     * @return
     */
    public T withLibrary(Class<?>... classes) {
        return withLibrary(false, classes);
    }

    /**
     * Add library that consists of defined classes. Include empty beans.xml if necessary.
     * 
     * @param omitBeansXml
     * @param classes
     * @return
     */
    public T withLibrary(boolean includeEmptyBeanXml, Class<?>... classes) {

        if (libraries == null)
            libraries = new ArrayList<LibraryDescriptor>();

        this.libraries.add(new LibraryDescriptor(includeEmptyBeanXml, classes));
        return self();
    }

    /**
     * @return self to enable generic builder
     */
    public abstract T self();

    /**
     * @return shrinkwrap archive
     */
    public A build() {
        if (testClazz == null)
            throw new IllegalStateException("Test class must be set!");

        resolveAsClientMode();

        // org.jboss.jsr299.tck.api
        withPackage(JSR299Configuration.class.getPackage());
        // org.jboss.jsr299.tck.spi
        withPackage(Beans.class.getPackage());
        // org.jboss.jsr299.tck.impl
        withClasses(ConfigurationFactory.class, JSR299ConfigurationImpl.class, JSR299PropertiesBasedConfigurationBuilder.class,
                MockCreationalContext.class, OldSPIBridge.class);
        // org.jboss.jsr299.tck.literal
        withPackage(AnyLiteral.class.getPackage());
        addDefaultLibraries();

        if (!isAsClientMode) {
            withClass(AbstractJSR299Test.class);
        }
        return buildInternal();
    }

    /**
     * @return concrete shrinkwrap archive
     */
    protected abstract A buildInternal();

    /**
     * Process packages. Exclude classes specified via {@link #withExcludedClass(Class)}. If in as-client mode, filter test
     * class.
     * 
     * @param archive
     */
    protected void processPackages(final ClassContainer<?> archive) {

        if (packages == null)
            return;

        for (String pack : packages) {

            final URLPackageScanner.Callback callback = new URLPackageScanner.Callback() {
                @Override
                public void classFound(String className) {
                    if ((isAsClientMode() && testClazz.getName().equals(className))
                            || (excludedClasses != null && excludedClasses.contains(className)))
                        return;

                    archive.addClass(className);
                }
            };

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            if (classLoader == null) {
                classLoader = getClass().getClassLoader();
            }

            final URLPackageScanner scanner = URLPackageScanner.newInstance(false, classLoader, callback, pack);
            scanner.scanPackage();
        }
    }

    /**
     * Process classes.
     * 
     * @param archive
     */
    protected void processClasses(ClassContainer<?> archive) {

        if (classes == null)
            return;

        for (String clazz : classes) {
            archive.addClass(clazz);
        }
    }

    /**
     * Process libraries.
     * 
     * @param archive
     */
    protected void processLibraries(LibraryContainer<?> archive) {

        if (libraries != null) {

            for (LibraryDescriptor descriptor : libraries) {

                if (descriptor.getFileDescriptor() != null) {
                    archive.addAsLibrary(descriptor.getFileDescriptor());
                } else {

                    JavaArchive library = ShrinkWrap.create(JavaArchive.class);
                    for (Class<?> clazz : descriptor.getBeanClasses()) {
                        library.addClass(clazz);
                    }

                    if (descriptor.getBeansXml() != null) {
                        library.addAsManifestResource(descriptor.getBeansXml().getSource(), descriptor.getBeansXml()
                                .getTarget());

                    } else if (descriptor.includeEmptyBeanXml) {
                        library.addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
                    }
                    archive.addAsLibrary(library);
                }
            }
        }
    }

    /**
     * Process resources.
     * 
     * @param archive
     */
    protected void processResources(ResourceContainer<?> archive) {

        if (resources == null)
            return;

        for (ResourceDescriptor resource : resources) {
            if (resource.getTarget() == null) {
                archive.addAsResource(resource.getSource());
            } else {
                archive.addAsResource(resource.getSource(), resource.getTarget());
            }
        }
    }

    /**
     * Process manifest resources.
     * 
     * @param archive
     */
    protected void processManifestResources(ManifestContainer<?> archive) {

        if (manifestResources == null)
            return;

        for (ResourceDescriptor resource : manifestResources) {
            if (resource.getTarget() == null) {
                archive.addAsManifestResource(resource.getSource());
            } else {
                archive.addAsManifestResource(resource.getSource(), resource.getTarget());
            }
        }
    }

    /**
     * Add default libraries from lib directory specified with <code>org.jboss.jsr299.tck.libraryDirectory</code> property in
     * <b>cdi-tck.properties</b>.
     */
    private void addDefaultLibraries() {

        File directory = new File(ConfigurationFactory.get(true).getLibraryDirectory());

        if (directory.isDirectory()) {
            for (File file : directory.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jar");
                }
            })) {
                withLibrary(file);
            }
        }
    }

    /**
     * Internal resource descriptor.
     * 
     * @author Martin Kouba
     */
    protected class ResourceDescriptor {

        private String source;

        private String target;

        private boolean useTestPackageToLocateSource = true;

        public ResourceDescriptor(String source) {
            super();
            this.source = source;
        }

        public ResourceDescriptor(String source, String target) {
            super();
            this.source = source;
            this.target = target;
        }

        public ResourceDescriptor(String source, String target, boolean useTestPackageToLocateSource) {
            super();
            this.source = source;
            this.target = target;
            this.useTestPackageToLocateSource = useTestPackageToLocateSource;
        }

        public String getSource() {
            return useTestPackageToLocateSource ? getTestPackagePath() + source : source;
        }

        public String getPlainSource() {
            return source;
        }

        public String getTarget() {
            return target;
        }

    }

    /**
     * Internal library descriptor. Its possible to automatically include empty beans.xml to promote it to BDA.
     * 
     * @author Martin Kouba
     */
    protected class LibraryDescriptor {

        private File fileDescriptor;

        private List<Class<?>> libraryClasses;

        private ResourceDescriptor beansXml;

        private boolean includeEmptyBeanXml;

        public LibraryDescriptor(File fileDescriptor) {
            super();
            this.fileDescriptor = fileDescriptor;
        }

        public LibraryDescriptor(ResourceDescriptor beansXml, Class<?>... classes) {
            super();
            this.beansXml = beansXml;
            this.libraryClasses = Arrays.asList(classes);
        }

        public LibraryDescriptor(boolean omitBeanXml, Class<?>... classes) {
            super();
            this.includeEmptyBeanXml = omitBeanXml;
            this.libraryClasses = Arrays.asList(classes);
        }

        public List<Class<?>> getBeanClasses() {
            return libraryClasses;
        }

        public ResourceDescriptor getBeansXml() {
            return beansXml;
        }

        public boolean isOmitBeanXml() {
            return includeEmptyBeanXml;
        }

        public File getFileDescriptor() {
            return fileDescriptor;
        }
    }

    private String getTestPackagePath() {
        return this.testClazz.getPackage().getName().replace('.', '/').concat("/");
    }

    /**
     * 
     * @return <code>true</code> if building as-client mode archive (no test infrastructure), <code>false</code> otherwise
     */
    protected boolean isAsClientMode() {
        return isAsClientMode;
    }

    /**
     * Find deployment method on test class definition and set to as-client mode if {@link Deployment#testable()} false or
     * {@link ShouldThrowException} is present.
     */
    private void resolveAsClientMode() {

        for (Method method : testClazz.getMethods()) {

            if (method.isAnnotationPresent(Deployment.class)) {

                if (method.isAnnotationPresent(ShouldThrowException.class)) {
                    this.isAsClientMode = true;
                } else if (!method.getAnnotation(Deployment.class).testable()) {
                    this.isAsClientMode = true;
                }
                break;
            }
        }
    }
}
