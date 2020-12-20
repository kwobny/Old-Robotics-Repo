package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import java.util.Collection;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.functiontypes.Predicate;

public interface Pile<T> extends Collection<T>, Foreachable<T> {
  //removes all of the elements that satisfy the given predicate. (predicate return value of true --> satisfied, false --> not satisfied)
  //returns true if any elements were removed,
  //else returns false.
  boolean removeIf(Predicate<? super T> filter);
}