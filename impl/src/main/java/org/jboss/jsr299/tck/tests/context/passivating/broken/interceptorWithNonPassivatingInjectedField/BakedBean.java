package org.jboss.jsr299.tck.tests.context.passivating.broken.interceptorWithNonPassivatingInjectedField;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped @BakedBinding
class BakedBean implements Serializable
{
  void bake() {}
}
