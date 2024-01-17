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
package org.jboss.cdi.tck.tests.full.decorators.context.dependent;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.TestGroups.CDI_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DEPENDENT_OBJECTS;

@SpecVersion(spec = "cdi", version = "2.0")
public class DependentContextTest extends AbstractTest {

   @Deployment
   public static WebArchive createTestArchive() {
      return new WebArchiveBuilder().withTestClassPackage(DependentContextTest.class).withBeansXml("beans.xml").build();
   }

   @Test(groups = CDI_FULL)
   @SpecAssertion(section = DEPENDENT_OBJECTS, id = "ab")
   public void testDependentScopedDecoratorsAreDependentObjectsOfBean() {
      Bean<Interior> roomBean = getBeans(Interior.class, new RoomBinding()).iterator().next();

      CreationalContext<Interior> roomCreationalContext = getCurrentManager().createCreationalContext(roomBean);
      Interior room = (Interior) getCurrentManager().getReference(roomBean, Interior.class, roomCreationalContext);

      InteriorDecorator.reset();

      room.foo();

      assert InteriorDecorator.getInstances().size() == 1;
      roomCreationalContext.release();
      assert InteriorDecorator.isDestroyed();
   }

}
