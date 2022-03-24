package org.jboss.cdi.tck.tests.build.compatible.extensions.invalid;

import jakarta.enterprise.inject.spi.DefinitionException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;

@SpecVersion(spec = "cdi", version = "4.0")
public class RegistrationOnlyMessagesTest extends AbstractInvalidExtensionParamTest {

    @ShouldThrowException(DefinitionException.class)
    @Deployment
    public static WebArchive createTestArchive() {
        return prepareArchiveBuilder().withBuildCompatibleExtension(RegistrationOnlyMessagesExtension.class)
                        .withClass(RegistrationOnlyMessagesExtension.class)
                        .build();
    }
}
