package org.jboss.jsr299.tck.tests.definition.stereotype.broken.scopeConflict;

/**
 * This bean definition is invalid because it declares two stereotypes
 * that have different default scopes and the bean does not explictly
 * define a scope to resolve the conflict.
 */
@AnimalStereotype
@FishStereotype
class Scallop_Broken
{
   
}
