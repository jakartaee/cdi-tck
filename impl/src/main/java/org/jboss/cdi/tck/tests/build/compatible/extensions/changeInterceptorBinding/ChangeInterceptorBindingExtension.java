package org.jboss.cdi.tck.tests.build.compatible.extensions.changeInterceptorBinding;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;

public class ChangeInterceptorBindingExtension implements BuildCompatibleExtension {

    @Enhancement(types = MyService.class)
    public void enhancement(ClassConfig config) {
        config.methods()
                .stream()
                .filter(it -> "hello".equals(it.info().name()))
                .forEach(m -> m.addAnnotation(new MyBinding.Literal("foo")));
    }

}
