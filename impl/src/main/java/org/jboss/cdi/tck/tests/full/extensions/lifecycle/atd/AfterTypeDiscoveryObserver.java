/*
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.literal.InjectLiteral;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.AfterTypeDiscovery;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.ProcessProducer;

import org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd.lib.Bar;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd.lib.Baz;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd.lib.Boss;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd.lib.Foo;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.atd.lib.Pro;

/**
 * @author Martin Kouba
 * @author Tomas Remes
 */
public class AfterTypeDiscoveryObserver implements Extension {

    private List<Class<?>> interceptors = null;
    private List<Class<?>> alternatives = null;
    private List<Class<?>> decorators = null;
    private boolean bossObserved = false;
    private boolean processProducerEventFiredForProducerMethod = false;
    private boolean processProducerEventFiredForProducerField = false;
    private DeltaAlternativeBean deltaAlternative = new DeltaAlternativeBean();
    private DeltaInterceptorBean deltaInterceptor = new DeltaInterceptorBean();
    private DeltaDecoratorBean deltaDecorator;

    public void observeAfterTypeDiscovery(@Observes AfterTypeDiscovery event, BeanManager beanManager) {

        event.getAlternatives().add(DeltaAlternative.class);
        event.getInterceptors().add(DeltaInterceptor.class);
        event.getDecorators().add(DeltaDecorator.class);

        interceptors = Collections.unmodifiableList(new ArrayList<Class<?>>(event.getInterceptors()));
        alternatives = Collections.unmodifiableList(new ArrayList<Class<?>>(event.getAlternatives()));
        decorators = Collections.unmodifiableList(new ArrayList<Class<?>>(event.getDecorators()));

        event.addAnnotatedType(beanManager.createAnnotatedType(Boss.class), AfterTypeDiscoveryObserver.class.getName());

        // Bravo interceptor removed
        for (Iterator<Class<?>> iterator = event.getInterceptors().iterator(); iterator.hasNext();) {
            if (BravoInterceptor.class.equals(iterator.next())) {
                iterator.remove();
            }
        }
        // The order of decorators reverted
        Collections.reverse(event.getDecorators());
        // Remove first alternative based on index - AlphaAlternative
        // There can be more alternatives in the deployment, calculate index first
        for (int i = 0; i < event.getAlternatives().size(); i++) {
            if (event.getAlternatives().get(i).equals(AlphaAlternative.class)) {
                event.getAlternatives().remove(i);
            }
        }

        // add Baz annotatedType via AnnotatedTypeConfigurator
        event.addAnnotatedType(Baz.class, AfterTypeDiscoveryObserver.class.getName() + ":" + Baz.class.getName())
                .add(Pro.ProLiteral.INSTANCE)
                .add(RequestScoped.Literal.INSTANCE)
                .filterFields(annotatedField -> annotatedField.getJavaMember().getType().equals(Instance.class)).findFirst()
                .get()
                .add(InjectLiteral.INSTANCE)
                .add(Pro.ProLiteral.INSTANCE);

        // Remove alternative, decorator and interceptor based on Class<?>
        event.getAlternatives().remove(EchoAlternative.class);
        event.getInterceptors().remove(EchoInterceptor.class);
        event.getDecorators().remove(EchoDecorator.class);

    }

    public void observeAfterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager beanManager) {

        AnnotatedType<DeltaDecorator> type = beanManager.createAnnotatedType(DeltaDecorator.class);
        deltaDecorator = new DeltaDecoratorBean(type.getFields().iterator().next(), beanManager);

        event.addBean(deltaAlternative);
        event.addBean(deltaInterceptor);
        event.addBean(deltaDecorator);

    }

    public void observeProcessAnnotatedType(@Observes ProcessAnnotatedType<DeltaDecorator> event) {
        event.veto();
    }

    public void observeBossAnnotatedType(@Observes ProcessAnnotatedType<Boss> event) {
        bossObserved = true;
    }

    public void observeProcessProducerForProducerField(@Observes ProcessProducer<Boss, Foo> event) {
        processProducerEventFiredForProducerField = true;
    }

    public void observeProcessProducerForProducerMethod(@Observes ProcessProducer<Boss, Bar> event) {
        processProducerEventFiredForProducerMethod = true;
    }

    public List<Class<?>> getInterceptors() {
        return interceptors;
    }

    public List<Class<?>> getAlternatives() {
        return alternatives;
    }

    public List<Class<?>> getDecorators() {
        return decorators;
    }

    public boolean isBossObserved() {
        return bossObserved;
    }

    public boolean isProcessProcuderFieldObserved() {
        return processProducerEventFiredForProducerField;
    }

    public boolean isProcessProcuderMethodObserved() {
        return processProducerEventFiredForProducerMethod;
    }

}
