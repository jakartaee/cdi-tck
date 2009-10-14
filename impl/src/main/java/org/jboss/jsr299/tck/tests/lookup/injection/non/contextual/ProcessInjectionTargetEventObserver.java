package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContextListener;
import javax.servlet.jsp.tagext.SimpleTag;

public class ProcessInjectionTargetEventObserver implements Extension
{

   private static ProcessInjectionTarget<Servlet> servletEvent = null; 
   private static ProcessInjectionTarget<ServletContextListener> listenerEvent = null; 
   private static ProcessInjectionTarget<Filter> filterEvent = null;
   private static ProcessInjectionTarget<ServletContextListener> tagLibraryListenerEvent = null; 
   private static ProcessInjectionTarget<SimpleTag> tagHandlerEvent = null; 
   
   public void observeServlet(@Observes ProcessInjectionTarget<Servlet> event) {
      servletEvent = event;
   }
   
   public void observeFilter(@Observes ProcessInjectionTarget<Filter> event) {
      filterEvent = event;
   }
   
   public void observeListener(@Observes ProcessInjectionTarget<ServletContextListener> event) {
      listenerEvent = event;
   }
   
   public void observeTagHandler(@Observes ProcessInjectionTarget<SimpleTag> event) {
      tagHandlerEvent = event;
   }
   
   public void observeTagLibraryListener(@Observes ProcessInjectionTarget<ServletContextListener> event) {
      tagLibraryListenerEvent = event;
   }

   public static ProcessInjectionTarget<Servlet> getServletEvent()
   {
      return servletEvent;
   }

   public static ProcessInjectionTarget<ServletContextListener> getListenerEvent()
   {
      return listenerEvent;
   }

   public static ProcessInjectionTarget<Filter> getFilterEvent()
   {
      return filterEvent;
   }

   public static ProcessInjectionTarget<ServletContextListener> getTagLibraryListenerEvent()
   {
      return tagLibraryListenerEvent;
   }

   public static ProcessInjectionTarget<SimpleTag> getTagHandlerEvent()
   {
      return tagHandlerEvent;
   }   
}
