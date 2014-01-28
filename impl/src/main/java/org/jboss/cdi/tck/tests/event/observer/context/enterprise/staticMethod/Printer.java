/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.observer.context.enterprise.staticMethod;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

@Stateless
@RunAs("printer")
@RolesAllowed("student")
@TransactionManagement(TransactionManagementType.BEAN)
public class Printer {

    @EJB
    private Toner toner;

    @Resource
    private TransactionSynchronizationRegistry tsr;

    @Resource
    private UserTransaction userTransaction;

    @Inject
    Event<Foo> event;

    private static Object key;

    public static Object getKey() {
        return key;
    }

    public void printSuccess() throws Exception {
        userTransaction.begin();

        // we need to set the key before firing an event as the observer checks for equality
        key = tsr.getTransactionKey();

        toner.spill();
        event.fire(new Foo());

        userTransaction.commit();
    }

    public void printFailure() throws Exception {
        userTransaction.begin();

        // we need to set the key before firing an event as the observer checks for equality
        key = tsr.getTransactionKey();

        toner.spill();
        event.fire(new Foo());

        userTransaction.rollback();
    }

    public void tryAccess() {
    }
}
