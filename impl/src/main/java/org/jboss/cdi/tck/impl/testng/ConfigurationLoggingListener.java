/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.impl.testng;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.jboss.cdi.tck.api.Configuration;
import org.jboss.cdi.tck.impl.ConfigurationFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class ConfigurationLoggingListener implements ISuiteListener {

    private static final String CONFIGURATION_FILE_PATH = "target/cdi-tck-configuration.log";
    private final Logger logger = Logger.getLogger(ConfigurationLoggingListener.class.getName());

    @Override
    public void onStart(ISuite suite) {
        try {
            FileHandler fh = new FileHandler(CONFIGURATION_FILE_PATH);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "CDI-TCK Implementation version: {0}", Package.getPackage("org.jboss.cdi.tck.impl.testng").getImplementationVersion());
        logger.log(Level.INFO, "CDI-TCK Specification version: {0}", Package.getPackage("org.jboss.cdi.tck.impl.testng").getSpecificationVersion());
        Configuration conf = ConfigurationFactory.get();
        logger.log(Level.INFO, conf.toString());
    }

    @Override
    public void onFinish(ISuite suite) {

    }

}
