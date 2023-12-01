package org.jboss.cdi.tck.tests.build.compatible.extensions.customNormalScope;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.MetaAnnotations;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;

public class CustomNormalScopeExtension implements BuildCompatibleExtension {
    @Discovery
    public void discovery(MetaAnnotations meta, ScannedClasses scan) {
        meta.addContext(CommandScoped.class, CommandContext.class);
    }

    @Synthesis
    public void synthesis(SyntheticComponents syn) {
        syn.addBean(CommandContextController.class)
                .type(CommandContextController.class)
                .scope(Dependent.class)
                .createWith(CommandContextControllerCreator.class);

        syn.addBean(CommandExecution.class)
                .type(CommandExecution.class)
                .scope(CommandScoped.class)
                .createWith(CommandExecutionCreator.class);
    }
}
