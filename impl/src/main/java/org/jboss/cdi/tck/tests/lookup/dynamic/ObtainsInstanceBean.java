/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.lookup.dynamic;

import static org.jboss.cdi.tck.tests.lookup.dynamic.PayBy.PaymentMethod.CHEQUE;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@Dependent
public class ObtainsInstanceBean {
    @Inject
    @PayBy(CHEQUE)
    Instance<AsynchronousPaymentProcessor> paymentProcessor;

    @Inject
    @Any
    Instance<PaymentProcessor> anyPaymentProcessor;

    @Inject
    Instance<Common> common;

    public Instance<AsynchronousPaymentProcessor> getPaymentProcessor() {
        return paymentProcessor;
    }

    public Instance<PaymentProcessor> getAnyPaymentProcessor() {
        return anyPaymentProcessor;
    }

    public Instance<Common> getCommon() {
        return common;
    }
}
