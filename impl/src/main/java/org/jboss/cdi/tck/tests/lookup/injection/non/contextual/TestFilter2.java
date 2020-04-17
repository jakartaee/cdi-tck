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

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;

@WebFilter(urlPatterns = "/TestFilter2")
public class TestFilter2 implements Filter {

    @WebServiceRef(value = TranslatorService.class)
    Translator translatorField;

    Translator translator;

    static boolean initCalledAfterWSResourceInjection = false;

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;

        if (request.getParameter("test").equals("wsresource")) {
            // Return 200 if the resource was injected before init, 500 otherwise
            resp.setStatus(initCalledAfterWSResourceInjection ? 200 : 500);
            resp.getWriter().append("Filter init: "+initCalledAfterWSResourceInjection);
        } else {
            resp.setStatus(404);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        initCalledAfterWSResourceInjection = translator != null & translatorField != null;
    }

    @WebServiceRef(value = TranslatorService.class)
    private void setTranslator(Translator translator) {
        this.translator = translator;
    }
}
