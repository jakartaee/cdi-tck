package org.jboss.cdi.tck.tests.definition.bean.types;

public interface GroupingOfCertainType<T> {

    T getType(); // just to avoid having <T> unused
}
