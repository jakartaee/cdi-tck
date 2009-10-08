package org.jboss.jsr299.tck.tests.event.observer.transactional;

import javax.ejb.Local;

@Local
public interface Trainer
{
   public void train(DisobedientDog dog);
   
   public void trainNewTricks(ShowDog dog);
   
   public void trainSightSeeing(LargeDog dog);
   
   public void trainCompanion(SmallDog dog);
}
