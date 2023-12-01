package org.jboss.cdi.tck.tests.build.compatible.extensions.customNormalScope;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.build.compatible.spi.Parameters;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticBeanCreator;
import jakarta.enterprise.inject.spi.BeanContainer;

public class CommandExecutionCreator implements SyntheticBeanCreator<CommandExecution> {
    @Override
    public CommandExecution create(Instance<Object> lookup, Parameters params) {
        CommandContext ctx = (CommandContext) lookup.select(BeanContainer.class).get().getContext(CommandScoped.class);
        return ctx.getCurrentCommandExecution();
    }
}
