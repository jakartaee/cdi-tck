package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.failsDuringValidation;

import javax.inject.Inject;


class BeanWithInjection
{
   @Inject protected NotABean pretendBean;
}
