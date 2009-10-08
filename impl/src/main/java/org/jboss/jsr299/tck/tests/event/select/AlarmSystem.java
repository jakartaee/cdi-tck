package org.jboss.jsr299.tck.tests.event.select;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;

@RequestScoped class AlarmSystem
{
   private int numSecurityEvents = 0;
   
   private int numSystemTests = 0;
   
   private int numBreakIns = 0;
   
   private int numViolentBreakIns = 0;
   
   public void securityEventOccurred(@Observes @Any SecurityEvent event)
   {
      numSecurityEvents++;
   }
   
   public void selfTest(@Observes @SystemTest SecurityEvent event)
   {
      numSystemTests++;
   }
   
   public void breakInOccurred(@Observes @Any BreakInEvent event)
   {
      numBreakIns++;
   }
   
   public void securityBreeched(@Observes @Violent BreakInEvent event)
   {
      numViolentBreakIns++;
   }
   
   public int getNumSystemTests()
   {
      return numSystemTests;
   }
   
   public int getNumSecurityEvents()
   {
      return numSecurityEvents;
   }
   
   public int getNumBreakIns()
   {
      return numBreakIns;
   }
   
   public int getNumViolentBreakIns()
   {
      return numViolentBreakIns;
   }
   
   public void reset()
   {
      numBreakIns = 0;
      numViolentBreakIns = 0;
      numSecurityEvents = 0;
      numSystemTests = 0;
   }
}
