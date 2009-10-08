package org.jboss.jsr299.tck.tests.context.passivating;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.interceptor.Interceptors;

@SuppressWarnings("serial")
@SessionScoped
@Interceptors(KokkolaInterceptor.class)
public class Kokkola implements Serializable
{

}
