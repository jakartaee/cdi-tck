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

   private static boolean applicationScopeActive = false;
   private static double beanId = 0.0d;
   private static boolean sameBean = false;

   public void climb()
   {
      timerService.createTimer(200, "Climb command timeout");
   }

   public void descend()
   {
      timerService.createTimer(100, "Descend command timeout");
      beanId = 0.0d;
      sameBean = false;
   }

   public void turnLeft()
   {
   }

   public void turnRight()
   {
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
   }

   public boolean isApplicationScopeActive()
   {
      return applicationScopeActive;
   }

   public void setApplicationScopeActive(boolean applicationScopeActive)
   {
      FMSModelIII.applicationScopeActive = applicationScopeActive;
   }

   public boolean isSameBean()
   {
      return sameBean;
   }

}
