package lithiumcore.utils.unidirectionfunc;

//This class is a sequential operation function which is optimized for a nested seq op func structure.
//this class is an immutable class, except when extended.

//when extending this class, the one and only requirement is to set the funcs variable to something via the setFuncs function.

//Unlike the other seq op funcs, this seq op func can have edge cases in its funcs variable.
//The funcs variable can be set to null, be a length of 0, and/or have null elements in it (also can be all null elements). If any of these conditions are met, the function is unusable and will be skipped over if nested in another seq op func.
//Edge cases: func length 0, func is null, or individual elements null (all elements could be null)
//if you forget to set the funcs variable, it is alright. The function will just be unusable.

//always manually reset the speed factors and set the output after this is done.
public class NestedSOF extends BasicSOF {

  //constructors
  protected NestedSOF() {
    //constructor for extending classes.
  }
  public NestedSOF(final UniDirectionalFunc ...funcs) {
    super(funcs);
  }

  //private UniDirectionalFunc[] funcs = null;

  private UniDirectionalFunc currentFunc = null;
  //private double xThreshold = 0.0;
  //private double xTranslateVal = 0.0;
  //private int index = 0; //visible to other objects of this class

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

  @Override
  public double getLastYValue() {
    return lastYVal;
  }

  //This function resets the index back to the lowest applicable function. If none exists, then the function is made unusable.
  @Override
  public void reset() {
    index = -1;

    //find the current func
    //if no current func can be found, then the function is unusable.
    currentFunc = null;
    incrementIndexAndSetCurrentFunc();

    xTranslateVal = 0.0;
    if (currentFunc == null) {
      xThreshold = 0.0;
    }
    else {
      xThreshold = currentFunc.getEndThreshold();
    }
  }

  //This is the function you call to set the funcs array. It automatically resets the function.
  @Override
  protected void setFuncs(final UniDirectionalFunc ...funcs) {
    this.funcs = funcs;
    //check if the funcs array cannot be used. If so, then mark this seq op func as unusable by setting the current func to null.
    if (funcs == null || funcs.length == 0) {
      index = 0;
      xTranslateVal = 0.0;
      currentFunc = null;
      xThreshold = 0.0;
      lastYVal = 0;
    }
    else {
      lastYVal = funcs[funcs.length-1].getLastYValue();
      reset();
    }
  }

  @Override
  public double yValueOf(final double input) {

    if (funcs == null) {
      throw new RuntimeException("You cannot get the y-value of a seq op function whose funcs array is null.");
    }
    if (index >= funcs.length) {
      //throw new RuntimeException("You cannot get the y-value of a seq op function that has already ended");
      return lastYVal;
    }

    if (input > xThreshold && increment()) {
      return lastYVal;
    }
    
    return currentFunc.yValueOf(input - xTranslateVal);

  }

  //This method is called when the x value exceeds the threshold.
  //returns whether or not the index has overflowed.
  @Override
  boolean increment() {

    //call the increment function for the current index and check if it has overflowed.
    //Check to see if you need to increment index.
    if (funcs[index] instanceof NestedSOF && !((NestedSOF) funcs[index]).increment()) {
      //No need to increment index

      //set currentFunc to whatever is the new one.
      currentFunc = ((NestedSOF) funcs[index]).currentFunc;
    }
    else {
      //Yes, have to increment index.
      if (!incrementIndexAndSetCurrentFunc()) {
        return true;
      }
    }

    //set other two config variables
    xTranslateVal = xThreshold;
    xThreshold += currentFunc.getEndThreshold();
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
      if (funcs[index] instanceof NestedSOF) {
        tempFunc = ((NestedSOF) funcs[index]).currentFunc;
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