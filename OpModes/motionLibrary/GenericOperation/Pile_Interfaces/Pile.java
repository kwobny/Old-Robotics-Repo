package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.GenericOperation.Pile_Interfaces;

//A pile is something that contains a bunch of things (elements). These elements are referred to and accessed by their value, sort of like a hashset.
public interface Pile<T> {

  //run a consumer over all elements in the pile
  public void forAll(final Consumer<T> consumer);

}