/*
 * Copyright 2023, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.build.compatible.extensions.customNormalScope;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.build.compatible.spi.BuildCompatibleExtension;
import jakarta.enterprise.inject.build.compatible.spi.Discovery;
import jakarta.enterprise.inject.build.compatible.spi.MetaAnnotations;
import jakarta.enterprise.inject.build.compatible.spi.ScannedClasses;
import jakarta.enterprise.inject.build.compatible.spi.Synthesis;
import jakarta.enterprise.inject.build.compatible.spi.SyntheticComponents;

public class CustomNormalScopeExtension implements BuildCompatibleExtension {
    @Discovery
    public void discovery(MetaAnnotations meta, ScannedClasses scan) {
        meta.addContext(CommandScoped.class, CommandContext.class);
    }

    @Synthesis
    public void synthesis(SyntheticComponents syn) {
        syn.addBean(CommandContextController.class)
                .type(CommandContextController.class)
                .scope(Dependent.class)
                .createWith(CommandContextControllerCreator.class);

        syn.addBean(CommandExecution.class)
                .type(CommandExecution.class)
                .scope(CommandScoped.class)
                .createWith(CommandExecutionCreator.class);
    }
}
