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
package org.jboss.jsr299.tck.tests.extensions.processBean.sessionBean;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessSessionBean;

public class ProcessBeanObserver implements Extension
{
   private static ProcessSessionBean<Elephant> elephantProcessSessionBean;
   private static int elephantProcessBeanCount;

   public void cleanup(@Observes BeforeShutdown shutdown)
   {
      elephantProcessSessionBean = null;
   }

   public void observeElephantSessionBean(@Observes ProcessSessionBean<Elephant> event)
   {
      ProcessBeanObserver.elephantProcessSessionBean = event;
   }
   
   public void observeElephantBean(@Observes ProcessBean<Elephant> event)
   {
      ProcessBeanObserver.elephantProcessBeanCount++;
   }

   public static ProcessSessionBean<Elephant> getElephantProcessSessionBean()
   {
      return elephantProcessSessionBean;
   }

   public static int getElephantProcessBeanCount()
   {
      return elephantProcessBeanCount;
   }

}
