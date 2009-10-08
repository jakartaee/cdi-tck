package org.jboss.jsr299.tck.tests.extensions.annotated.broken.processInjectionTargetThrowsException;

import javax.inject.Inject;

class Dog
{
   @Inject
   private DogBone dogBone;
}
