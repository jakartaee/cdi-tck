package org.jboss.jsr299.tck.tests.extensions.container.event;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

@Stateless(name = "sheep")
@Tame
public class Sheep implements SheepLocal
{
   @Interceptors(Sheep.class)
   public void baa() {
   }
}
