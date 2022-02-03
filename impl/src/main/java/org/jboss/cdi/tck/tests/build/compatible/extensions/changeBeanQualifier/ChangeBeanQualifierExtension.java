package org.jboss.cdi.tck.tests.build.compatible.extensions.changeBeanQualifier;

import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.ClassConfig;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.Enhancement;
import jakarta.enterprise.inject.build.compatible.spi.FieldConfig;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;

public class ChangeBeanQualifierExtension implements BuildCompatibleExtension {
    @Discovery
    public void discovery(ScannedClasses scan) {
        scan.add(MyServiceFoo.class.getName());
        scan.add(MyServiceBar.class.getName());
        scan.add(MyServiceBaz.class.getName());
    }

    @Enhancement(types = MyServiceFoo.class)
    public void foo(ClassConfig clazz) {
        clazz.removeAnnotation(ann -> ann.name().equals(MyQualifier.class.getName()));
    }

    @Enhancement(types = MyServiceBar.class)
    public void bar(ClassConfig clazz) {
        clazz.addAnnotation(MyQualifier.class);
    }

    @Enhancement(types = MyOtherService.class)
    public void service(FieldConfig field) {
        if ("myService".equals(field.info().name())) {
            field.addAnnotation(MyQualifier.class);
        }
    }
}
