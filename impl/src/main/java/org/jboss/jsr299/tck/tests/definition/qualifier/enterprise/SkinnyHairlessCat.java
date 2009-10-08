package org.jboss.jsr299.tck.tests.definition.qualifier.enterprise;

import javax.ejb.Stateless;

@Stateless
@Skinny
class SkinnyHairlessCat extends HairlessCat implements SkinnyHairlessCatLocal
{

}
