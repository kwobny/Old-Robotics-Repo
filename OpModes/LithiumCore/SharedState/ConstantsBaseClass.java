package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SharedState;

//This is the base implementation for a constants class which declares constants. The user is meant to define the constants.
public abstract class ConstantsBaseClass {

  public ConstantsBaseClass() {
    _initialize();
  }

  //function which is used by user to define the predeclared constants.
  protected abstract void _initialize();

}

/*

EXAMPLE:

public abstract class {NAME} extends ConstantsBaseClass {
  //A bunch of constant declaring here
  public double lol; //defined by inner implementation initialize
  public double userLol; //defined/provided by user in user initialize

  @Override
  public {NAME}() {
    //initialize the implementation constants here
    //lol is initialized using user provided userLol variable.
    lol = userLol + 2.0;
  }

  @Override
  protected abstract void _initialize();

}

TEMPLATE:

public abstract class {NAME} extends ConstantsBaseClass {

  public {NAME}() {
    //
  }

  @Override
  protected abstract void _initialize();

}

*/