package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.GenericOperation.Pile_Interfaces;

//This is a pile which you can add elements to. You add elements by providing the element itself.
public interface AddablePile<T> extends Pile<T> {

  public void add(T elem);

}