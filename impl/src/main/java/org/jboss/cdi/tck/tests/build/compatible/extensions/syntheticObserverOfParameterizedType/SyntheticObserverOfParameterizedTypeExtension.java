package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticObserverOfParameterizedType;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.inject.build.compatible.spi.Types;

import java.util.List;

public class SyntheticObserverOfParameterizedTypeExtension implements BuildCompatibleExtension {
    @Synthesis
    public void synthesize(SyntheticComponents syn, Types types) {
        syn.<List<MyData>>addObserver(types.parameterized(List.class, MyData.class))
                .observeWith(MyObserver.class);
    }
}
