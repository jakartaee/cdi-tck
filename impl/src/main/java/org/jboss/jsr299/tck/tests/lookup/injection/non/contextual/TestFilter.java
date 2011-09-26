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
package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class TestFilter implements Filter {

    @Inject
    private Sheep sheep;
    private boolean injectionPerformedCorrectly = false;
    private boolean initializerCalled = false;
    private boolean initCalledAfterInitializer = false;

    @Inject
    public void initialize(Sheep sheep) {
        initializerCalled = sheep != null;
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;

        if (request.getParameter("test").equals("injection")) {
            // Return 200 if injection into Filter occurred, 500 otherwise
            resp.setStatus(injectionPerformedCorrectly ? 200 : 500);
        } else if (request.getParameter("test").equals("initializer")) {
            // Return 200 if initializer was called, 500 otherwise
            resp.setStatus(initCalledAfterInitializer ? 200 : 500);
        } else {
            resp.setStatus(404);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        injectionPerformedCorrectly = sheep != null;
        initCalledAfterInitializer = initializerCalled;
    }
}
