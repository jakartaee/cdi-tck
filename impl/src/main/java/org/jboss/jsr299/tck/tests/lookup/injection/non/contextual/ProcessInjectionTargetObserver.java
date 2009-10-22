package org.jboss.jsr299.tck.tests.lookup.injection.non.contextual;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ProcessInjectionTargetObserver implements Extension
{

   private static ProcessInjectionTarget<TestServlet> servletEvent = null; 
   private static ProcessInjectionTarget<TestListener> listenerEvent = null; 
   private static ProcessInjectionTarget<TestFilter> filterEvent = null;
   private static ProcessInjectionTarget<TagLibraryListener> tagLibraryListenerEvent = null; 
   private static ProcessInjectionTarget<TestTagHandler> tagHandlerEvent = null;
   private static ProcessInjectionTarget<Farm> jsfManagedBeanEvent = null;
   
   private static boolean servletSuperTypeObserved = false;
   private static boolean servletSubTypeObserved = false;
   private static boolean listenerSuperTypeObserved = false;
   private static boolean tagHandlerSuperTypeObserved = false;
   private static boolean tagHandlerSubTypeObserved = false;
   private static boolean stringObserved = false;
   
   public void observeServlet(@Observes ProcessInjectionTarget<TestServlet> event) {
      servletEvent = event;
   }
   
   public void observeFilter(@Observes ProcessInjectionTarget<TestFilter> event) {
      filterEvent = event;
   }
   
   public void observeListener(@Observes ProcessInjectionTarget<TestListener> event) {
      listenerEvent = event;
   }
   
   public void observeTagHandler(@Observes ProcessInjectionTarget<TestTagHandler> event) {
      tagHandlerEvent = event;
   }
   
   public void observeTagLibraryListener(@Observes ProcessInjectionTarget<TagLibraryListener> event) {
      tagLibraryListenerEvent = event;
   }
   
   public void observeJsfManagedBean(@Observes ProcessInjectionTarget<Farm> event) {
      jsfManagedBeanEvent = event;
   }
   
   public void observeServletSuperType(@Observes ProcessInjectionTarget<? super HttpServlet> event) {
      servletSuperTypeObserved = true;
   }
   
   public void observeServletSubType(@Observes ProcessInjectionTarget<? extends HttpServlet> event) {
      servletSubTypeObserved = true;
   }
   
   public void observeListenerSuperType(@Observes ProcessInjectionTarget<? super ServletContextListener> event) {
      listenerSuperTypeObserved = true;
   }
   
   public void tagHandlerSuperType(@Observes ProcessInjectionTarget<? super SimpleTagSupport> event) {
      tagHandlerSuperTypeObserved = true;
   }
   
   public void tagHandlerSubType(@Observes ProcessInjectionTarget<? extends SimpleTag> event) {
      tagHandlerSubTypeObserved = true;
   }
   
   public void stringObserver(@Observes ProcessInjectionTarget<String> event) {
      stringObserved = true;
   }

   public static ProcessInjectionTarget<TestServlet> getServletEvent()
   {
      return servletEvent;
   }

   public static ProcessInjectionTarget<TestListener> getListenerEvent()
   {
      return listenerEvent;
   }

   public static ProcessInjectionTarget<TestFilter> getFilterEvent()
   {
      return filterEvent;
   }

   public static ProcessInjectionTarget<TagLibraryListener> getTagLibraryListenerEvent()
   {
      return tagLibraryListenerEvent;
   }

   public static ProcessInjectionTarget<TestTagHandler> getTagHandlerEvent()
   {
      return tagHandlerEvent;
   }
   
   public static ProcessInjectionTarget<Farm> getJsfManagedBeanEvent()
   {
      return jsfManagedBeanEvent;
   }

   public static boolean isServletSuperTypeObserved()
   {
      return servletSuperTypeObserved;
   }

   public static boolean isServletSubTypeObserved()
   {
      return servletSubTypeObserved;
   }

   public static boolean isListenerSuperTypeObserved()
   {
      return listenerSuperTypeObserved;
   }

   public static boolean isTagHandlerSuperTypeObserved()
   {
      return tagHandlerSuperTypeObserved;
   }

   public static boolean isTagHandlerSubTypeObserved()
   {
      return tagHandlerSubTypeObserved;
   }

   public static boolean isStringObserved()
   {
      return stringObserved;
   }   
}
