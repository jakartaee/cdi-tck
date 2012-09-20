/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.context.request.event.timeout;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.cdi.tck.util.Timer;
import org.jboss.cdi.tck.util.Timer.StopCondition;

/**
 * @author Martin Kouba
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/info")
public class InfoServlet extends HttpServlet {

    @Inject
    ObservingBean observingBean;

    @Inject
    TimeoutService timeoutService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        timeoutService.start();

        try {
            new Timer().setDelay(1000l).addStopCondition(new StopCondition() {
                public boolean isSatisfied() {
                    return TimeoutService.isTimeouted;
                }
            }).start();
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }

        resp.getWriter().append("Timeouted:" + TimeoutService.isTimeouted);
        resp.getWriter().append("\n");
        resp.getWriter().append("Initialized:" + observingBean.getInitializedRequestCount().get());
        resp.getWriter().append("\n");
        resp.getWriter().append("Destroyed:" + observingBean.getDestroyedRequestCount().get());
        resp.setContentType("text/plain");
    }

}
