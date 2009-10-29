package org.jboss.jsr299.tck.tests.deployment.lifecycle;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
class DataAccessAuthorizationDecorator implements DataAccess
{
   @Inject @Delegate
   DataAccess delegate;

   @Inject
   User       user;

   public void save()
   {
      authorize("save");
      delegate.save();
   }

   public void delete()
   {
      authorize("delete");
      delegate.delete();
   }

   private void authorize(String action)
   {
      Object id = delegate.getId();
      Class<?> type = delegate.getDataType();
      if (user.hasPermission(action, type, id))
      {
         System.out.println("Authorized for " + action);
      }
      else
      {
         System.out.println("Not authorized for " + action);
         throw new NotAuthorizedException(action);
      }
   }

   public Class<?> getDataType()
   {
      return delegate.getDataType();
   }

   public Object getId()
   {
      return delegate.getId();
   }

   public Object load(Object id)
   {
      return delegate.load(id);
   }

}
