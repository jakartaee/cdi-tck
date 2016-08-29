/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.configurators.invalid;

import java.io.IOException;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * We need a servlet to retrieve test results (from extensions) as the test is a client one
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@WebServlet("/test")
public class TestServlet extends HttpServlet {

    // we need Instance amd re-type because every deployment will only have one extension at a time
    @Inject
    Instance<Extension> extension;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        resp.setContentType("text/text");

        if (req.getParameter("ext").equals(ConfiguratorAndSetMethodTest.PAT)) {
            resp.getWriter().print(extension.select(PATExtension.class, Any.Literal.INSTANCE).get().ISE_CAUGHT);
        } else if (req.getParameter("ext").equals(ConfiguratorAndSetMethodTest.PAT_REVERSE)) {
            resp.getWriter().print(extension.select(PATReverseExtension.class, Any.Literal.INSTANCE).get().ISE_CAUGHT);
        } else if (req.getParameter("ext").equals(ConfiguratorAndSetMethodTest.PBA)) {
            resp.getWriter().print(extension.select(PBAExtension.class, Any.Literal.INSTANCE).get().ISE_CAUGHT);
        } else if (req.getParameter("ext").equals(ConfiguratorAndSetMethodTest.PBA_REVERSE)) {
            resp.getWriter().print(extension.select(PBAReverseExtension.class, Any.Literal.INSTANCE).get().ISE_CAUGHT);
        } else if (req.getParameter("ext").equals(ConfiguratorAndSetMethodTest.PIP)) {
            resp.getWriter().print(extension.select(PIPExtension.class, Any.Literal.INSTANCE).get().ISE_CAUGHT);
        } else if (req.getParameter("ext").equals(ConfiguratorAndSetMethodTest.PIP_REVERSE)) {
            resp.getWriter().print(extension.select(PIPReverseExtension.class, Any.Literal.INSTANCE).get().ISE_CAUGHT);
        } else if (req.getParameter("ext").equals(ConfiguratorAndSetMethodTest.POM)) {
            resp.getWriter().print(extension.select(POMExtension.class, Any.Literal.INSTANCE).get().ISE_CAUGHT);
        } else if (req.getParameter("ext").equals(ConfiguratorAndSetMethodTest.POM_REVERSE)) {
            resp.getWriter().print(extension.select(POMReverseExtension.class, Any.Literal.INSTANCE).get().ISE_CAUGHT);
        } else {
            resp.getWriter().println("error: bad parameter value?");
        }
    }
}
