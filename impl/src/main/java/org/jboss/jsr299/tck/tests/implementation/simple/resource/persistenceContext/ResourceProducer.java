package org.jboss.jsr299.tck.tests.implementation.simple.resource.persistenceContext;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

class ResourceProducer
{
   @Produces @Database @PersistenceUnit 
   private EntityManagerFactory persistenceUnit;
   
   @Produces @Database @PersistenceContext
   private EntityManager persistenceContext;
   
   /**
    * @return the persistenceContext
    */
   public EntityManager getPersistenceContext()
   {
      return persistenceContext;
   }
   
   /**
    * @return the persistenceUnit
    */
   public EntityManagerFactory getPersistenceUnit()
   {
      return persistenceUnit;
   }
}
