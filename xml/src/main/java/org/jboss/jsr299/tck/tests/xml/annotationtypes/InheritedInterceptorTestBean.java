package org.jboss.jsr299.tck.tests.xml.annotationtypes;

@InterceptorType2
class InheritedInterceptorTestBean
{
   public InterceptorRecorder getInterceptors()
   {
      return new InterceptorRecorder();
   }
}
