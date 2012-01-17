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

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/")
@SuppressWarnings("serial")
public class LowercaseConverterServlet extends HttpServlet {

    @Inject
    LowercaseConverter lowercaseConverter;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getSession().setAttribute(LowercaseConverter.TEXT, null);
        req.getSession().getServletContext().setAttribute(LowercaseConverter.TEXT, "ServletCONTEXT");

        if (req.getRequestURI().contains("/convert-request")) {
            resp.getWriter().append(lowercaseConverter.convert(null));
        } else if (req.getRequestURI().contains("/convert-session")) {
            req.getSession().setAttribute(LowercaseConverter.TEXT, "SesSion");
            resp.getWriter().append(lowercaseConverter.convert(null));
        } else if (req.getRequestURI().contains("/convert-context")) {
            req.getSession().getServletContext().setAttribute(LowercaseConverter.TEXT, "ServletCONTEXT");
            resp.getWriter().append(lowercaseConverter.convert(null));
        }
        resp.setContentType("text/plain");
    }
}
