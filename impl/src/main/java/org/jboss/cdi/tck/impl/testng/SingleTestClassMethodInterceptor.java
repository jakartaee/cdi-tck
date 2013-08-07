/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cdi.tck.impl.testng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

/**
 * This {@link IMethodInterceptor} fixes one or the other problem with CDI TCK test suite execution:
 * <ol>
 * <li>Run tests from single test class - it seems that Maven Surefire plugin is not able to run single test that is located outside src/test dir. If test class
 * system property is set all test methods that don't belong to specified test class are excluded.</li>
 * <li>Avoid randomly mixed test method execution - causing test archive deployments collisions. If test class system property is not set group test methods by
 * test class.</li>
 * <ol>
 * 
 * @author Stuart Douglas
 * @author Martin Kouba
 */
public class SingleTestClassMethodInterceptor implements IMethodInterceptor {

    public static final String TEST_CLASS_PROPERTY = "tckTest";

    private final Logger logger = Logger.getLogger(SingleTestClassMethodInterceptor.class.getName());

    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {

        if (methods == null || methods.isEmpty() || methods.size() == 1) {
            return methods;
        }

        logger.log(Level.INFO, "Intercepting... [methods: {0}, testRun: {1}, suiteName: {2}]", new Object[] { methods.size(), context.getName(),
                context.getSuite().getName() });

        long start = System.currentTimeMillis();
        String testString = System.getProperty(TEST_CLASS_PROPERTY);
        if (testString == null || testString.isEmpty()) {

            // Run the test suite - group test methods by class name
            Collections.sort(methods, new Comparator<IMethodInstance>() {
                public int compare(IMethodInstance o1, IMethodInstance o2) {
                    int result = o1.getMethod().getTestClass().getName().compareTo(o2.getMethod().getTestClass().getName());
                    return result;
                }
            });
            logger.log(Level.INFO, "tckTest not set [time: {0} ms]", System.currentTimeMillis() - start);
            return methods;
        }

        // Run the tests of a single test class or single method
        List<IMethodInstance> ret = new ArrayList<IMethodInstance>();
        String testClass = null;
        String testMethod = null;
        if (testString.contains("#")) {
            testClass = testString.split("#")[0];
            testMethod = testString.split("#")[1];
        } else {
            testClass = testString;
        }
        if (testString.contains(".")) {
            for (IMethodInstance method : methods) {
                if (method.getMethod().getTestClass().getName().equals(testClass)) {
                    if ((testMethod == null) || (method.getMethod().getMethodName().equals(testMethod))) {
                        ret.add(method);
                    }
                }
            }
        } else {
            for (IMethodInstance method : methods) {
                if (method.getMethod().getTestClass().getName().endsWith("." + testClass)) {
                    if ((testMethod == null) || (method.getMethod().getMethodName().equals(testMethod))) {
                        ret.add(method);
                    }
                }
            }
        }

        logger.log(Level.INFO, "tckTest set to {0} [methods: {1}, time: {2} ms]", new Object[] { testClass, ret.size(), System.currentTimeMillis() - start });
        return ret;
    }

}
