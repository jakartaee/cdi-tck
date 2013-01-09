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
package org.jboss.cdi.tck.shrinkwrap;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.Extension;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.TestGroups;
import org.jboss.cdi.tck.api.Configuration;
import org.jboss.cdi.tck.impl.ConfigurationFactory;
import org.jboss.cdi.tck.impl.ConfigurationImpl;
import org.jboss.cdi.tck.impl.MockCreationalContext;
import org.jboss.cdi.tck.impl.OldSPIBridge;
import org.jboss.cdi.tck.impl.PropertiesBasedConfigurationBuilder;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.spi.Beans;
import org.jboss.cdi.tck.util.Timer;
import org.jboss.cdi.tck.util.annotated.AnnotatedWrapper;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.container.ClassContainer;
import org.jboss.shrinkwrap.api.container.LibraryContainer;
import org.jboss.shrinkwrap.api.container.ManifestContainer;
import org.jboss.shrinkwrap.api.container.ResourceContainer;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.shrinkwrap.descriptor.api.persistence20.PersistenceDescriptor;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.shrinkwrap.impl.base.URLPackageScanner;
import org.jboss.shrinkwrap.impl.base.asset.AssetUtil;
import org.jboss.shrinkwrap.impl.base.path.BasicPath;

/**
 * Abstract ShrinkWrap archive builder for CDI TCK Arquillian test.
 * <p>
 * This is a base class for builders that try to solve most <b>JBoss Test Harness</b> to <b>Arquillian</b> migration issues. The
 * main goal was to use CDI TCK 1.0 tests with minimum code changes.
 * </p>
 * <p>
 * Note that all Arquillian tests running in as-client mode (including tests using {@link ShouldThrowException}) must not
 * contain testing related stuff (anything that depends on Arquillian, TestNG, incl. test class itself) since Arquillian is not
 * enriching as-client test archives. That's why {@link #isAsClientMode} has to be properly set.
 * </p>
 * <p>
 * In case of {@link #isTestArchive} set to <code>false</code> this archive may not include any testing related stuff as it is
 * probably part of another test archive.
 * </p>
 * 
 * @param <T> Self type to enable abstract builder pattern
 * @param <A> Final shrinkwrap archive
 * @author Martin Kouba
 */
public abstract class ArchiveBuilder<T extends ArchiveBuilder<T, A>, A extends Archive<A>> {

    private static final Logger logger = Logger.getLogger(ArchiveBuilder.class.getName());

    private static final JavaArchive supportLibrary = buildSupportLibrary();

    private String name;

    private Boolean isAsClientMode = null;

    private boolean isTestArchive = true;

    private Class<?> testClazz = null;

    protected ResourceDescriptor beansXml = null;

    protected BeansDescriptor beansDescriptor = null;

    protected ResourceDescriptor webXml = null;

    protected WebAppDescriptor webXmlDescriptor = null;

    protected PersistenceDescriptor persistenceDescriptor = null;

    protected List<ResourceDescriptor> manifestResources = null;

    protected List<ResourceDescriptor> resources = null;

    protected List<ResourceDescriptor> webResources = null;

    protected List<Package> packages = null;

    protected List<Class<?>> classes = null;

    protected List<String> excludedClasses = null;

    protected List<LibraryDescriptor> libraries = null;

    protected List<JavaArchive> shrinkWrapLibraries = null;

    protected List<ServiceProviderDescriptor> serviceProviders = null;

    /**
     * Change default archive name.
     * 
     * @param name
     * @return
     */
    public T withName(String name) {
        this.name = name;
        return self();
    }

    /**
     * Add <code>beans.xml</code> located in src/main/resource/{testPackagePath}.
     * 
     * <p>
     * Do not use this in new tests - use {@link #withBeansXml(BeansDescriptor)} instead.
     * </p>
     * 
     * @param beansXml
     * @return self
     */
    public T withBeansXml(String beansXml) {
        this.beansXml = new ResourceDescriptor(beansXml, "beans.xml");
        return self();
    }

