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

package org.jboss.cdi.tck.impl.testng;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Intended for debug purpose only.
 * 
 * @author Martin Kouba
 * 
 */
public class ProgressLoggingTestListener implements ITestListener {

    private final Logger logger = Logger.getLogger(ProgressLoggingTestListener.class.getName());

    private final AtomicInteger testStarted = new AtomicInteger(0);

    private int totalCountOfMethods = 0;

    @Override
    public void onTestStart(ITestResult result) {
        logger.log(Level.INFO, "On test start {0}/{1}", new Object[] { testStarted.incrementAndGet(), totalCountOfMethods });
    }

    @Override
    public void onTestSuccess(ITestResult result) {
    }

    @Override
    public void onTestFailure(ITestResult result) {
    }

    @Override
    public void onTestSkipped(ITestResult result) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onStart(ITestContext context) {
        // For some reason this is only called once and not per test class as javadoc claims
        totalCountOfMethods = context.getSuite().getAllMethods().size();
    }

    @Override
    public void onFinish(ITestContext context) {
    }

}
