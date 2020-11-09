package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils.Pile_Interfaces.*;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Consumer;

import java.util.*;

//This class is basically a random pile of stuff.
//You can't single out a specific thing or delete anything.
//You can only add to the pile and go through everything at once.
//You can add to the pile while iterating.
//You can add the same thing to this pile twice.
public class SimplePile<T> implements AddablePile<T> {

  //Element array. Can be accessed by subclasses
  protected ArrayList<T> elemArr = new ArrayList<>();
  
  @Override
  public void add(final T elem) {
    elemArr.add(elem);
  }

  public void forAll(final Consumer<T> consumer) {
    Iterator<T> iter = elemArr.iterator();
    while (iter.hasNext()) {
      consumer.run(iter.next());
    }

  }

}