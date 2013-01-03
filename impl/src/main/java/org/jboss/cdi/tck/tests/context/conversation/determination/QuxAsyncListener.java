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

package org.jboss.cdi.tck.tests.context.conversation.determination;

import java.io.IOException;

import javax.enterprise.context.Conversation;
import javax.inject.Inject;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

/**
 * @author Martin Kouba
 * 
 */
public class QuxAsyncListener implements AsyncListener {

    @Inject
    Conversation conversation;

    @Inject
    TestResult testResult;

    @Override
    public void onComplete(AsyncEvent event) throws IOException {

        if ("test".equals(event.getSuppliedRequest().getParameter("action"))) {
            if (FooServlet.CID.equals(conversation.getId())) {
                // The long-running conversation is available
                testResult.setAsyncListenerOk();
            }
        }

        event.getAsyncContext().getResponse().setContentType("text/plain");
        event.getAsyncContext().getResponse().getWriter().append(testResult.toString());
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
    }

}
