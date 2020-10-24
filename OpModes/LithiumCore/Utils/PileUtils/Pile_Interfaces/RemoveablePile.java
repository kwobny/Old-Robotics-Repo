package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils.Pile_Interfaces;

//This is a pile which you can remove elements from. You remove elements by providing the element itself.
public interface RemoveablePile<T> extends Pile<T> {

  public void remove(T elem);

}