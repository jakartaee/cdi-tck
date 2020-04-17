package org.jboss.cdi.tck.tests.lookup.clientProxy.unproxyable.interceptor;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Fish
@Interceptor
public class FishInterceptor {

	public FishInterceptor() {
	}

	@AroundInvoke
	public Object aroundInvoke(InvocationContext ic) throws Exception {

		return ic.proceed();
	}

}
