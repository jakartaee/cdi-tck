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
package org.jboss.cdi.tck.tests.build.compatible.extensions.customStereotype;

import jakarta.enterprise.context.Dependent;

/**
 * Deployment for this test contains only one archive with no beans.xml and an extension.
 * For some EE containers this might trigger an optimization where they do not bootstrap CDI container.
 * Presence of this CDI-annotated class can help prevent it.
 *
 * Due to the archive not being a bean archive, this class should not be discovered as a bean despite having bean
 * defining annotation.
 */
@Dependent
public class NotDiscoveredBean {
}
