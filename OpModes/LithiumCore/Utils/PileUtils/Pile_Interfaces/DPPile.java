package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils.Pile_Interfaces;

//Dicernable Presence Pile
//Is a type of pile where you can see if an element is in it through the has method.
public interface DPPile<T> {
  //returns a boolean signifying whether or not the element is in the pile.
  //true means the element is present in the pile, false means the element is not present.
  public boolean has(T elem);
}