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
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;

/**
 * Producer class. Contains loads of producer methods all of which are altered in extension.
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@ApplicationScoped
public class BarProducer {

    @Produces
    @OneAmongMany
    public Bar produceBarOne() {
        return new Bar(OneAmongMany.class);
    }

    @Produces
    @YetAnother
    public Bar produceBarTwo() {
        return new Bar(YetAnother.class);
    }

    @Produces
    @Some
    public Bar produceBarThree() {
        return new Bar(Some.class);
    }

    @Produces
    @Default
    public Bar produceBarFour() {
        return new Bar(Default.class);
    }

    @Produces
    @Another
    public Bar alterProduceAndDispose() {
        return new Bar(null);
    }
}
