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
package org.jboss.cdi.tck.util;

import org.jboss.cdi.tck.util.Timer;
import org.jboss.cdi.tck.util.Timer.ResolutionLogic;
import org.jboss.cdi.tck.util.Timer.StopCondition;
import org.testng.annotations.Test;
import org.testng.internal.thread.ThreadTimeoutException;

public class TimerTest {

    @Test(timeOut = 1000l, expectedExceptions = { ThreadTimeoutException.class })
    public void testNoCondition() throws InterruptedException {
        new Timer().setDelay(2000l).start();
    }

    @Test(timeOut = 1000l)
    public void testSingleCondition() throws InterruptedException {

        new Timer().setDelay(5000l).addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        }).start();
    }

    @Test(timeOut = 1000l)
    public void testMultipleConditionDisjunction() throws InterruptedException {

        new Timer().setDelay(5000l).addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        }).addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        }).start();
    }

    @Test(timeOut = 1000l, expectedExceptions = { ThreadTimeoutException.class })
    public void testMultipleConditionConjunction() throws InterruptedException {

        new Timer().setDelay(5000l).setResolutionLogic(ResolutionLogic.CONJUNCTION).addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        }).addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        }).start();
    }

    @Test(timeOut = 7000l)
    public void testReuse() throws InterruptedException {

        // Will be stopped immediately
        Timer timer = new Timer().setDelay(5000l).addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        }).start();

        // Will be stopped after timeout (5s) exceeds
        timer.addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        }, true).start();

        timer.reset();

        // And finally one more second
        timer.start();
    }

}
