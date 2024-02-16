/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.interceptors.ejb;

import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.ejb.Timeout;
import jakarta.ejb.Timer;
import org.jboss.cdi.tck.tests.interceptors.invocation.AlmightyBinding;

@AlmightyBinding
@Stateless
public class Timing {

    public static Long timeoutAt = null;

    @Resource
    private SessionContext ctx;

    public void createTimer() {
        ctx.getTimerService().createTimer(1l, null);
    }

    @Timeout
    public void handleTimeout(Timer timer) {
        timeoutAt = System.currentTimeMillis();
    }

}
