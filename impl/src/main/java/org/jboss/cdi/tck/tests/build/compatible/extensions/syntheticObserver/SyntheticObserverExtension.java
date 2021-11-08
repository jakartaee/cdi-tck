package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticObserver;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;

public class SyntheticObserverExtension implements BuildCompatibleExtension {
    @Synthesis
    public void synthesize(SyntheticComponents syn) {
        syn.addObserver(MyEvent.class)
                .priority(10)
                .observeWith(MyObserver.class);

        syn.addObserver(MyEvent.class)
                .qualifier(MyQualifier.class)
                .priority(20)
                .withParam("name", "@MyQualifier")
                .observeWith(MyObserver.class);
    }
}
