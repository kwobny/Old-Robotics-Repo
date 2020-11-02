package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SCS_Package;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.MathFunctions.*;
import java.util.ArrayDeque;

//This class chains a bunch of unidirectional functions together into 1 unidirectional function. Is basically a compound unidirectional function.
//Coolest feature: you can nest multiple seq op funcs with low performance difference. This whle thing was designed on top of that.
//this class is an immutable class, except when extended.

//when extending this class, the one and only requirement is to set the funcs variable to something via the setFuncs function.

//The funcs variable can be set to null, be a length of 0, and/or have null elements in it (also can be all null elements). If any of these conditions are met, the function is unusable and will be skipped over if nested in another seq op func.
//Edge cases: func length 0, func is null, or individual elements null (all elements could be null)
//if you forget to set the funcs variable, it is alright. The function will just be unusable.

//always manually reset the speed factors and set the output after this is done.
public class SeqOpFunc implements UniDirectionalFunc {

  //This is a conversion wrapper for non-unidirectional functions to become unidirectional.
  //This class is immutable.
  public static class UniWrapper implements UniDirectionalFunc {
    public final double endXVal;
    public final MathFunction graphFunc;
    public UniWrapper(final double endXVal, final MathFunction graphFunc) {
      this.endXVal = endXVal;
      this.graphFunc = graphFunc;
    }
    @Override
    public double getEndThreshold() {
      return endXVal;
    }
    @Override
    public double yValueOf(final double x) {
      return graphFunc.yValueOf(x);
    }
  }
  public static UniWrapper getUniWrapper(final double endXVal, final MathFunction graphFunc) {
    return new UniWrapper(endXVal, graphFunc);
  }

  //constructors
  protected SeqOpFunc() {
    //constructor for extending classes.
  }
  public SeqOpFunc(final UniDirectionalFunc ...funcs) {
    setFuncs(funcs);
  }

  private UniDirectionalFunc[] funcs = null;

  private UniDirectionalFunc currentFunc = null;
  private double currentThreshold = 0.0;
  private double translateVal = 0.0;
  private int index = 0; //visible to other objects of this class

  //this method gets the x value where the domain of the whole seq op math function ends. This is the threshold you should wait for if you are doing an input wait.
  //costly operation.
  @Override
  public double getEndThreshold() {
    double sum = 0;
    if (funcs != null) {
      for (UniDirectionalFunc i : funcs) {
        if (i != null)
          sum += i.getEndThreshold();
      }
    }
    return sum;
  }

  //This function resets the index back to the lowest applicable function. If none exists, then the function is made unusable.
  public void reset() {
    index = -1;

    //find the current func
    //if no current func can be found, then the function is unusable.
    currentFunc = null;
    incrementIndexAndSetCurrentFunc();

    translateVal = 0.0;
    if (currentFunc == null) {
      currentThreshold = 0.0;
    }
    else {
      currentThreshold = currentFunc.getEndThreshold();
    }
  }

  //This is the function you call to set the funcs array. It automatically resets the function.
  protected void setFuncs(final UniDirectionalFunc ...funcs) {
    this.funcs = funcs;
    //check if the funcs array cannot be used. If so, then mark this seq op func as unusable by setting the current func to null.
    if (funcs == null || funcs.length == 0) {
      index = 0;
      translateVal = 0.0;
      currentFunc = null;
      currentThreshold = 0.0;
    }
    else {
      reset();
    }
  }

  @Override
  public double yValueOf(final double input) {

    if (funcs == null) {
      throw new RuntimeException("You cannot get the y-value of a seq op function whose funcs array is null.");
    }
    if (index >= funcs.length) {
      throw new RuntimeException("You cannot get the y-value of a seq op function that has already ended");
    }

    if (input > currentThreshold) {
      increment();
    }
    
    return currentFunc.yValueOf(input - translateVal);

  }

  //This method is called when the x value exceeds the threshold.
  //returns whether or not the index has overflowed.
  private boolean increment() {

    //call the increment function for the current index and check if it has overflowed.
    //Check to see if you need to increment index.
    if (funcs[index] instanceof SeqOpFunc && !((SeqOpFunc) funcs[index]).increment()) {
      //No need to increment index

      //set currentFunc to whatever is the new one.
      currentFunc = ((SeqOpFunc) funcs[index]).currentFunc;
    }
    else {
      //Yes, have to increment index.
      if (!incrementIndexAndSetCurrentFunc()) {
        return true;
      }
    }

    //set other two config variables
    translateVal = currentThreshold;
    currentThreshold += currentFunc.getEndThreshold();
    return false;

  }

  //increments the index and sets the current function to the first one that is not null. If no match is found, then the current function is not changed.
  //returns whether or not a current function was found after the index.
  private boolean incrementIndexAndSetCurrentFunc() {
    //loop until the index has gone out of bounds or until you have reached a function that is not null.
    while (true) {
      //increment index
      ++index;
      if (index >= funcs.length) {
        return false;
      }
      //set the func variable
      UniDirectionalFunc tempFunc;
      if (funcs[index] instanceof SeqOpFunc) {
        tempFunc = ((SeqOpFunc) funcs[index]).currentFunc;
      }
      else {
        tempFunc = funcs[index];
      }
      //check if currentFunc is null. This indicates that there is no function at the index because the function is unusable, either because a sub seq op func's func array is 0 or null, or if the function itself is null.
      if (tempFunc != null) {
        currentFunc = tempFunc;
        return true;
      }
    }

  }

}