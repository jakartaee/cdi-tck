package org.jboss.cdi.tck.tests.build.compatible.extensions.customNormalScope;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

/**
 * Executes a {@link Command Command} in the command scope. That is, the command context
 * is activated before calling {@code Command.execute()} and deactivated when
 * {@code Command.execute()} returns (or throws).
 */
@Dependent
public class CommandExecutor {
    private final CommandContextController control;

    @Inject
    CommandExecutor(CommandContextController control) {
        this.control = control;
    }

    public void execute(Command command) {
        try {
            control.activate();
            command.execute();
        } finally {
            control.deactivate();
        }
    }
}
