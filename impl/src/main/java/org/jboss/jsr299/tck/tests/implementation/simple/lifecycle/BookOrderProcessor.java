package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

class BookOrderProcessor extends OrderProcessor
{
   
   @Override
   public void postConstruct() {}
   
   @Override
   public void preDestroy() {}
   
}
