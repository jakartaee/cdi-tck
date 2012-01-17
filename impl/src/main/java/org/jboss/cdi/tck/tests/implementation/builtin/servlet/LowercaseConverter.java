/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cdi.tck.tests.implementation.builtin.servlet;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
@SessionScoped
public class LowercaseConverter implements Serializable {

    protected static final String TEXT = "text";

    private long id = System.currentTimeMillis();

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private HttpSession httpSession;

    @Inject
    private ServletContext servletContext;

    /**
     * 
     * @param text
     * @return
     */
    public String convert(String text) {
        if (text == null) {
            // Request
            text = httpServletRequest.getParameter(TEXT);
            if (text == null) {
                // Session
                text = (String) httpSession.getAttribute(TEXT);
                if (text == null) {
                    // Servlet context
                    text = (String) servletContext.getAttribute(TEXT);
                }
            }
        }
        return text != null ? text.toLowerCase() : null;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public long getId() {
        return id;
    }

}
