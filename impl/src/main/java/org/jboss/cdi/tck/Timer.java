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
package org.jboss.cdi.tck;

import java.util.ArrayList;
import java.util.List;

/**
 * Delays thread execution for specified time or unless stop conditions are satisfied according to actual
 * {@link #resolutionLogic}.
 * <p>
 * Setting the sleep interval to value less than 15 ms is questionable since some operating systems do not provide such
 * precision. Moreover such values may impact test performance.
 * </p>
 * <p>
 * In case of no stop conditions are specified, timer logic corresponds to regular {@link Thread#sleep(long)} execution.
 * </p>
 */
public class Timer {

    private static final long DEFAULT_DELAY = 1000l;

    private static final long DEFAULT_SLEEP_INTERVAL = 50l;

    private static final ResolutionLogic DEFAULT_RESOLUTION_LOGIC = ResolutionLogic.DISJUNCTION;

    /**
     * Delay in ms
     */
    private long delay = DEFAULT_DELAY;

    /**
     * Thread sleep interval
     */
    private long sleepInterval = DEFAULT_SLEEP_INTERVAL;

    /**
     * Stop conditions
     */
    private List<StopCondition> stopConditions = null;

    /**
     * Stop conditions resolution logic
     */
    private ResolutionLogic resolutionLogic = DEFAULT_RESOLUTION_LOGIC;

    /**
     * Create new timer with default delay and sleep interval.
     * 
     * @param delay
     */
    public Timer() {
        super();
    }

    /**
     * Set new delay value.
     * 
     * @param delay
     * @return self
     */
    public Timer setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    /**
     * Set new sleep interval value.
     * 
     * @param sleepInterval
     * @return self
     */
    public Timer setSleepInterval(long sleepInterval) {
        this.sleepInterval = sleepInterval;
        return this;
    }

    /**
     * Set new resolution logic.
     * 
     * @param resolutionLogic
     * @return self
     */
    public Timer setResolutionLogic(ResolutionLogic resolutionLogic) {
        this.resolutionLogic = resolutionLogic;
        return this;
    }

    /**
     * Add new stop condition.
     * 
     * @param condition
     * @return self
     */
    public Timer addStopCondition(StopCondition condition) {
        return addStopCondition(condition, false);
    }

    /**
     * Add new stop condition.
     * 
     * @param condition
     * @param clear Clear stop conditions
     * @return self
     */
    public Timer addStopCondition(StopCondition condition, boolean clear) {

        if (condition != null) {

            if (stopConditions == null) {
                stopConditions = new ArrayList<Timer.StopCondition>(5);
            } else if (clear) {
                clearStopConditions();
            }
            stopConditions.add(condition);
        }
        return this;
    }

    /**
     * Start the timer.
     * 
     * @throws InterruptedException
     */
    public Timer start() throws InterruptedException {

        checkConfiguration();

        if (stopConditions == null || stopConditions.isEmpty()) {
            Thread.sleep(delay);
        } else {

            long start = System.currentTimeMillis();

            while (!hasConditionsSatisfied() && !isTimeoutExpired(start)) {
                Thread.sleep(sleepInterval);
            }
        }
        return this;
    }

    /**
     * Reset to default values.
     */
    public void reset() {
        this.delay = DEFAULT_DELAY;
        this.sleepInterval = DEFAULT_SLEEP_INTERVAL;
        this.resolutionLogic = DEFAULT_RESOLUTION_LOGIC;
        clearStopConditions();
    }

    /**
     * 
     */
    public void clearStopConditions() {
        if (this.stopConditions != null && !this.stopConditions.isEmpty())
            this.stopConditions.clear();
    }

    private void checkConfiguration() {
        if (delay < 0 || sleepInterval < 0 || delay < sleepInterval)
            throw new IllegalArgumentException("Invalid timer configuration");
    }

    private boolean hasConditionsSatisfied() {
        switch (resolutionLogic) {
            case DISJUNCTION:
                return hasAtLeastOneConditionsSatisfied();
            case CONJUNCTION:
                return hasAllConditionsSatisfied();
            default:
                throw new IllegalStateException("Unsupported condition resolution logic");
        }
    }

    private boolean hasAtLeastOneConditionsSatisfied() {

        for (StopCondition condition : stopConditions) {
            if (condition.isSatisfied())
                return true;
        }
        return false;
    }

    private boolean hasAllConditionsSatisfied() {

        for (StopCondition condition : stopConditions) {
            if (!condition.isSatisfied())
                return false;
        }
        return true;
    }

    private boolean isTimeoutExpired(long start) {
        return (System.currentTimeMillis() - start) >= delay;
    }

    /**
     * 
     */
    public interface StopCondition {

        /**
         * @return <code>true</code> if stop condition satisfied, <code>false</code> otherwise
         */
        public boolean isSatisfied();

    }

    public enum ResolutionLogic {

        /**
         * At least one condition must be satisfied
         */
        DISJUNCTION,
        /**
         * All conditions must be satisfied
         */
        CONJUNCTION, ;

    }

}