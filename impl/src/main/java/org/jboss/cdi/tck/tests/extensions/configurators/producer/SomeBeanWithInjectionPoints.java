/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.configurators.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * All IPs are intentionally unsatisfied.
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@ApplicationScoped
public class SomeBeanWithInjectionPoints {

    @Inject
    @NoProducerCanCreateThis
    private Bar barOne;

    @Inject
    @NoProducerCanCreateThis
    private Bar barTwo;

    @Inject
    @NoProducerCanCreateThis
    private Bar barThree;

    @Inject
    @NoProducerCanCreateThis
    private Bar barFour;

    @Inject
    @NoProducerCanCreateThis
    private Bar barFive;

    @Inject
    @NoProducerCanCreateThis
    private Bar barSix;

    @Inject
    @NoProducerCanCreateThis
    private Bar barSeven;

    @Inject
    @NoProducerCanCreateThis
    private Bar barEight;

    public Bar getBarEight() {
        return barEight;
    }

    public Bar getBarFive() {
        return barFive;
    }

    public Bar getBarFour() {
        return barFour;
    }

    public Bar getBarOne() {
        return barOne;
    }

    public Bar getBarSeven() {
        return barSeven;
    }

    public Bar getBarSix() {
        return barSix;
    }

    public Bar getBarThree() {
        return barThree;
    }

    public Bar getBarTwo() {
        return barTwo;
    }
}
