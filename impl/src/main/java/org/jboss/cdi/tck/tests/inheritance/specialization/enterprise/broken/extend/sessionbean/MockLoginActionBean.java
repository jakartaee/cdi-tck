package org.jboss.cdi.tck.tests.inheritance.specialization.enterprise.broken.extend.sessionbean;

import javax.ejb.Stateless;
import javax.enterprise.inject.Specializes;

@Stateless
@Mock
@Specializes
public class MockLoginActionBean extends LoginActionBean {
}
