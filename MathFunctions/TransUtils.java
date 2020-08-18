package MathFunctions;

//defines utility functions for using transformations

public class TransUtils {
  
  //for the trans parameter, the lowest indices are connected first to the actual math root function.
  public static MathFunction applyTrans(final MathFunction root, final Transformation ...trans) {
    if (trans.length == 0) 
      return root;

    trans[0].next = root;
    for (int i = 1; i < trans.length; i++) {
      trans[i].next = trans[i-1];
    }
    return trans[trans.length];
  }

}