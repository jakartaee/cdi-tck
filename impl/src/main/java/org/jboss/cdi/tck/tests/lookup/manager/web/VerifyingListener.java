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
package org.jboss.cdi.tck.tests.lookup.manager.web;

import javax.enterprise.inject.spi.BeanManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Verifies that the correct BeanManager can be obtained from the ServletContext early in the deployment - during
 * ServletContextListener notification.
 * 
 * @author Jozef Hartinger
 * 
 */
@WebListener
public class VerifyingListener implements ServletContextListener {

    private boolean invoked;

    public void contextInitialized(ServletContextEvent sce) {
        if (invoked) {
            throw new IllegalStateException("ServletContextEvent observed multiple times");
        }
        invoked = true;
        // we can obtain the manager but cannot verify if this is the correct one
        // we'll pass it to the servlet under a different namespace to verify
        BeanManager manager = (BeanManager) sce.getServletContext().getAttribute(BeanManager.class.getName());
        if (manager == null) {
            throw new IllegalStateException("BeanManager not available in ServletContext");
        }
        sce.getServletContext().setAttribute(VerifyingListener.class.getName(), manager);
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
