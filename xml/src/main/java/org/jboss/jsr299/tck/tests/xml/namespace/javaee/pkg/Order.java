package org.jboss.jsr299.tck.tests.xml.namespace.javaee.pkg;

import java.util.Date;

import javax.ejb.TimerHandle;
import javax.enterprise.context.RequestScoped;
import javax.event.Event;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.xml.ws.Binding;

@RequestScoped
@TestBindingType
class Order
{
   private final Integer integer; 
   
   private final Date date;
   
   public Order()
   {
      this(0, new Date(), null, null, null, null, null, null);
   }
   
   public Order(Integer integer, Date date, DataSource source, InvocationContext invocation, Event<String> e, TimerHandle schedule, 
         EntityManager entityManager, Binding binding)
   {
      this.integer = integer;
      this.date = date;
   }

   public Integer getInteger()
   {
      return integer;
   }

   public Date getDate()
   {
      return date;
   }
}
