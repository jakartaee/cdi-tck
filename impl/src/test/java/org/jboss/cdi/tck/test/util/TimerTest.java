/*
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
package org.jboss.cdi.tck.test.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.jboss.cdi.tck.impl.ConfigurationFactory;
import org.jboss.cdi.tck.util.Timer;
import org.jboss.cdi.tck.util.Timer.ResolutionLogic;
import org.jboss.cdi.tck.util.Timer.StopCondition;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.internal.thread.ThreadTimeoutException;

public class TimerTest {

    @BeforeMethod
    public void beforeTestMethod() {
        ConfigurationFactory.get().setTestTimeoutFactor(100);
    }

    @Test(timeOut = 1000l, expectedExceptions = { ThreadTimeoutException.class })
    public void testNoCondition() throws InterruptedException {
        new Timer().setDelay(2000l).start();
    }

    @Test(timeOut = 1000l)
    public void testSingleCondition() throws InterruptedException {

        Timer timer = new Timer().setDelay(5000l).addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        }).start();
        assertTrue(timer.isStopConditionsSatisfiedBeforeTimeout());
    }

    @Test(timeOut = 1000l)
    public void testMultipleConditionDisjunction() throws InterruptedException {

        Timer timer = new Timer().setDelay(5000l).addStopCondition(new StopCondition() {
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
        assertTrue(timer.isStopConditionsSatisfiedBeforeTimeout());
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

    @Test(timeOut = 5000l)
    public void testReuse() throws InterruptedException {

        // Will be stopped immediately
        Timer timer = new Timer().setDelay(2000l).addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return true;
            }
        }).start();
        assertTrue(timer.isStopConditionsSatisfiedBeforeTimeout());

        // Will be stopped after timeout (2s) exceeds
        timer.addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return false;
            }
        }, true).start();
        assertFalse(timer.isStopConditionsSatisfiedBeforeTimeout());

        timer.reset();

        // And finally half-second
        timer.setDelay(500l);
        timer.start();
    }

    @Test(timeOut = 1000l, expectedExceptions = { ThreadTimeoutException.class })
    public void testTimeoutFactor() throws InterruptedException {
        ConfigurationFactory.get().setTestTimeoutFactor(200);
        new Timer().setDelay(1000).start();
    }

    @Test
    public void testTimeUnits() {
        Timer timer = new Timer();
        timer.setDelay(2, TimeUnit.SECONDS);
        assertEquals(2000, timer.getDelay());
        timer.setDelay(1, TimeUnit.MINUTES);
        assertEquals(60000, timer.getDelay());
        try {
            timer.setDelay(-10, TimeUnit.MILLISECONDS);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            timer.setDelay(0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testDelayAdjustment() {
        ConfigurationFactory.get().setTestTimeoutFactor(200);
        Timer timer = new Timer();
        timer.setDelay(2, TimeUnit.SECONDS);
        assertEquals(4000, timer.getDelay());
        timer.setDelay(10);
        assertEquals(20, timer.getDelay());
        ConfigurationFactory.get().setTestTimeoutFactor(25);
        timer.setDelay(888);
        assertEquals(222, timer.getDelay());
        timer.setDelay(13);
        assertEquals(4, timer.getDelay());
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void testNoDelaySet() throws InterruptedException {
        new Timer().start();
    }

}
