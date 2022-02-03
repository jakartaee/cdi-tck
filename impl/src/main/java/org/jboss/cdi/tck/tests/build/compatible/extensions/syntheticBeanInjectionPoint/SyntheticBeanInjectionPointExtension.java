package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBeanInjectionPoint;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;

public class SyntheticBeanInjectionPointExtension implements BuildCompatibleExtension {
    @Synthesis
    public void synthesize(SyntheticComponents syn) {
        syn.addBean(MyDependentBean.class)
                .type(MyDependentBean.class)
                .scope(Dependent.class)
                .createWith(MyDependentBeanCreator.class)
                .disposeWith(MyDependentBeanDisposer.class);

        syn.addBean(MyApplicationScopedBean.class)
                .type(MyApplicationScopedBean.class)
                .scope(ApplicationScoped.class)
                .createWith(MyApplicationScopedBeanCreator.class);
    }
}
