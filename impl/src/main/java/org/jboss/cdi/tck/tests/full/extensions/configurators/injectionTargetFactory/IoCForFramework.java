/*
 * Copyright 2016, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.full.extensions.configurators.injectionTargetFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.literal.InjectLiteral;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.InjectionTarget;
import jakarta.enterprise.inject.spi.InjectionTargetFactory;
import jakarta.inject.Inject;

/**
 *
 * @author <a href="mailto:manovotn@redhat.com">Matej Novotny</a>
 */
@ApplicationScoped
public class IoCForFramework {

    private CreationalContext<NotOurClass> nocc;
    private InjectionTarget<NotOurClass> it;
    private boolean exceptionThrown = false;

    public IoCForFramework(){}

    @Inject
    public IoCForFramework(BeanManager bm) {
        nocc = bm.createCreationalContext(null);
        InjectionTargetFactory<NotOurClass> itf = bm.getInjectionTargetFactory(bm.createAnnotatedType(NotOurClass.class));
        itf.configure().filterMethods(m -> m.getJavaMember().getName().equals("setService")).findFirst().get().add(InjectLiteral.INSTANCE);

        it = itf.createInjectionTarget(null);

        // invoking configure() after create method should throw exception
        try {
            itf.configure().filterMethods(m -> m.getJavaMember().getName().equals("setService")).findFirst().get().add(InjectLiteral.INSTANCE);
        } catch (IllegalStateException e) {
            //expected behaviour
            exceptionThrown = true;
        }

    }

    @Produces
    public NotOurClass injectServiceInNotOurClass() {
        NotOurClass noc = new NotOurClass();
        it.inject(noc, nocc);
        return noc;
    }

    public boolean wasExceptionThrown() {
        return exceptionThrown;
    }

}
