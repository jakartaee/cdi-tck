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
package org.jboss.jsr299.tck.tests.context;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Context;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.testng.annotations.Test;

/**
 * 
 * @author Nicklas Karlsson
 * @author Pete Muir
 * @author David Allen
 */
@Artifact
@SpecVersion(spec="cdi", version="20091101")
public class DestroyForSameCreationalContext2Test extends AbstractJSR299Test
{
   
   @Test
   @SpecAssertion(section = "6.2", id = "r")
   public void testDestroyForSameCreationalContextOnly()
   {
      // Check that the mock cc is called (via cc.release()) when we request a context destroyed
      // Note that this is an indirect effect
      Context sessionContext = getCurrentManager().getContext(SessionScoped.class);
      Context requestContext = getCurrentManager().getContext(RequestScoped.class);
      Context appContext = getCurrentManager().getContext(ApplicationScoped.class);
      
      // We also test this directly using a custom contextual, and ensuring that the same contextual is passed to both methods
      DummyContextual contextual = new DummyContextual();
      
      sessionContext.get(contextual, getCurrentManager().createCreationalContext(contextual));
      destroyContext(sessionContext);
      assert contextual.getCreationalContextPassedToCreate() == contextual.getCreationalContextPassedToDestroy();
      
      // Do it for other contexts
      contextual = new DummyContextual();
      appContext.get(contextual, getCurrentManager().createCreationalContext(contextual));
      destroyContext(appContext);
      assert contextual.getCreationalContextPassedToCreate() == contextual.getCreationalContextPassedToDestroy();
      
      contextual = new DummyContextual();
      requestContext.get(contextual, getCurrentManager().createCreationalContext(contextual));
      destroyContext(requestContext);
      assert contextual.getCreationalContextPassedToCreate() == contextual.getCreationalContextPassedToDestroy();
      
   }
   
}
