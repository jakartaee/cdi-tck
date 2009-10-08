package org.jboss.jsr299.tck.tests.lookup.injection;

import javax.inject.Inject;

class FoxRun
{
   
   @Inject
   public Fox fox;
   
   @Inject
   public Fox anotherFox;
   
}
