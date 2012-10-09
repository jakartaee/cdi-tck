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
package org.jboss.cdi.tck.tests.extensions.processBean;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessBeanAttributes;
import javax.enterprise.inject.spi.ProcessManagedBean;
import javax.enterprise.inject.spi.ProcessProducerField;
import javax.enterprise.inject.spi.ProcessProducerMethod;

import org.jboss.cdi.tck.util.ActionSequence;

public class ProcessBeanObserver implements Extension {

    private static ProcessManagedBean<Cat> catProcessManagedBean;
    private static int catProcessBeanCount;

    // https://issues.jboss.org/browse/WELD-586
    private static ProcessProducerMethod<Cow, Cowshed> cowProcessProducerMethod;
    private static int cowShedProcessBeanCount;

    // https://issues.jboss.org/browse/WELD-586
    private static ProcessProducerField<Chicken, ChickenHutch> chickenProcessProducerField;
    private static int chickenHutchProcessBeanCount;

    private static ActionSequence catActionSeq = new ActionSequence();
    private static ActionSequence cowActionSeq = new ActionSequence();
    private static ActionSequence chickenActionSeq = new ActionSequence();

    public void observeCatManagedBean(@Observes ProcessManagedBean<Cat> event) {
        ProcessBeanObserver.catProcessManagedBean = event;
        ProcessBeanObserver.catProcessBeanCount++;
        catActionSeq.add(ProcessManagedBean.class.getName());
    }

    public void observeCatBean(@Observes ProcessBean<Cat> event) {
        ProcessBeanObserver.catProcessBeanCount++;
    }

    /**
     * https://issues.jboss.org/browse/WELD-586
     * 
     * @param event
     */
    public void observeCowProcessProducerMethod(@Observes ProcessProducerMethod<Cow, Cowshed> event) {
        ProcessBeanObserver.cowProcessProducerMethod = event;
        cowActionSeq.add(ProcessProducerMethod.class.getName());
    }

    public void observeCowShedProccesBean(@Observes ProcessBean<Cowshed> event) {
        ProcessBeanObserver.cowShedProcessBeanCount++;
    }

    /**
     * https://issues.jboss.org/browse/WELD-586
     * 
     * @param event
     */
    public void observeChickenProcessProducerField(@Observes ProcessProducerField<Chicken, ChickenHutch> event) {
        ProcessBeanObserver.chickenProcessProducerField = event;
        chickenActionSeq.add(ProcessProducerField.class.getName());
    }

    public void observeChickenHutchProccesBean(@Observes ProcessBean<ChickenHutch> event) {
        ProcessBeanObserver.chickenHutchProcessBeanCount++;
    }

    public void observeCatBeanAttributes(@Observes ProcessBeanAttributes<Cat> event) {
        catActionSeq.add(ProcessBeanAttributes.class.getName());
    }

    public void observeCowBeanAttributes(@Observes ProcessBeanAttributes<Cow> event) {
        cowActionSeq.add(ProcessBeanAttributes.class.getName());
    }

    public void observeChickenBeanAttributes(@Observes ProcessBeanAttributes<Chicken> event) {
        chickenActionSeq.add(ProcessBeanAttributes.class.getName());
    }

    public static ProcessManagedBean<Cat> getCatProcessManagedBean() {
        return catProcessManagedBean;
    }

    public static ProcessProducerMethod<Cow, Cowshed> getCowProcessProducerMethod() {
        return cowProcessProducerMethod;
    }

    public static int getCatProcessBeanCount() {
        return catProcessBeanCount;
    }

    public static int getCowShedProcessBeanCount() {
        return cowShedProcessBeanCount;
    }

    public static int getChickenHutchProcessBeanCount() {
        return chickenHutchProcessBeanCount;
    }

    public static ActionSequence getCatActionSeq() {
        return catActionSeq;
    }

    public static ActionSequence getCowActionSeq() {
        return cowActionSeq;
    }

    public static ActionSequence getChickenActionSeq() {
        return chickenActionSeq;
    }

    /**
     * https://issues.jboss.org/browse/WELD-586
     * 
     * @return
     */
    public static ProcessProducerField<Chicken, ChickenHutch> getChickenProcessProducerField() {
        return chickenProcessProducerField;
    }

}
