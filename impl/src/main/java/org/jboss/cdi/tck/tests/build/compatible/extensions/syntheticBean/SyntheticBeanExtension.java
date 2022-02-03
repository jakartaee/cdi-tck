package org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBean;

import jakarta.enterprise.inject.build.compatible.spi.AnnotationBuilder;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;
import jakarta.enterprise.inject.build.compatible.spi.Types;

public class SyntheticBeanExtension implements BuildCompatibleExtension {
    @Synthesis
    public void synthesize(SyntheticComponents syn, Types types) {
        syn.addBean(MyPojo.class)
                .type(MyPojo.class)
                .withParam("name", "World")
                .withParam("data", AnnotationBuilder.of(MyComplexValue.class)
                        .member("number", 42)
                        .member("enumeration", MyEnum.YES)
                        .member("type", MyEnum.class)
                        .member("nested", new MySimpleValue.Literal("yes", new byte[] { 4, 5, 6 }))
                        .build())
                .createWith(MyPojoCreator.class)
                .disposeWith(MyPojoDisposer.class);

        syn.addBean(MyPojo.class)
                .type(MyPojo.class)
                .qualifier(MyQualifier.class)
                .withParam("name", "Special")
                .withParam("data", AnnotationBuilder.of(MyComplexValue.class)
                        .member("number", 13)
                        .member("enumeration", MyEnum.class, "NO")
                        .member("type", types.ofClass("org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBean.MyEnum")
                                .declaration())
                        .member("nested", AnnotationBuilder.of(
                                        types.ofClass("org.jboss.cdi.tck.tests.build.compatible.extensions.syntheticBean.MySimpleValue")
                                                .declaration())
                                .value("no").build())
                        .build())
                .createWith(MyPojoCreator.class)
                .disposeWith(MyPojoDisposer.class);
    }
}
