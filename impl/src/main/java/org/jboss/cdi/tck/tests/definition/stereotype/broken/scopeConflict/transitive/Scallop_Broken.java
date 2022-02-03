package org.jboss.cdi.tck.tests.definition.stereotype.broken.scopeConflict.transitive;

/**
 * This bean definition is invalid because it has two stereotypes (one directly, one transitively)
 * that have different default scopes and the bean does not explictly define a scope to resolve the conflict.
 */
@AnimalStereotype
public class Scallop_Broken {
}
