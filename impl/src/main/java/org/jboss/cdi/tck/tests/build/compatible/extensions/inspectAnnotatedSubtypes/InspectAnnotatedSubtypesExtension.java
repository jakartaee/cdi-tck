package org.jboss.cdi.tck.tests.build.compatible.extensions.inspectAnnotatedSubtypes;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.Messages;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;
import jakarta.enterprise.inject.build.compatible.spi.Validation;

import java.util.HashSet;
import java.util.Set;

public class InspectAnnotatedSubtypesExtension implements BuildCompatibleExtension {
    private final Set<String> seenAnnotatedSubtypes = new HashSet<>();

    @Discovery
    public void discovery(ScannedClasses scan) {
        scan.add(MyServiceBaz.class.getName());
    }

    @Enhancement(types = MyService.class, withSubtypes = true, withAnnotations = MyAnnotation.class)
    public void enhancement(ClassConfig clazz) {
        seenAnnotatedSubtypes.add(clazz.info().simpleName());
    }

    @Validation
    public void validation(Messages msg) {
        if (seenAnnotatedSubtypes.size() != 2) {
            msg.error("Must have seen 2 subtypes of MyService with MyAnnotation");
        }

        if (!seenAnnotatedSubtypes.contains(MyServiceFoo.class.getSimpleName())) {
            msg.error("Must have seen MyServiceFoo");
        }

        if (seenAnnotatedSubtypes.contains(MyServiceBar.class.getSimpleName())) {
            msg.error("Must NOT have seen MyServiceBar");
        }

        if (!seenAnnotatedSubtypes.contains(MyServiceBaz.class.getSimpleName())) {
            msg.error("Must have seen MyServiceBaz");
        }
    }
}
