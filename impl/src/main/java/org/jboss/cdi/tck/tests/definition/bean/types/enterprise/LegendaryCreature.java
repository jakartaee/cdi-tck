package org.jboss.cdi.tck.tests.definition.bean.types.enterprise;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;

@LocalBean
@Stateful
public class LegendaryCreature extends Creature implements LegendaryLocal {
}
