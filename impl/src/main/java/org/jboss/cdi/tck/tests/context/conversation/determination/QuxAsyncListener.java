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
import javax.servlet.ServletResponse;

/**
 * @author Martin Kouba
 * 
 */
public class QuxAsyncListener implements AsyncListener {

    @Inject
    Conversation conversation;

    public static boolean onStartAsync = false;
    public static boolean onError = false;
    public static boolean onTimeout = false;
    public static boolean onComplete = false;

    @Override
    public void onComplete(AsyncEvent event) throws IOException {

        if (!onTimeout && !onError) {
            onComplete = checkSameConversationActive();
            writeInfo(event.getAsyncContext().getResponse());
        }
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        onTimeout = checkSameConversationActive();
        writeInfo(event.getAsyncContext().getResponse());
        event.getAsyncContext().complete();
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        onError = checkSameConversationActive();
        writeInfo(event.getAsyncContext().getResponse());
        event.getAsyncContext().complete();
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        onStartAsync = checkSameConversationActive();
    }

    private void writeInfo(ServletResponse response) throws IOException {
        response.getWriter().print(getInfo());
        response.getWriter().flush();
        response.getWriter().close();
    }

    public static String getInfo() {
        return String
                .format("onStartAsync: %s, onError: %s, onTimeout: %s, onComplete: %s", onStartAsync, onError, onTimeout, onComplete);
    }

    public boolean checkSameConversationActive(){
        return FooServlet.CID.equals(conversation.getId());
    }

    public static void reset() {
        onStartAsync = false;
        onError = false;
        onTimeout = false;
        onComplete = false;
    }

}
