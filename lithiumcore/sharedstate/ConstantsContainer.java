package org.firstinspires.ftc.teamcode.lithiumcore.sharedstate;

public class ConstantsContainer {

  public SystemConfig config;
  public RobotParameters robotParameters;

  //This method is optional
  public void defaultInitialize() {
    if (config == null) {
      config = new SystemConfig();
    }
    if (robotParameters == null) {
      robotParameters = new RobotParameters.Default();
    }
  }

}

/*

Two ways to define constants:
1. Defining outside of class

{CONSTANTS_CLASS} consts = new {CONSTANTS_CLASS}();

consts.blahProp = blahVal;
consts.blahProp2 = blahVal2;
consts.moreInitialize();

2. Define in a new class. Allows for easy reusability.

public class {CONSTANTS_CLASS} extends {CONSTANTS_BASE} {

  public {CONSTANTS_CLASS}() {
    //define stuff
  }

}

*/