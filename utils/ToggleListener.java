package org.firstinspires.ftc.teamcode.utils;

import java.util.Objects;

// This is a listener that listens for whenever an object changes.
// You continually provide an object into the listener and when the object changes value,
// The set function notifies you by returning true.
public class ToggleListener<T> {
    
    public ToggleListener() {}
    // The constructor below lets you initialize the value in the listener.
    public ToggleListener(final T value) {
        this.value = value;
    }

    private T value; //default value at beginning is null.

    // Sets the current value.
    // Returns true when a value that is different from the current value is inputted,
    // i.e. when the value inputted is different from the last value.
    // Returns false if not.
    // This method uses the equals method to compare values.
    public boolean set(final T value) {
        if (!Objects.equals(this.value, value)) {
            this.value = value;
            return true;
        }
        return false;
    }

    // Returns the current value in the toggle listener.
    public T get() {
        return value;
    }

}
