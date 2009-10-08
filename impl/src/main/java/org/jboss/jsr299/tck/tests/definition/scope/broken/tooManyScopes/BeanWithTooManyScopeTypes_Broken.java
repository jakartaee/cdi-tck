package org.jboss.jsr299.tck.tests.definition.scope.broken.tooManyScopes;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;

@RequestScoped
@ConversationScoped
class BeanWithTooManyScopeTypes_Broken
{
}
