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

package org.jboss.cdi.tck.tests.context.request.async;

import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import org.jboss.cdi.tck.util.SimpleLogger;

/**
 * @author Martin Kouba
 * @author Tomas Remes
 */
public class SimpleAsyncListener implements AsyncListener {

    public static boolean isRequestContextActive = false;
    private static final SimpleLogger logger = new SimpleLogger(SimpleAsyncListener.class);

    @Inject
    SimpleRequestBean simpleRequestBean;

    @Inject
    BeanManager beanManager;

    @Inject
    StatusBean statusBean;

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.AsyncListener#onComplete(javax.servlet.AsyncEvent)
     */
    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        logger.log("onComplete");

        if (!statusBean.isOnTimeout() && !statusBean.isOnError()) {
            // Do not check and write info in case of post timeout/error action
            statusBean.setOnComplete(checkRequestContextAvailability());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.AsyncListener#onTimeout(javax.servlet.AsyncEvent)
     */
    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        logger.log("onTimeout");
        statusBean.setOnTimeout(checkRequestContextAvailability());
        event.getAsyncContext().complete();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.AsyncListener#onError(javax.servlet.AsyncEvent)
     */
    @Override
    public void onError(AsyncEvent event) throws IOException {
        logger.log("onError");
        statusBean.setOnError(checkRequestContextAvailability());
        event.getAsyncContext().complete();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.AsyncListener#onStartAsync(javax.servlet.AsyncEvent)
     */
    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        logger.log("onStartAsync");
        statusBean.setOnStartAsync(checkRequestContextAvailability());
    }

    private boolean checkRequestContextAvailability() throws IOException {
        try {
            statusBean.setRequestBeanId(simpleRequestBean.getId());
            isRequestContextActive = beanManager.getContext(RequestScoped.class).isActive();
        } catch (Throwable e) {
            logger.log("Problem while checking request scope: " + e.getMessage());
        }
        if (!isRequestContextActive || statusBean.getRequestBeanId() == null) {
            return false;
        }
        return true;
    }
}
