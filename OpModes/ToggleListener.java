package org.firstinspires.ftc.teamcode.OpModes;

// This is a listener that listens for whenever a boolean value is toggled.
// You continually provide a boolean into the listener and when the boolean transitions states, the consumer is called.
public class ToggleListener {

    private boolean value = false; //default value at beginning is false.

    // Sets the current value.
    // Returns true if a value that is opposite from the current value is inputted,
    // false if not.
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
