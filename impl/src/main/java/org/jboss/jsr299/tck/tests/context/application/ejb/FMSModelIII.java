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
package org.jboss.jsr299.tck.tests.context.application.ejb;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

@Stateless
public class FMSModelIII implements FMS
{
   private static final long serialVersionUID = 1L;

   @Resource
   private TimerService timerService;
   
   @Inject
   private BeanManager beanManager;
   
   @Inject
   private Instance<SimpleApplicationBean> simpleApplicationBeanInstance;

   private static volatile boolean applicationScopeActive = false;
   private static volatile double beanId = 0.0d;
   private static volatile boolean sameBean = false;
   
   private static volatile int climbState;
   private static volatile int descendState;

   public void climb() throws Exception
   {
      climbState = 0;
      timerService.createTimer(200, "Climb command timeout");
      long timestamp = System.currentTimeMillis();
      // Wait for the timer
      while (climbState == 0)
      {
         // but don't wait more than 10s so we don't just hang!
         if (System.currentTimeMillis() > timestamp + 10000)
         {
            return;
         }
         Thread.sleep(1);
      }
   }

   public void descend() throws Exception
   {
      descendState = 0;
      timerService.createTimer(100, "Descend command timeout");
      long timestamp = System.currentTimeMillis();
      // Wait for the timer
      while (descendState == 0)
      {
         // but don't wait more than 10s so we don't just hang!
         if (System.currentTimeMillis() > timestamp + 10000)
         {
            return;
         }
         Thread.sleep(1);
      }
   }

   @Timeout
   public void timeout(Timer timer)
   {
      if (beanManager.getContext(ApplicationScoped.class).isActive())
      {
         applicationScopeActive = true;
         if (beanId > 0.0)
         {
            if (beanId == simpleApplicationBeanInstance.get().getId())
            {
               sameBean = true;
            }
         }
         else
         {
            beanId = simpleApplicationBeanInstance.get().getId();
         }
      }
      climbState = 1;
      descendState = 1;
   }

   public boolean isApplicationScopeActive()
   {
      return applicationScopeActive;
   }

   public boolean isSameBean()
   {
      return sameBean;
   }

}
