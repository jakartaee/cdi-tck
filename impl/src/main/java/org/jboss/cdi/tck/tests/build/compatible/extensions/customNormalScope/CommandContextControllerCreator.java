package org.jboss.cdi.tck.tests.build.compatible.extensions.customNormalScope;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;
import jakarta.enterprise.inject.spi.BeanContainer;

public class CommandContextControllerCreator implements SyntheticBeanCreator<CommandContextController> {
    @Override
    public CommandContextController create(Instance<Object> lookup, Parameters params) {
        BeanContainer beanContainer = lookup.select(BeanContainer.class).get();
        CommandContext ctx = (CommandContext) beanContainer.getContexts(CommandScoped.class).iterator().next();
        return new CommandContextController(ctx, beanContainer);
    }
}
