/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

import jakarta.enterprise.context.spi.Context;

import org.jboss.cdi.tck.api.Configuration;
import org.jboss.cdi.tck.spi.Beans;
import org.jboss.cdi.tck.spi.Contexts;
import org.jboss.cdi.tck.spi.Contextuals;
import org.jboss.cdi.tck.spi.CreationalContexts;
import org.jboss.cdi.tck.spi.EL;

/**
 * CDI TCK configuration implementation.
 *
 * @author Pete Muir
 * @author Martin Kouba
 */
public class ConfigurationImpl implements Configuration {

    private Boolean cdiLiteMode;

    private Beans beans;
    private Contexts<? extends Context> contexts;
    private Contextuals contextuals;
    private CreationalContexts creationalContexts;
    private EL el;

    private String libraryDirectory;

    private String testDataSource;
    private String testJmsConnectionFactory;
    private String testJmsQueue;
    private String testJmsTopic;

    private int testTimeoutFactor;

    protected ConfigurationImpl() {
    }

    public Boolean getCdiLiteMode() {
        return cdiLiteMode;
    }

    public void setCdiLiteMode(Boolean cdiLiteMode) {
        this.cdiLiteMode = cdiLiteMode;
    }

    public Beans getBeans() {
        return beans;
    }

    public void setBeans(Beans beans) {
        this.beans = beans;
    }

    @SuppressWarnings("unchecked")
    public <T extends Context> Contexts<T> getContexts() {
        return (Contexts<T>) contexts;
    }

    public <T extends Context> void setContexts(Contexts<T> contexts) {
        this.contexts = contexts;
    }

    public Contextuals getContextuals() {
        return contextuals;
    }

    public void setContextuals(Contextuals contextuals) {
        this.contextuals = contextuals;
    }

    public CreationalContexts getCreationalContexts() {
        return creationalContexts;
    }

    public void setCreationalContexts(CreationalContexts creationalContexts) {
        this.creationalContexts = creationalContexts;
    }

    public EL getEl() {
        return el;
    }

    public void setEl(EL el) {
        this.el = el;
    }

    public String getLibraryDirectory() {
        return libraryDirectory;
    }

    public void setLibraryDirectory(String libraryDirectory) {
        this.libraryDirectory = libraryDirectory;
    }

    public String getTestDataSource() {
        return testDataSource;
    }

    public void setTestDataSource(String testDataSource) {
        this.testDataSource = testDataSource;
    }

    @Override
    public String getTestJmsConnectionFactory() {
        return testJmsConnectionFactory;
    }

    @Override
    public void setTestJmsConnectionFactory(String testJmsConnectionFactory) {
        this.testJmsConnectionFactory = testJmsConnectionFactory;
    }

    @Override
    public String getTestJmsQueue() {
        return testJmsQueue;
    }

    @Override
    public void setTestJmsQueue(String testJmsQueue) {
        this.testJmsQueue = testJmsQueue;
    }

    @Override
    public String getTestJmsTopic() {
        return testJmsTopic;
    }

    @Override
    public void setTestJmsTopic(String testJmsTopic) {
        this.testJmsTopic = testJmsTopic;
    }

    @Override
    public int getTestTimeoutFactor() {
        return testTimeoutFactor;
    }

    @Override
    public void setTestTimeoutFactor(int timeoutFactor) {
        if (timeoutFactor <= 0) {
            throw new IllegalArgumentException("Test timeout factor must be greater than zero");
        }
        this.testTimeoutFactor = timeoutFactor;
    }

    @Override
    public String toString() {
        StringBuilder configuration = new StringBuilder();
        configuration.append("Jakarta CDI 4.0 TCK Configuration\n");
        configuration.append("-----------------\n");
        configuration.append("\tCDI Lite mode: ").append(getCdiLiteMode()).append("\n");
        configuration.append("\tBeans: ").append(getBeans()).append("\n");
        configuration.append("\tContexts: ").append(getContexts()).append("\n");
        configuration.append("\tContextuals: ").append(getContextuals()).append("\n");
        configuration.append("\tCreationalContexts: ").append(getCreationalContexts()).append("\n");
        configuration.append("\tEL: ").append(getEl()).append("\n");
        configuration.append("\tLibrary dir: ").append(getLibraryDirectory()).append("\n");
        configuration.append("\tTest DS: ").append(getTestDataSource()).append("\n");
        configuration.append("\tTest JMS connection factory: ").append(getTestJmsConnectionFactory()).append("\n");
        configuration.append("\tTest JMS queue: ").append(getTestJmsQueue()).append("\n");
        configuration.append("\tTest JMS topic: ").append(getTestJmsTopic()).append("\n");
        configuration.append("\tTest timeout factor: ").append(getTestTimeoutFactor()).append("\n");
        configuration.append("\n");
        return configuration.toString();
    }

}
