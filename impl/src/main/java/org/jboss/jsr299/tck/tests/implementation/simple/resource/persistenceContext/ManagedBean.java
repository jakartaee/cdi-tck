package org.jboss.jsr299.tck.tests.implementation.simple.resource.persistenceContext;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

class ManagedBean implements Serializable
{
   @Inject @Database 
   private EntityManager persistenceContext;

   @Inject @Database
   private EntityManagerFactory persistenceUnit;

   public EntityManager getPersistenceContext()
   {
      return persistenceContext;
   }

   public EntityManagerFactory getPersistenceUnit()
   {
      return persistenceUnit;
   }

}
