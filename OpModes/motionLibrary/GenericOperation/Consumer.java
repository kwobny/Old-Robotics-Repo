package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.GenericOperation;

//this interface only works for reference types
public interface Consumer<T> {
  public void run(T obj);
}