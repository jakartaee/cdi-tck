package org.jboss.jsr299.tck.tests.xml.annotationtypes;

@AnotherTestInterceptorBindingType
class BeanWithAnotherInterceptorBinding
{
   public Object getInterceptor()
   {
      return null;
   }
}
