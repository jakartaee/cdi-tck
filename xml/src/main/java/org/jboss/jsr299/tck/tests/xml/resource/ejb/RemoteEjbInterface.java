package org.jboss.jsr299.tck.tests.xml.resource.ejb;

import javax.ejb.Remote;

@Remote
public interface RemoteEjbInterface
{
   public String hello();
}
