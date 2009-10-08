package org.jboss.jsr299.tck.tests.context.passivating.broken.managedBeanWithNonSerializableInterceptorClass;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.interceptor.Interceptors;

@SuppressWarnings("serial")
@SessionScoped
@Interceptors(Interceptor_Broken.class)
class Kokkola_Broken implements Serializable
{

}
