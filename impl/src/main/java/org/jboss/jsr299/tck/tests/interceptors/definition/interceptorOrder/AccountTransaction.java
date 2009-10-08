package org.jboss.jsr299.tck.tests.interceptors.definition.interceptorOrder;

import javax.interceptor.Interceptors;

@Transactional
@Interceptors(AnotherInterceptor.class)
class AccountTransaction
{
   public void execute() {}
}
