package org.firstinspires.ftc.teamcode.LithiumCore.Utils.PileUtils.Pile_Interfaces;

import org.firstinspires.ftc.teamcode.LithiumCore.Utils.Consumer;

//A pile is something that contains a bunch of things (elements). These elements are referred to and accessed by their value, sort of like a hashset.
public interface Pile<T> {

  //run a consumer over all elements in the pile
  public void forAll(final Consumer<T> consumer);

}