package org.jboss.jsr299.tck.tests.context.application.ejb;

import javax.ejb.Stateless;
import javax.xml.ws.WebServiceRef;

@Stateless
public class CommonKingfisher implements Bird
{

   @WebServiceRef
   private FeederService birdFeeder;

   public void eat()
   {
      birdFeeder.refillFood();
   }

}
