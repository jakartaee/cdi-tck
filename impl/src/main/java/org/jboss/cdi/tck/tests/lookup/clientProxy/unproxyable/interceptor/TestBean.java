package org.jboss.cdi.tck.tests.lookup.clientProxy.unproxyable.interceptor;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class TestBean {

  @Inject
  Tuna tuna;
	
}
