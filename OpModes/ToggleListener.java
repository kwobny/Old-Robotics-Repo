package org.firstinspires.ftc.teamcode.OpModes;

import org.firstinspires.ftc.teamcode.LithiumCore.Utils.BooleanConsumer;

//this is a listener that listens for whenever a boolean value is toggled.
// You continually provide a boolean into the listener and when the boolean transitions states, the consumer is called.
public class ToggleListener {
    // Whenever the callback gets executed, it is provided the latest value (value provided into set function).
    public BooleanConsumer callback;
    private boolean lastValue = false; //default last value is false.

    // You can override this in subclasses
    protected void onToggle(final boolean value) {
        callback.accept(value);
    }

    public ToggleListener() {
        //
    }
    public ToggleListener(final BooleanConsumer callback) {
        this.callback = callback;
    }

    // The callback is run when a value that is opposite from the latest value is run.
    // The callback is provided the value inputted into the set function.
    public void set(final boolean value) {
        if (value != lastValue) {
            lastValue = value;
            onToggle(value);
        }
    }
    // This function is used to initialize the last value.
    public void setLastValue(final boolean value) {
        lastValue = value;
    }
}
