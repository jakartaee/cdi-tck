/*
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
package org.jboss.cdi.tck.api;

import jakarta.enterprise.context.spi.Context;

import org.jboss.cdi.tck.spi.Beans;
import org.jboss.cdi.tck.spi.Contexts;
import org.jboss.cdi.tck.spi.Contextuals;
import org.jboss.cdi.tck.spi.CreationalContexts;
import org.jboss.cdi.tck.spi.EL;

/**
 * The configuration of the TCK.
 *
 * The TCK may be configured using system properties or placed in a properties file called META-INF/cdi-tck.properties.
 *
 * Porting package property names are the FQCN of the SPI class. Other property names (one for each non-porting package SPI
 * configuration option) are specified here.
 *
 * The TCK may also be configured programatically through this interface.
 *
 * @author Pete Muir
 * @author Martin Kouba
 */
public interface Configuration {

    public static final String CDI_LITE_MODE = "org.jboss.cdi.tck.cdiLiteMode";

    public static final String LIBRARY_DIRECTORY_PROPERTY_NAME = "org.jboss.cdi.tck.libraryDirectory";

    public static final String TEST_DATASOURCE_PROPERTY_NAME = "org.jboss.cdi.tck.testDataSource";

    public static final String TEST_JMS_CONNECTION_FACTORY = "org.jboss.cdi.tck.testJmsConnectionFactory";

    public static final String TEST_JMS_QUEUE = "org.jboss.cdi.tck.testJmsQueue";

    public static final String TEST_JMS_TOPIC = "org.jboss.cdi.tck.testJmsTopic";

    public static final String TEST_TIMEOUT_FACTOR = "org.jboss.cdi.tck.testTimeoutFactor";

    public static final int TEST_TIMEOUT_FACTOR_DEFAULT_VALUE = 100;

    /**
     * The CDI Lite mode setting.
     * If enabled, the following settings are ignored and don't have to be provided:
     * <ul>
     * <li>{@link #TEST_DATASOURCE_PROPERTY_NAME}</li>
     * <li>{@link #TEST_JMS_CONNECTION_FACTORY}</li>
     * <li>{@link #TEST_JMS_QUEUE}</li>
     * <li>{@link #TEST_JMS_TOPIC}</li>
     * </ul>
     *
     * @return true if running in a CDI Lite environment
     */
    public Boolean getCdiLiteMode();

    public void setCdiLiteMode(Boolean cdiLiteMode);

    /**
     * The implementation of {@link Beans} in use.
     */
    public Beans getBeans();

    /**
     * The implementation of {@link Contexts} in use.
     */
    public <T extends Context> Contexts<T> getContexts();

    /**
     * The implementation of {@link Contextuals} in use.
     */
    public Contextuals getContextuals();

    /**
     * The implementation of {@link CreationalContexts} in use.
     */
    public CreationalContexts getCreationalContexts();

    /**
     * The implementation of {@link EL} in use.
     */
    public EL getEl();

    public void setBeans(Beans beans);

    public <T extends Context> void setContexts(Contexts<T> contexts);

    public void setContextuals(Contextuals contextuals);

    public void setCreationalContexts(CreationalContexts creationalContexts);

    public void setEl(EL el);

    /**
     * The TCK allows additional libraries to be put in the deployed test artifacts (for example the porting package for the implementation). Any jars in this
     * directory will be added to the deployed artifact.
     *
     * By default no directory is used.
     *
     * @return path to additional libraries
     */
    public String getLibraryDirectory();

    public void setLibraryDirectory(String libraryDir);

    /**
     * Few TCK tests need to work with Java EE services related to persistence (JPA, JTA) - test datasource must be provided. These tests belong to testng group
     * <code>persistence</code>.
     *
     * @return the JNDI name of the test datasource
     */
    public String getTestDataSource();

    /**
     *
     * @param testDatasource
     */
    public void setTestDataSource(String testDatasource);

    /**
     *
     * @return the JNDI name of the test JMS connection factory
     */
    public String getTestJmsConnectionFactory();

    /**
     * @param testJmsConnectionFactory
     */
    public void setTestJmsConnectionFactory(String testJmsConnectionFactory);

    /**
     *
     * @return the JNDI name of the test JMS queue
     */
    public String getTestJmsQueue();

    /**
     * @param testJmsQueue
     */
    public void setTestJmsQueue(String testJmsQueue);

    /**
     *
     * @return the JNDI name of the test JMS topic
     */
    public String getTestJmsTopic();

    /**
     * @param testJmsTopic
     */
    public void setTestJmsTopic(String testJmsTopic);

    /**
     * All tests using some timeout technique (e.g. wait for async processing) should use this value to adjust the final timeout so that it's possible to configure timeouts
     * according to the testing runtime performance and throughput.
     *
     * @return the test timeout factor (in percent)
     */
    public int getTestTimeoutFactor();

    /**
     *
     * @param timeoutFactor
     */
    public void setTestTimeoutFactor(int timeoutFactor);

}