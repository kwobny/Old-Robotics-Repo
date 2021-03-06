package org.firstinspires.ftc.teamcode.OpModes;

// This is a listener that listens for whenever a boolean value is toggled.
// You continually provide a boolean into the listener and when the boolean transitions states,
// The set function notifies you by returning true.
public class ToggleListener {
    
    public ToggleListener() {}
    // The constructor below lets you initialize the boolean value in the listener.
    public ToggleListener(final boolean value) {
        this.value = value;
    }

    private boolean value = false; //default value at beginning is false.

    // Sets the current value.
    // Returns true when a value that is opposite from the current value is inputted,
    // i.e. when the value inputted is different from the last value.
    // Returns false if not.
    public boolean set(final boolean value) {
        if (this.value != value) {
            this.value = value;
            return true;
        }
        return false;
    }

    // Returns the current value in the toggle listener.
    public boolean get() {
        return value;
    }

}
