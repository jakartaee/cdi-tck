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

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestResult;

/**
 * Intended for debug purpose only.
 *
 * @author Martin Kouba
 */
public class ProgressLoggingTestListener implements IInvokedMethodListener {

    private final Logger logger = Logger.getLogger(ProgressLoggingTestListener.class.getName());

    private final AtomicInteger testMethodInvocations = new AtomicInteger(0);

    private final AtomicInteger processedTestClasses = new AtomicInteger(0);

    private Integer totalCountOfMethods = null;

    private String lastTestClassName = null;

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
        if (!method.isTestMethod()) {
            return;
        }
        // Note that TCK is processing test classes sequentially in one thread - see also SingleTestClassMethodInterceptor
        if (totalCountOfMethods == null) {
            totalCountOfMethods = context.getSuite().getAllMethods().size();
        }
        String testClassName = method.getTestMethod().getTestClass().getName();
        if (!testClassName.equals(lastTestClassName)) {
            processedTestClasses.incrementAndGet();
            lastTestClassName = testClassName;
        }

        logger.log(Level.INFO, "Invoke {0}.{1}: {2}/{3} Failed tests: {4} ({5})",
                new Object[] { method.getTestMethod().getTestClass().getRealClass().getSimpleName(),
                        method.getTestMethod().getMethodName(), testMethodInvocations.incrementAndGet(), totalCountOfMethods, context.getFailedTests().size(),
                        processedTestClasses.get() });
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
    }

}
