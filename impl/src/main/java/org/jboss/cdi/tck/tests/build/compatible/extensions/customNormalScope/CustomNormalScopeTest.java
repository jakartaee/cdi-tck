package org.jboss.cdi.tck.tests.build.compatible.extensions.customNormalScope;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

// this test is basically a stripped down version of https://github.com/weld/command-context-example
public class CustomNormalScopeTest extends AbstractTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(CustomNormalScopeTest.class)
                .withBuildCompatibleExtension(CustomNormalScopeExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.DISCOVERY_PHASE, id = "a", note = "Register custom normal scope")
    public void commandContextController() {
        CommandContextController control = getContextualReference(CommandContextController.class);
        boolean activated = control.activate();
        assertTrue(activated);
        try {
            assertEquals(getContextualReference(IdService.class).get(), getContextualReference(IdService.class).get());
        } finally {
            control.deactivate();
        }
    }

    @Test
    @SpecAssertion(section = Sections.DISCOVERY_PHASE, id = "a", note = "Register custom normal scope")
    public void commandExecutor() {
        CommandExecutor executor = getContextualReference(CommandExecutor.class);
        executor.execute(() -> {
            CommandExecution execution = getContextualReference(CommandExecution.class);
            IdService idService = getContextualReference(IdService.class);

            getContextualReference(MyService.class).process();
            assertEquals(execution.getData().get("id"), idService.get());
            assertNotNull(execution.getStartedAt());
        });
    }
}
