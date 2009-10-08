package org.jboss.jsr299.tck.tests.implementation.enterprise.broken.singletonWithConversationScope;

import javax.ejb.Singleton;
import javax.enterprise.context.ConversationScoped;


@Singleton
@ConversationScoped
class Husky_Broken
{

}
