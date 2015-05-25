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
package org.jboss.cdi.tck.interceptors.tests.contract.aroundInvoke;

import static org.testng.AssertJUnit.assertTrue;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

@DeclareRoles(value = { "students", "printers" })
public class PrinterSecurityInterceptor {

    public static boolean securityContextOK = false;

    @Resource
    private SessionContext sc;

    @EJB
    private Toner toner;

    @AroundInvoke
    public Object intercept(InvocationContext ic) throws Exception {
        Object obj = ic.proceed();

        assertTrue(this.sc.isCallerInRole("students"));
        assertTrue(!this.sc.isCallerInRole("printers"));
        securityContextOK = true;

        toner.callFromInterceptor();

        return obj;
    }
}
