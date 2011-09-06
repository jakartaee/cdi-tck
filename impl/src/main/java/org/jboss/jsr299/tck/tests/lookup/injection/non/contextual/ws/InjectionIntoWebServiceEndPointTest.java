/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual.ws;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * FIXME invalid WSDL location  
 */
@SpecVersion(spec="cdi", version="20091101")
public class InjectionIntoWebServiceEndPointTest extends AbstractJSR299Test
{
    
   @Deployment(testable=false)
   public static WebArchive createTestArchive() 
	{
       return new WebArchiveBuilder()
           .withTestClass(InjectionIntoWebServiceEndPointTest.class)
           .withClasses(Sheep.class, SheepWSEndPoint.class)
           .withWebXml("web.xml")
           .build();
   }
    
   @Test(groups = "javaee-full-only")
   @SpecAssertions({
      @SpecAssertion(section = "5.5", id = "ee"),
      @SpecAssertion(section = "5.5.2", id = "aq"),
      @SpecAssertion(section = "5.5.2", id = "ar")
   })
   // JBAS-7046
   public void testInjectionIntoWebServiceEndpoint() throws Exception
   {
      SheepWSEndPointService service = new SheepWSEndPointService();
      SheepWS ws = service.getSheepWSPort();
      assert ws.isSheepInjected();
   }
}
