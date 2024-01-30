/*
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.cdi.tck.api.InSequence;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

/**
 * This {@link IMethodInterceptor} fixes one or the other problem with CDI TCK test suite execution:
 * <ul>
 * <li>
 *     Run tests from single test class - it seems that Maven Surefire plugin is not able to run single test that is located outside src/test dir. If test class
 * </li>
 * <li>
 *     system property is set all test methods that don't belong to specified test class are excluded.
 * </li>
 * <li>
 *     Avoid randomly mixed test method execution - causing test archive deployments collisions. If test class system property is not set group test methods by
 * test class.
 * </li>
 * </ul>
 *
 * @author Stuart Douglas
 * @author Martin Kouba
 */
public class SingleTestClassMethodInterceptor implements IMethodInterceptor {

    private final class MethodComparator implements Comparator<IMethodInstance> {
        @Override
        public int compare(IMethodInstance o1, IMethodInstance o2) {
            int result = o1.getMethod().getTestClass().getName().compareTo(o2.getMethod().getTestClass().getName());

            if (result == 0) {
                InSequence inSequence1 = o1.getMethod().getConstructorOrMethod().getMethod().getAnnotation(InSequence.class);
                int o1Priority = inSequence1 != null ? inSequence1.value() : 0;

                InSequence inSequence2 = o2.getMethod().getConstructorOrMethod().getMethod().getAnnotation(InSequence.class);
                int o2Priority = inSequence2 != null ? inSequence2.value() : 0;
                return Integer.compare(o2Priority, o1Priority);
            }
            return result;
        }
    }

    public static final String TEST_CLASS_PROPERTY = "tckTest";

    private final Logger logger = Logger.getLogger(SingleTestClassMethodInterceptor.class.getName());

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {

        if (methods == null || methods.isEmpty()) {
            return methods;
        }

        logger.log(Level.INFO, "Intercepting... [methods: {0}, testRun: {1}, suiteName: {2}]", new Object[] { methods.size(), context.getName(),
                context.getSuite().getName() });

        long start = System.currentTimeMillis();
        String testString = System.getProperty(TEST_CLASS_PROPERTY);

        // Run the test suite - group test methods by class name
        if (testString == null || testString.isEmpty()) {
            Collections.sort(methods, new MethodComparator());
            logger.log(Level.INFO, "tckTest not set [time: {0} ms]", System.currentTimeMillis() - start);
            return methods;
        }

        // Run the tests of each test class with the specified name (there might be more of them)
        // or its specific methods
        List<IMethodInstance> methodsToRun = new ArrayList<IMethodInstance>();
        String testClass = null;
        Set<String> testMethodsSet = new HashSet<String>();
        if (testString.contains("#")) {
            testClass = testString.split("#")[0];
            String[] testMethod = testString.split("#")[1].split("\\+");
            testMethodsSet.addAll(Arrays.asList(testMethod));
        } else {
            testClass = testString;
        }
        if (testClass.contains(".")) {
            for (IMethodInstance method : methods) {
                if (method.getMethod().getTestClass().getName().equals(testClass)) {
                    if ((testMethodsSet.isEmpty()) || (testMethodsSet.contains(method.getMethod().getMethodName()))) {
                        methodsToRun.add(method);
                    }
                }
            }
        } else {
            for (IMethodInstance method : methods) {
                if (method.getMethod().getTestClass().getName().endsWith("." + testClass)) {
                    if ((testMethodsSet.isEmpty()) || (testMethodsSet.contains(method.getMethod().getMethodName()))) {
                        methodsToRun.add(method);
                    }
                }
            }
        }

        Collections.sort(methodsToRun, new MethodComparator());
        logger.log(Level.INFO, "tckTest set to {0} [methods: {1}, time: {2} ms]",
                new Object[] { testClass, methodsToRun.size(), System.currentTimeMillis() - start });
        return methodsToRun;
    }
}
