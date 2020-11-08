package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SharedState;

public abstract class RobotConfig extends ConstantsBaseClass {

  //SYSTEM CONFIG

  //Turn on Once Per Loop Processing: a setting which if turned on, makes it so that there is only 1 robot state that is being used throughout the loop. Helps to avoid unnecessary extra computation, and synchronises all parts of the wait system.
  public boolean turnOnOPLP = true;

  public double lowFreqMaintInterval = 0.2; //period in which low frequency periodic functions like motor cali run (in seconds)
  public double highFreqMaintInterval = 0.01; //period in which very high frequency accurate functions run (in seconds)

  //ROBOT CONFIG

  public double totalAngleMeasure = 360; //the number of angle units in a full circle
  public double defaultSectorSize = 90; //The default size of the sector for wait for displacement.
  public double motor_down_scale = 0.2; //specifies the universal buffer reference speed factor

  //IMPLEMENTATION
  public RobotConfig() {
    //
  }

  @Override
  protected abstract void _initialize();

}