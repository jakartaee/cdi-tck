package org.jboss.cdi.tck.tests.build.compatible.extensions.customNormalScope;

/**
 * A <em>command</em>. Should not be a bean. The {@link CommandExecutor CommandExecutor}
 * should be used to execute a command with automatic activation/deactivation of the command context.
 */
@FunctionalInterface
public interface Command {
    void execute();
}
