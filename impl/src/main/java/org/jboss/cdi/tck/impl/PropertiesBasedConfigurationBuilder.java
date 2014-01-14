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
package org.jboss.cdi.tck.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.jboss.cdi.tck.api.Configuration;
import org.jboss.cdi.tck.spi.Beans;
import org.jboss.cdi.tck.spi.Contexts;
import org.jboss.cdi.tck.spi.EL;

/**
 * CDI TCK configuration builder.
 *
 * Mostly based on jboss-test-harness code from <code>org.jboss.testharness.impl.PropertiesBasedConfigurationBuilder</code> and
 * <code>org.jboss.testharness.properties.PropertiesManager</code>.
 */
public class PropertiesBasedConfigurationBuilder {

    public static final String RESOURCE_BUNDLE = "META-INF/cdi-tck.properties";

    private final Configuration configuration;

    /**
     *
     */
    public PropertiesBasedConfigurationBuilder() {
        configuration = new ConfigurationImpl();
    }

    /**
     * @return built configuration
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * @param deploymentPhase Deployment phase (building test archive) initialization includes deployment specific properties
     * @return initialized self
     */
    @SuppressWarnings("unchecked")
    public PropertiesBasedConfigurationBuilder init(boolean deploymentPhase) {

        configuration.setBeans(getInstanceValue(Beans.PROPERTY_NAME, Beans.class, !deploymentPhase));
        configuration.setEl(getInstanceValue(EL.PROPERTY_NAME, EL.class, !deploymentPhase));
        configuration.setContexts(getInstanceValue(Contexts.PROPERTY_NAME, Contexts.class, !deploymentPhase));

        configuration.setLibraryDirectory(getStringValue(Configuration.LIBRARY_DIRECTORY_PROPERTY_NAME, null, deploymentPhase));

        configuration.setTestDataSource(getStringValue(Configuration.TEST_DATASOURCE_PROPERTY_NAME, null, deploymentPhase));
        configuration.setTestJmsConnectionFactory(getStringValue(Configuration.TEST_JMS_CONNECTION_FACTORY, null, deploymentPhase));
        configuration.setTestJmsQueue(getStringValue(Configuration.TEST_JMS_QUEUE, null, deploymentPhase));
        configuration.setTestJmsTopic(getStringValue(Configuration.TEST_JMS_TOPIC, null, deploymentPhase));

        configuration.setTestTimeoutFactor(getIntegerValue(Configuration.TEST_TIMEOUT_FACTOR, Configuration.TEST_TIMEOUT_FACTOR_DEFAULT_VALUE, false));

        return this;
    }

    /**
     * Get a list of possible values for a given key.
     *
     * First, System properties are tried, followed by the specified resource bundle (first in classpath only).
     *
     * @param key The key to search for
     * @return A list of possible values. An empty list is returned if there are no matches.
     */
    public Set<String> getPropertyValues(String key) {
        Set<String> values = new HashSet<String>();
        addPropertiesFromSystem(key, values);
        addPropertiesFromResourceBundle(key, values);
        return values;
    }

    /**
     *
     * @param propertyName
     * @param defaultValue
     * @param required
     * @return
     */
    public String getStringValue(String propertyName, String defaultValue, boolean required) {
        String value = getValue(propertyName, required);
        return value != null ? value : defaultValue;
    }

    /**
     *
     * @param propertyName
     * @param defaultValue
     * @param required
     * @return
     */
    public long getLongValue(String propertyName, long defaultValue, boolean required) {
        try {
            String value = getValue(propertyName, required);
            return value != null ? Long.valueOf(value) : defaultValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid property value", e);
        }
    }

    /**
     *
     * @param propertyName
     * @param defaultValue
     * @param required
     * @return
     */
    public int getIntegerValue(String propertyName, int defaultValue, boolean required) {
        try {
            String value = getValue(propertyName, required);
            return value != null ? Integer.valueOf(value) : defaultValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid property value", e);
        }
    }

