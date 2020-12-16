package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

//This is an interface which provides the user of a client library a way to interact with the pile directly.
//The pile interface follows a policy where users can only know and interact with elements that they themselves have added. Users cannot mess with elements that they have not added to the pile, hence the absence of iterating methods.

public interface PileInterface<T> {

  //methods to add and remove elements.
  //They should normally return the added/removed element, but technically don't have too.

  //Add method is required.
  //Should throw an error if the element to be added is already in the pile (equals method equality). However, this does not have to be the case for some piles where efficiency is paramount.
  public T add(T element);

  //this method throws an error/exception if the element to be removed is not present.
  public T remove(T element);
  //this method does not throw an error, and only removes if the element is present.
  //true means the element was removed. False means the element did not exist in the pile and was not removed.
  public boolean removeIfPresent(T element);

  //returns a boolean signifying whether or not the element is in the pile.
  //true means the element is present in the pile, false means the element is not present.
  public boolean contains(T element);

}