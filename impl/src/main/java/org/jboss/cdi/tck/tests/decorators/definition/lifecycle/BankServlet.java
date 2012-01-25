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
package org.jboss.cdi.tck.tests.decorators.definition.lifecycle;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/bank")
@SuppressWarnings("serial")
public class BankServlet extends HttpServlet {

    @Inject
    ShortTermAccount shortTermAccount;

    @Inject
    DurableAccount durableAccount;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if ("withdraw".equals(req.getParameter("action"))) {
            shortTermAccount.withdraw(getAmountValue(req));
            durableAccount.withdraw(getAmountValue(req));
        } else if ("deposit".equals(req.getParameter("action"))) {
            shortTermAccount.deposit(getAmountValue(req));
            durableAccount.deposit(getAmountValue(req));
        }

        resp.getWriter().append("ShortTermBalance:" + shortTermAccount.getBalance());
        resp.getWriter().append("\n");
        resp.getWriter().append("DurableBalance:" + durableAccount.getBalance());
        resp.getWriter().append("\n");
        resp.getWriter().append("PostConstructCallers:" + CallStore.getPostConstructCallers().size());
        resp.getWriter().append("\n");
        resp.getWriter().append("PreDestroyCallers:" + CallStore.getPreDestroyCallers().size());
        resp.getWriter().append("\n");

        resp.setContentType("text/plain");
    }

    private Integer getAmountValue(HttpServletRequest req) {
        return Integer.valueOf(req.getParameter("amount"));
    }
}
