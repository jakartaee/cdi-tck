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
package org.jboss.cdi.tck.interceptors.tests.contract.aroundTimeout.bindings;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.testng.Assert.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.cdi.tck.util.Timer;
import org.jboss.cdi.tck.util.Timer.StopCondition;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Matus Abaffy
 */
@SpecVersion(spec = "int", version = "1.2")
public class AroundTimeoutOrderInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AroundTimeoutOrderInterceptorTest.class).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = "2.3", id = "hb"), @SpecAssertion(section = "2.7", id = "ba"),
            @SpecAssertion(section = "2.7", id = "bb"), @SpecAssertion(section = "5.2.1", id = "aa"),
            @SpecAssertion(section = "5.2.1", id = "ab"), @SpecAssertion(section = "5.2.2", id = "a") })
    public void testTimeoutMethodIntercepted(TimingBean timing) throws Exception {
        ActionSequence.reset();

        timing.createTimer();
        new Timer().setDelay(5, TimeUnit.SECONDS).addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return TimingBean.timeoutAt != null;
            }
        }).start();

        assertNotNull(TimingBean.timeoutAt);

        ActionSequence.assertSequenceDataContainsAll(SuperInterceptor1.class, MiddleInterceptor1.class, Interceptor1.class,
                SuperInterceptor2.class, Interceptor2.class, SuperTimingBean.class, MiddleTimingBean.class, TimingBean.class);

        ActionSequence.assertSequenceDataEquals(SuperInterceptor1.class, MiddleInterceptor1.class, Interceptor1.class,
                SuperInterceptor2.class, Interceptor2.class, SuperTimingBean.class, MiddleTimingBean.class, TimingBean.class);
    }
}
