package org.jboss.cdi.tck.tests.build.compatible.extensions.invalid;

import jakarta.enterprise.inject.spi.DefinitionException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "4.0")
public class EnhancementOnlyMessagesTest extends AbstractInvalidExtensionParamTest {

    @ShouldThrowException(DefinitionException.class)
    @Deployment
    public static WebArchive createTestArchive() {
        return prepareArchiveBuilder().withBuildCompatibleExtension(EnhancementOnlyMessagesExtension.class)
                .withClass(EnhancementOnlyMessagesExtension.class)
                .build();
    }

    @Test
    @SpecAssertion(section = Sections.ENHANCEMENT_PHASE, id= "aa", note = "Fail due to no required parameter ext method")
    public void shouldFail() {
    }
}
