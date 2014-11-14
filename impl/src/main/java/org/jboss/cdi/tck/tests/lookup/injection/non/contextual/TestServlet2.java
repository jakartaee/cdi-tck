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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;

@WebServlet(urlPatterns = "/TestServlet2", loadOnStartup = 1)
public class TestServlet2 extends HttpServlet {

    @WebServiceRef(value = TranslatorService.class)
    Translator translatorField;

    Translator translator;

    static boolean initCalledAfterWSResourceInjection = false;

    private static final long serialVersionUID = -7672096098457821010L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("test").equals("wsresource")) {
            // Return 200 if the resource was injected before init, 500 otherwise
            resp.setStatus(initCalledAfterWSResourceInjection ? 200 : 500);
            resp.getWriter().append("Servlet init: "+initCalledAfterWSResourceInjection);
        } else {
            resp.setStatus(404);
        }
    }

    @Override
    public void init() throws ServletException {
        this.initCalledAfterWSResourceInjection = translator != null & translatorField != null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        init();
    }

    @WebServiceRef(value = TranslatorService.class)
    private void setTranslator(Translator translator) {
        this.translator = translator;
    }
}
