package org.jboss.jsr299.tck.tests.context;

import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;

/**
 * @author pmuir
 *
 */
final class DummyContextual implements Contextual<String>
{
   private CreationalContext<String> creationalContextPassedToCreate;
   private CreationalContext<String> creationalContextPassedToDestroy;

   public String create(CreationalContext<String> creationalContext)
   {
      this.creationalContextPassedToCreate = creationalContext;
      return "123";
   }

   public void destroy(String instance, CreationalContext<String> creationalContext)
   {
      this.creationalContextPassedToDestroy = creationalContext;
   }
   
   /**
    * @return the creationalContextPassedToCreate
    */
   public CreationalContext<String> getCreationalContextPassedToCreate()
   {
      return creationalContextPassedToCreate;
   }
   
   /**
    * @return the creationalContextPassedToDestroy
    */
   public CreationalContext<String> getCreationalContextPassedToDestroy()
   {
      return creationalContextPassedToDestroy;
   }
}