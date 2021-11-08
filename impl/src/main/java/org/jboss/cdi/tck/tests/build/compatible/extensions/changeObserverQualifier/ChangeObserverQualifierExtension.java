package org.jboss.cdi.tck.tests.build.compatible.extensions.changeObserverQualifier;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.MethodConfig;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;

public class ChangeObserverQualifierExtension implements BuildCompatibleExtension {
    @Discovery
    public void discovery(ScannedClasses scan) {
        scan.add(MyConsumer.class.getName());
        scan.add(MyProducer.class.getName());
    }

    @Enhancement(types = MyConsumer.class)
    public void consumer(MethodConfig method) {
        switch (method.info().name()) {
            case "consume":
                method.parameters().get(0).addAnnotation(MyQualifier.class);
                break;
            case "noConsume":
                method.parameters().get(0).removeAllAnnotations();
                break;
        }
    }
}
