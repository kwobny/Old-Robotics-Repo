package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Wait_Package;

//This class is a wait condition that needs to be started/initialized. Sort of like pressing go on a stopwatch.
//You can start it multiple times.
public interface StartedCondition extends WaitCondition {

  //This method should start the condition and return the current object (this). It does not have to, but that is what would usually happen.
  public StartedCondition start();
}