    /**
     * Add <code>beans.xml</code> descriptor created with shrinkwrap-descriptors.
     * 
     * @param beansDescriptor
     * @return self
     */
    public T withBeansXml(BeansDescriptor beansDescriptor) {
        this.beansDescriptor = beansDescriptor;
        return self();
    }

    /**
     * Add CDI extension. This method does not add the specified extension class to the archive.
     * 
     * @param extensionClass
     * @return self
     */
    public T withExtension(Class<? extends Extension> extensionClass) {
        return withServiceProvider(new ServiceProviderDescriptor(Extension.class, extensionClass));
    }

    /**
     * Add CDI extensions. This method does not add the specified extension classes to the archive.
     * 
     * @param extensionClasses
     * @return self
     */
    public T withExtensions(Class<? extends Extension>... extensionClasses) {
        return withServiceProvider(new ServiceProviderDescriptor(Extension.class, extensionClasses));
    }

    /**
     * Add service provider.
     * 
     * @param serviceProvider
     * @return
     */
    public T withServiceProvider(ServiceProviderDescriptor serviceProvider) {

        if (serviceProviders == null)
            serviceProviders = new ArrayList<ServiceProviderDescriptor>();

        serviceProviders.add(serviceProvider);
        return self();
    }

    /**
     * Add class to archive.
     * 
     * @param clazz
     * @return self
     */
    public T withClass(Class<?> clazz) {
        if (this.classes == null)
            this.classes = new ArrayList<Class<?>>();

        this.classes.add(clazz);
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
     * Avoid using this feature if possible - the implementation has negative performance effects.
     * 
     * @param clazz Fully qualified class name
     * @return self
     */
    public T withExcludedClass(String clazz) {
        if (this.excludedClasses == null)
            this.excludedClasses = new ArrayList<String>();

        this.excludedClasses.add(clazz);
        return self();
    }

    /**
     * Specified classes must be excluded from final archive unless also added via {@link #withClass(Class)} or
     * {@link #withClasses(Class...)}. Useful for exluding some classes from package added via {@link #withPackage(Package)}.
     * 
     * Avoid using this feature if possible - the implementation has negative performance effects.
     * 
     * @param classes Fully qualified class names
     * @return self
     */
    public T withExcludedClasses(String... classes) {

        for (String clazz : classes) {
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
     * Always use this for as-client test archives, e.g. deployment method annotated with {@link ShouldThrowException}.
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
     * Add package (that is its content). Subpackages are not included.
     * 
     * @param pack
     * @return self
     */
    public T withPackage(Package pack) {

        if (this.packages == null)
            this.packages = new ArrayList<Package>();

        this.packages.add(pack);
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
     * Add web.xml located in src/main/resource/{testPackagePath}.
     * 
     * <p>
     * Do not use this in new tests - use {@link #withWebXml(WebAppDescriptor)} instead.
     * </p>
     * 
     * @param webXml
     * @return
     */
    public T withWebXml(String webXml) {
        this.webXml = new ResourceDescriptor(webXml);
        return self();
    }

    /**
     * Add <code>web.xml</code> descriptor created with shrinkwrap-descriptors.
     * 
     * @param webXml
     * @return
     */
    public T withWebXml(WebAppDescriptor webXml) {
        this.webXmlDescriptor = webXml;
        return self();
    }

    /**
     * Add default persistence.xml.
     * 
     * @return self
     */
    public T withDefaultPersistenceXml() {
        return withPersistenceXml(Descriptors.create(PersistenceDescriptor.class).createPersistenceUnit().name("test")
                .jtaDataSource(ConfigurationFactory.get().getTestDataSource()).up());
    }

    /**
     * Add persistence.xml descriptor created with shrinkwrap-descriptors.
     * 
     * @param persistenceXml
     * @return self
     */
    public T withPersistenceXml(PersistenceDescriptor persistenceDescriptor) {
        this.persistenceDescriptor = persistenceDescriptor;
        return self();
    }

    /**
     * Add library.
     * 
     * @param library
     * @return self
     */
    public T withLibrary(File library) {

        if (this.libraries == null)
            this.libraries = new ArrayList<LibraryDescriptor>();

        this.libraries.add(new LibraryDescriptor(library));
        return self();
    }

    /**
     * Add bean library that consists of defined bean classes; automatically include empty <code>beans.xml</code> and if any of
     * defined classes implements {@link Extension} add corresponding service provider.
     * 
     * @param beanClasses
     * @return self
     */
    public T withBeanLibrary(Class<?>... beanClasses) {
        return withLibrary(null, true, beanClasses);
    }

    /**
     * Add bean library that consists of defined bean classes; automatically include empty <code>beans.xml</code> and if any of
     * defined classes implements {@link Extension} add corresponding service provider.
     * 
     * @param beanClasses
     * @return self
     */
    public T withBeanLibrary(BeansDescriptor beansDescriptor, Class<?>... beanClasses) {
        return withBeanLibrary(null, beansDescriptor, beanClasses);
    }

    /**
     * Add bean library that consists of defined bean classes; automatically include empty <code>beans.xml</code> and if any of
     * defined classes implements {@link Extension} add corresponding service provider.
     * 
     * @param beanClasses
     * @return self
     */
    public T withBeanLibrary(String name, Class<?>... beanClasses) {
        return withBeanLibrary(name, null, beanClasses);
    }

    /**
     * Add bean library that consists of defined bean classes; automatically include empty <code>beans.xml</code> and if any of
     * defined classes implements {@link Extension} add corresponding service provider.
     * 
     * @param beanClasses
     * @return self
     */
    public T withBeanLibrary(String name, BeansDescriptor beansDescriptor, Class<?>... beanClasses) {
        return withLibrary(name, beansDescriptor, true, beanClasses);
    }

    /**
     * Add library that consists of defined classes.
     * 
     * @param classes
     * @return self
     */
    public T withLibrary(Class<?>... classes) {
        return withLibrary(null, false, classes);
    }

    /**
     * Add library that consists of defined classes. Include empty beans.xml if necessary.
     * 
     * @param serviceProvider
     * @param omitBeansXml
     * @param classes
     * @return self
     */
    public T withLibrary(BeansDescriptor beansDescriptor, boolean includeEmptyBeanXml, Class<?>... classes) {
        return withLibrary(null, beansDescriptor, includeEmptyBeanXml, classes);
    }

    /**
     * Add library that consists of defined classes. Include empty beans.xml if necessary.
     * 
     * @param serviceProvider
     * @param omitBeansXml
     * @param classes
     * @return self
     */
    public T withLibrary(String name, BeansDescriptor beansDescriptor, boolean includeEmptyBeanXml, Class<?>... classes) {

        if (libraries == null)
            libraries = new ArrayList<LibraryDescriptor>(5);

        List<Class<?>> extensions = new ArrayList<Class<?>>();
        for (Class<?> clazz : classes) {
            if (Extension.class.isAssignableFrom(clazz)) {
                extensions.add(clazz);
            }
        }
        ServiceProviderDescriptor serviceProvider = extensions.isEmpty() ? null : new ServiceProviderDescriptor(
                Extension.class, extensions.toArray(new Class<?>[extensions.size()]));

        this.libraries.add(beansDescriptor != null ? new LibraryDescriptor(name, serviceProvider, beansDescriptor, classes)
                : new LibraryDescriptor(name, serviceProvider, includeEmptyBeanXml, classes));
        return self();
    }

    /**
     * Add the specified ShrinkWrap library.
     * 
     * @param library
     * @return self
     */
    public T withLibrary(JavaArchive library) {

        if (shrinkWrapLibraries == null)
            shrinkWrapLibraries = new ArrayList<JavaArchive>(5);

        shrinkWrapLibraries.add(library);

        return self();
    }

    /**
     * @return self to enable generic builder
     */
    public abstract T self();

    /**
     * @return ShrinkWrap archive
     */
    public A build() {

        long start = System.currentTimeMillis();

        if (isTestArchive()) {

            if (testClazz == null)
                throw new IllegalStateException("Test class must be set!");

            resolveAsClientMode();

            // Support library
            withLibrary(supportLibrary);
            // Default libraries
            addDefaultLibraries();

            if (!isAsClientMode()) {
                withClass(AbstractTest.class);
                withClass(TestGroups.class);
            }
        }

        A archive = buildInternal();
        logger.log(
                Level.INFO,
                "Test archive built [info: {0}, time: {1} ms]",
                new Object[] { testClazz != null ? testClazz.getName() : archive.getName(),
                        Long.valueOf(System.currentTimeMillis() - start) });
        return archive;
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
    protected <P extends Archive<?> & ClassContainer<?>> void processPackages(final P archive) {

        if (packages == null)
            return;

        // Avoid using URLPackageScanner if possible - it has negative performance effects
        if ((excludedClasses == null || excludedClasses.isEmpty()) && !isAsClientMode()) {
            archive.addPackages(false, packages.toArray(new Package[packages.size()]));
        } else {

            final String testClazzName = testClazz.getName();
            final ClassLoader cl = testClazz.getClassLoader();
            final ClassLoader clToUse = (cl != null ? cl : ClassLoader.getSystemClassLoader());

            final URLPackageScanner.Callback callback = new URLPackageScanner.Callback() {
                @Override
                public void classFound(String className) {

                    if (isAsClientMode() && testClazzName.equals(className))
                        return;

                    if (excludedClasses != null && !excludedClasses.isEmpty()) {
                        for (String exludeClassName : excludedClasses) {
                            // Handle annonymous inner classes
                            if (className.startsWith(exludeClassName))
                                return;
                        }
                    }

                    // This is a performance WORKAROUND - adding classes via ClassContainer.addClass(String fqcn) is much slower
                    // See also org.jboss.shrinkwrap.impl.base.container.ContainerBase addPackage() and getClassesPath() methods
                    ArchivePath classesPath = resolveClassesPath(archive);

                    if (classesPath != null) {
                        ArchivePath classNamePath = AssetUtil.getFullPathForClassResource(className);
                        archive.add(new ClassLoaderAsset(classNamePath.get().substring(1), clToUse), new BasicPath(classesPath,
                                classNamePath));
                    } else {
                        archive.addClass(className);
                    }
                }
            };

            for (Package pack : packages) {
                URLPackageScanner.newInstance(false, clToUse, callback, pack.getName()).scanPackage();
            }
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

        for (Class<?> clazz : classes) {

            if ((isAsClientMode() && testClazz.getName().equals(clazz))
                    || (excludedClasses != null && excludedClasses.contains(clazz)))
                continue;

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
                    archive.addAsLibrary(descriptor.buildJarArchive());
                }
            }
        }

        if (shrinkWrapLibraries != null) {
            archive.addAsLibraries(shrinkWrapLibraries);
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

        if (manifestResources != null) {
            for (ResourceDescriptor resource : manifestResources) {
                if (resource.getTarget() == null) {
                    archive.addAsManifestResource(resource.getSource());
                } else {
                    archive.addAsManifestResource(resource.getSource(), resource.getTarget());
                }
            }
        }

        if (serviceProviders != null) {
            for (ServiceProviderDescriptor descriptor : serviceProviders) {
                archive.addAsServiceProvider(descriptor.getServiceInterface(), descriptor.getServiceImplementations());
            }
        }
    }

    /**
     * Add default libraries from lib directory specified with <code>org.jboss.cdi.tck.libraryDirectory</code> property in
     * <b>cdi-tck.properties</b>.
     */
    private void addDefaultLibraries() {

        File directory = new File(ConfigurationFactory.get(true).getLibraryDirectory());
        logger.log(Level.FINE, "Scanning default library dir {0}", directory.getPath());

        if (directory.isDirectory()) {
            for (File file : directory.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jar");
                }
            })) {
                logger.log(Level.FINE, "Adding default library {0}", file.getName());
                withLibrary(file);
            }
        }
    }

    /**
     * Internal service provider descriptor.
     * 
     * @author Martin Kouba
     */
    protected class ServiceProviderDescriptor {

        private Class<?> serviceInterface;

        private Class<?>[] serviceImplementations;

        public ServiceProviderDescriptor(Class<?> serviceInterface, Class<?>... serviceImplementations) {
            super();
            this.serviceInterface = serviceInterface;
            this.serviceImplementations = serviceImplementations;
        }

        public Class<?> getServiceInterface() {
            return serviceInterface;
        }

        public Class<?>[] getServiceImplementations() {
            return serviceImplementations;
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
     * Internal library descriptor.
     * 
     * @author Martin Kouba
     */
    protected class LibraryDescriptor {

        private String name;

        private File fileDescriptor = null;

        private List<Class<?>> libraryClasses = null;

        protected BeansDescriptor beansDescriptor = null;

        private boolean includeEmptyBeanXml = false;

        private List<ServiceProviderDescriptor> serviceProviders;

        public LibraryDescriptor(File fileDescriptor) {
            super();
            this.fileDescriptor = fileDescriptor;
        }

        public LibraryDescriptor(String name, ServiceProviderDescriptor serviceProvider, BeansDescriptor beansDescriptor,
                Class<?>... classes) {
            super();
            if (serviceProvider != null) {
                this.serviceProviders = new ArrayList<ServiceProviderDescriptor>();
                this.serviceProviders.add(serviceProvider);
            }
            this.beansDescriptor = beansDescriptor;
            this.libraryClasses = Arrays.asList(classes);
            this.name = name;
        }

        /**
         * 
         * @param name
         * @param serviceProvider
         * @param includeEmptyBeanXml Automatically include empty beans.xml to promote the lib to BDA
         * @param classes
         */
        public LibraryDescriptor(String name, ServiceProviderDescriptor serviceProvider, boolean includeEmptyBeanXml,
                Class<?>... classes) {
            super();
            if (serviceProvider != null) {
                this.serviceProviders = new ArrayList<ServiceProviderDescriptor>();
                this.serviceProviders.add(serviceProvider);
            }
            this.includeEmptyBeanXml = includeEmptyBeanXml;
            this.libraryClasses = Arrays.asList(classes);
            this.name = name;
        }

        public String getName() {
            return name;
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

        public List<ServiceProviderDescriptor> getServiceProviders() {
            return serviceProviders;
        }

        /**
         * 
         * @return shrinkwrap jar archive
         */
        public JavaArchive buildJarArchive() {

            JavaArchive library = null;

            if (name != null) {
                library = ShrinkWrap.create(JavaArchive.class, name);
            } else {
                library = ShrinkWrap.create(JavaArchive.class);
            }

            for (Class<?> clazz : libraryClasses) {
                library.addClass(clazz);
            }

            if (serviceProviders != null) {
                for (ServiceProviderDescriptor serviceProvider : serviceProviders) {
                    library.addAsServiceProvider(serviceProvider.getServiceInterface(),
                            serviceProvider.getServiceImplementations());
                }
            }

            if (beansDescriptor != null) {
                library.addAsManifestResource(new StringAsset(beansDescriptor.exportAsString()),
                        beansDescriptor.getDescriptorName());

            } else if (includeEmptyBeanXml) {
                library.addAsManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
            }
            return library;
        }

    }

    private String getTestPackagePath() {
        return this.testClazz.getPackage().getName().replace('.', '/').concat("/");
    }

    /**
     * 
     * @return <code>true</code> if building as-client mode archive, <code>false</code> otherwise
     */
    public Boolean isAsClientMode() {
        return isAsClientMode != null ? isAsClientMode : false;
    }

    /**
     * @param isAsClientMode
     * @see #resolveAsClientMode()
     */
    public T setAsClientMode(boolean isAsClientMode) {
        this.isAsClientMode = isAsClientMode;
        return self();
    }

    /**
     * 
     * @return <code>true</code> if TCK specific infrastructure (porting package, utils, etc.) should be automatically added,
     *         <code>false</code> otherwise
     * @see #resolveAsClientMode()
     */
    public boolean isTestArchive() {
        return isTestArchive;
    }

    /**
     * Mark this archive as non-testing. TCK specific infrastructure (porting package, utils, etc.) will not be automatically
     * added.
     */
    public T notTestArchive() {
        this.isTestArchive = false;
        return self();
    }

    /**
     * Try to resolve as-client mode automatically unless it was set already.
     * 
     * Set as-client mode to <code>true</code> provided that:
     * <ul>
     * <li>test class is annotated with {@link RunAsClient}</li>
     * <li>the deployment method on test class definition is annotated with {@link ShouldThrowException} or
     * {@link Deployment#testable()} is false</li>
     * <ul>
     * 
     * @throws IllegalStateException If multiple deployments detected and as-client mode not set
     * @see #setAsClientMode(boolean)
     */
    private void resolveAsClientMode() {

        if (this.isAsClientMode != null) {
            return;
        }

        if (testClazz.isAnnotationPresent(RunAsClient.class)) {
            setAsClientMode(true);
        }

        Method deploymentMethod = null;

        for (Method method : testClazz.getMethods()) {

            if (Modifier.isStatic(method.getModifiers()) && method.isAnnotationPresent(Deployment.class)) {

                if (deploymentMethod != null && this.isAsClientMode == null) {
                    throw new IllegalStateException("Multi-deployment test class and as-client mode not set");
                }
                deploymentMethod = method;

                if (method.isAnnotationPresent(ShouldThrowException.class)
                        || !method.getAnnotation(Deployment.class).testable()) {
                    setAsClientMode(true);
                }
            }
        }
    }

    /**
     * @return name of final archive
     */
    public String getName() {
        return name;
    }

    private static JavaArchive buildSupportLibrary() {

        long start = System.currentTimeMillis();

        JavaArchive supportLib = ShrinkWrap
                .create(JavaArchive.class, "cdi-tck-support.jar")
                // CDI TCK properties
                .addAsResource(PropertiesBasedConfigurationBuilder.RESOURCE_BUNDLE)
                // org.jboss.cdi.tck.api
                .addPackage(Configuration.class.getPackage())
                // org.jboss.cdi.tck.spi
                .addPackage(Beans.class.getPackage())
                // org.jboss.cdi.tck.impl
                .addClasses(ConfigurationFactory.class, ConfigurationImpl.class, PropertiesBasedConfigurationBuilder.class,
                        MockCreationalContext.class, OldSPIBridge.class)
                // org.jboss.cdi.tck.literal
                .addPackage(AnyLiteral.class.getPackage())
                // Util packages
                .addPackage(Timer.class.getPackage()).addPackage(AnnotatedWrapper.class.getPackage());

        logger.log(Level.INFO, "Support library built [time: {0} ms]", System.currentTimeMillis() - start);
        return supportLib;
    }

    /**
     * @param archive
     * @return Base Path for the ClassContainer resources
     */
    private ArchivePath resolveClassesPath(Archive<?> archive) {
        if (archive instanceof WebArchive) {
            return ArchivePaths.create("WEB-INF/classes");
        } else if (archive instanceof JavaArchive) {
            return new BasicPath("/");
        }
        return null;
    }

}