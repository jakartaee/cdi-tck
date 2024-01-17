/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
