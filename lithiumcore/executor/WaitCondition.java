package LithiumCore.Wait_Package;

//What the implementing wait class is recommended to look like
/*
public interface WaitCondition {
  //a bunch of data variables for each specific wait

  public Constructor(any number of parameters); //constructor converts the arguments into the data necessary (is like the old generate data function)

  public boolean pollCondition(); //true means end wait, false means wait is still continuing
}
*/

//actual interface (since some types of methods cannot be defined in an interface)
public interface WaitCondition {

  public boolean pollCondition(); //true means end wait, false means wait is still continuing

}