    /**
     * Adds matches from system properties
     *
     * @param key The key to match
     * @param values The currently found values
     */
    private void addPropertiesFromSystem(String key, Set<String> values) {
        addProperty(key, System.getProperty(key), values);
    }

    /**
     * Adds matches from detected resource bundles
     *
     * @param key The key to match
     * @param values The currently found values
     */
    private void addPropertiesFromResourceBundle(String key, Set<String> values) {
        try {

            for (Enumeration<URL> e = getResources(RESOURCE_BUNDLE); e.hasMoreElements();) {

                URL url = e.nextElement();
                Properties properties = new Properties();
                InputStream propertyStream = url.openStream();

                try {

                    properties.load(propertyStream);
                    addProperty(key, properties.getProperty(key), values);

                } finally {
                    if (propertyStream != null) {
                        propertyStream.close();
                    }
                }
            }

        } catch (IOException e) {
            // No-op, file is optional
        }
    }

    /**
     * Add the property to the set of properties only if it hasn't already been added
     *
     * @param key The key searched for
     * @param value The value of the property
     * @param values The currently found values
     */
    private void addProperty(String key, String value, Set<String> values) {
        if (value != null) {
            values.add(value);
        }
    }

    /**
     *
     * @param name
     * @return
     * @throws IOException
     */
    public Enumeration<URL> getResources(String name) throws IOException {

        if (Thread.currentThread().getContextClassLoader() != null) {
            return Thread.currentThread().getContextClassLoader().getResources(name);
        } else {
            return getClass().getClassLoader().getResources(name);
        }
    }

    /**
     *
     * @param <T>
     * @param propertyName
     * @param expectedType
     * @param required
     * @return
     */
    protected <T> T getInstanceValue(String propertyName, Class<T> expectedType, boolean required) {

        T instance = null;

        Class<T> clazz = getClassValue(propertyName, expectedType, required);
        if (clazz != null) {
            try {
                instance = clazz.newInstance();
            } catch (InstantiationException e) {
                throw new IllegalStateException("Error instantiating " + clazz + " specified by " + propertyName, e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Error instantiating " + clazz + " specified by " + propertyName, e);
            }
        }
        return instance;
    }

    /**
     *
     * @param <T>
     * @param propertyName
     * @param expectedType
     * @param required
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> Class<T> getClassValue(String propertyName, Class<T> expectedType, boolean required) {

        Set<Class<T>> classes = new HashSet<Class<T>>();

        for (String className : getPropertyValues(propertyName)) {
            ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                if (currentThreadClassLoader != null) {
                    classes.add((Class<T>) currentThreadClassLoader.loadClass(className));
                } else {
                    classes.add((Class<T>) Class.forName(className));
                }

            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Implementation class with name " + className + " not found using classloader "
                        + (currentThreadClassLoader != null ? currentThreadClassLoader : this.getClass().getClassLoader()), e);
            }
        }

        if (classes.size() == 0) {
            if (required) {
                throw new IllegalArgumentException("Cannot find any implementations of " + expectedType.getSimpleName() + ", check that " + propertyName
                        + " is specified");
            } else {
                return null;
            }
        } else if (classes.size() > 1) {
            throw new IllegalArgumentException("More than one implementation of " + expectedType.getSimpleName() + " specified by " + propertyName
                    + ", not sure which one to use!");
        } else {
            return classes.iterator().next();
        }
    }


    private String getValue(String propertyName, boolean required) {
        Set<String> values = getPropertyValues(propertyName);
        if (values.size() == 0) {
            if (required) {
                throw new IllegalArgumentException("Cannot find required property " + propertyName + ", check that it is specified");
            }
            return null;
        } else if (values.size() > 1) {
            throw new IllegalArgumentException("More than one value given for " + propertyName + ", not sure which one to use!");
        } else {
            return values.iterator().next();
        }
    }

}
