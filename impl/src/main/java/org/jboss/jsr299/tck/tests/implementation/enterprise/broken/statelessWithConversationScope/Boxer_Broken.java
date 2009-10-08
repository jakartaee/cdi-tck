package org.jboss.jsr299.tck.tests.implementation.enterprise.broken.statelessWithConversationScope;

import javax.ejb.Stateless;
import javax.enterprise.context.ConversationScoped;

@Stateless
@ConversationScoped
class Boxer_Broken implements BoxerLocal_Broken
{

}
