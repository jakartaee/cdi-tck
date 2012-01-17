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
package org.jboss.cdi.tck.tests.context.request.ejb;

import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

@Stateless
@Asynchronous
public class BarBean {

    @Inject
    BeanManager beanManager;

    @Inject
    SimpleRequestBean simpleRequestBean;

    /**
     * Async computation.
     * 
     * @return
     */
    public Future<Double> compute() {

        Double result = null;
        Context requestContext = null;

        try {
            requestContext = beanManager.getContext(RequestScoped.class);
        } catch (ContextNotActiveException e) {
            // No-op
        }

        if (requestContext == null || !requestContext.isActive() || simpleRequestBean == null) {
            result = -1.00;
        } else {
            result = simpleRequestBean.getId();
        }
        return new AsyncResult<Double>(result);
    }

}
