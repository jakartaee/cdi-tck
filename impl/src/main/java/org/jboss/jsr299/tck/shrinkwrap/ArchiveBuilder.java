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

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.inject.spi.Extension;

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
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.container.ClassContainer;
import org.jboss.shrinkwrap.api.container.LibraryContainer;
import org.jboss.shrinkwrap.api.container.ManifestContainer;
import org.jboss.shrinkwrap.api.container.ResourceContainer;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.shrinkwrap.impl.base.URLPackageScanner;

/**
 * Abstract shrinkwrap archive builder for JSR299 TCK arquillian test.
 * <p>
 * This is a base class for builders that try to solve most <b>JBoss Test Harness</b> to <b>Arquillian</b> migration issues. The
 * main goal was to use CDI TCK 1.0 tests with minimum code changes.
 * </p>
 * <p>
 * Note that all arquillian tests running in as-client mode (including tests using {@link ShouldThrowException}) cannot contain
 * testing related stuff like test class itself while arquillian is not repackaging test archive. That's why
 * {@link #isAsClientMode} has to be properly set.
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

    private String name;

    private boolean isAsClientMode = false;

    private boolean isTestArchive = true;

    private Class<?> testClazz = null;

    protected ResourceDescriptor beansXml = null;

    protected BeansDescriptor beansDescriptor = null;

    protected ResourceDescriptor webXml = null;

    protected ResourceDescriptor persistenceXml = null;

    protected List<ResourceDescriptor> manifestResources = null;

    protected List<ResourceDescriptor> resources = null;

    protected List<ResourceDescriptor> webResources = null;

    protected List<String> packages = null;

    protected List<String> classes = null;

    protected List<String> excludedClasses = null;

    protected List<LibraryDescriptor> libraries = null;

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
     * Add <code>beans.xml</code> located in src/main/resource/{testPackagePath}
     * 
     * <p>
     * Do not use this in new tests - use ${@link #withBeansXml(BeansDescriptor)} instead.
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
     * Add CDI extension located in src/main/resource/{testPackagePath} to
     * META-INF/services/javax.enterprise.inject.spi.Extension.
     * 
     * <p>
     * Do not use this in new tests - use ${@link #withExtension(Class)} instead.
     * </p>
     * 
     * @param extension
     * @return self
     */
    public T withExtension(String extension) {
        return withManifestResource(extension, "services/javax.enterprise.inject.spi.Extension", true);
    }

    /**
     * Add CDI extension. This method does not add the specified class to the archive.
     * 
     * @param extensionClass
     * @return self
     */
    public T withExtension(Class<? extends Extension> extensionClass) {
        return withServiceProvider(new ServiceProviderDescriptor(Extension.class, extensionClass));
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
     * @return self
     */
    public T withPersistenceXml(String persistenceXml) {
        this.persistenceXml = new ResourceDescriptor(persistenceXml);
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
     * @return
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
     * @return
     */
    public T withLibrary(String name, BeansDescriptor beansDescriptor, boolean includeEmptyBeanXml, Class<?>... classes) {

        if (libraries == null)
            libraries = new ArrayList<LibraryDescriptor>();

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
     * @return self to enable generic builder
     */
    public abstract T self();

    /**
     * @return shrinkwrap archive
     */
    public A build() {

        if (isTestArchive()) {

            if (testClazz == null)
                throw new IllegalStateException("Test class must be set!");

            resolveAsClientMode();

            // org.jboss.jsr299.tck.api
            withPackage(JSR299Configuration.class.getPackage());
            // org.jboss.jsr299.tck.spi
            withPackage(Beans.class.getPackage());
            // org.jboss.jsr299.tck.impl
            withClasses(ConfigurationFactory.class, JSR299ConfigurationImpl.class,
                    JSR299PropertiesBasedConfigurationBuilder.class, MockCreationalContext.class, OldSPIBridge.class);
            // org.jboss.jsr299.tck.literal
            withPackage(AnyLiteral.class.getPackage());
            addDefaultLibraries();

            if (!isAsClientMode()) {
                withClass(AbstractJSR299Test.class);
            }
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
     * <p>
     * Its possible to automatically include empty beans.xml to promote it to BDA.
     * </p>
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
     * @return <code>true</code> if building as-client mode archive (no test infrastructure), <code>false</code> otherwise
     */
    protected boolean isAsClientMode() {
        return isAsClientMode;
    }

    /**
     * 
     * @return <code>true</code> in case of archive should
     */
    public boolean isTestArchive() {
        return isTestArchive;
    }

    /**
     * Mark this archive as non-testing.
     */
    public T notTestArchive() {
        this.isTestArchive = false;
        return self();
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

    /**
     * @return name of final archive
     */
    public String getName() {
        return name;
    }

}
