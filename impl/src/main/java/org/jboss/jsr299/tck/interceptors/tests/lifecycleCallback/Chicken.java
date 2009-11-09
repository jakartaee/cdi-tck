package org.jboss.jsr299.tck.interceptors.tests.lifecycleCallback;

import javax.interceptor.Interceptors;

@Interceptors( { PublicLifecycleInterceptor.class, ProtectedLifecycleInterceptor.class, PackagePrivateLifecycleInterceptor.class, PrivateLifecycleInterceptor.class })
class Chicken
{

}
