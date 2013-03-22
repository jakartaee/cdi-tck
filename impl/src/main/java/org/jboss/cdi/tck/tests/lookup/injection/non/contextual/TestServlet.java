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
package org.jboss.cdi.tck.tests.lookup.injection.non.contextual;

import java.io.IOException;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {

    @Inject
    private Sheep sheep;

    @Resource(name = "greeting")
    String greeting;

    private boolean injectionPerformedCorrectly = false;
    private boolean initializerCalled = false;
    private boolean initCalledAfterInitializer = false;
    private boolean initCalledAfterResourceInjection = false;

    private static final long serialVersionUID = -7672096092047821010L;

    @Inject
    public void initialize(Sheep sheep) {
        initializerCalled = sheep != null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().endsWith("Servlet")) {
            testServlet(req, resp);
        } else if (req.getRequestURI().endsWith("ServletListener")) {
            testListener(req, resp);
        } else if (req.getRequestURI().endsWith("TagLibraryListener")) {
            testTagLibraryListener(req, resp);
        } else {
            resp.setStatus(404);
        }
    }

    private void testServlet(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameter("test").equals("injection")) {
            // Return 200 if injection into Servlet occurred, 500 otherwise
            resp.setStatus(injectionPerformedCorrectly ? 200 : 500);
        } else if (req.getParameter("test").equals("initializer")) {
            // Return 200 if the initializer was called, 500 otherwise
            resp.setStatus(initCalledAfterInitializer ? 200 : 500);
        } else if (req.getParameter("test").equals("resource")) {
            // Return 200 if the resource was injected before init, 500 otherwise
            resp.setStatus(initCalledAfterResourceInjection ? 200 : 500);
        } else {
            resp.setStatus(404);
        }
    }

    private void testListener(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameter("test").equals("injection")) {
            // Return 200 if injection into Listener occurred, 500 otherwise
            boolean result = (Boolean) req.getSession().getServletContext().getAttribute("listener.injected");
            resp.setStatus((result) ? 200 : 500);
        } else if (req.getParameter("test").equals("initializer")) {
            // Return 200 if the initializer was called, 500 otherwise
            boolean result = (Boolean) req.getSession().getServletContext().getAttribute("listener.initializer.called");
            resp.setStatus((result) ? 200 : 500);
        } else {
            resp.setStatus(404);
        }
    }

    private void testTagLibraryListener(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getParameter("test").equals("injection")) {
            // Return 200 if injection into TagLibrary Listener occurred, 500 otherwise
            boolean result = (Boolean) req.getSession().getServletContext().getAttribute("tag.library.listener.injected");
            resp.setStatus((result) ? 200 : 500);
        } else if (req.getParameter("test").equals("initializer")) {
            // Return 200 if the initializer was called, 500 otherwise
            boolean result = (Boolean) req.getSession().getServletContext()
                    .getAttribute("tag.library.listener.initializer.called");
            resp.setStatus((result) ? 200 : 500);
        } else {
            resp.setStatus(404);
        }
    }

    @Override
    public void init() throws ServletException {
        injectionPerformedCorrectly = sheep != null;
        initCalledAfterInitializer = initializerCalled;
        initCalledAfterResourceInjection = "Hello".equals(greeting);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        init();
    }
}
