package org.jboss.jsr299.tck.tests.lookup.injectionpoint.broken.not.bean;

import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;

@SuppressWarnings("serial")
public class TestServlet_Broken extends HttpServlet
{
   @SuppressWarnings("unused")
   @Inject
   private InjectionPoint ip;

}
