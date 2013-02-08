package org.jboss.cdi.tck.tests.lookup.clientProxy.unproxyable.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

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
