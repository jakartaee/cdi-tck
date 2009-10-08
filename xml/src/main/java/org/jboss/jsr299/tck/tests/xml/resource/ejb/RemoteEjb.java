package org.jboss.jsr299.tck.tests.xml.resource.ejb;

import javax.ejb.Stateless;

@Stateless(name="MyRemoteEjb", mappedName="ejb/MyRemoteEjb")
public class RemoteEjb implements RemoteEjbInterface
{

   public String hello()
   {
      return "hi!";
   }

}
