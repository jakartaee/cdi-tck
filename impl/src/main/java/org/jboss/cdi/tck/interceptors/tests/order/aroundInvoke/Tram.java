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
package org.jboss.cdi.tck.interceptors.tests.order.aroundInvoke;

import static org.testng.Assert.assertEquals;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;

@Interceptors({ Interceptor1.class, Interceptor3.class })
class Tram extends RailVehicle {

    @Interceptors({ Interceptor4.class, Interceptor5.class })
    public int getId() {
        return 0;
    }

    @AroundInvoke
    public Object intercept3(InvocationContext ctx) throws Exception {
        int id = (Integer) ctx.proceed();
        assertEquals(id, 0);
        return id + 1;
    }
}
