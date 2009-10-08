package org.jboss.jsr299.tck.tests.deployment.lifecycle;

interface DataAccess {
   public Object load(Object id);
   public Object getId();
   public void save();
   public void delete();
   public Class<?> getDataType();
}
