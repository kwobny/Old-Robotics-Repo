package org.firstinspires.ftc.teamcode.OpModes;

import org.firstinspires.ftc.teamcode.LithiumCore.Utils.BooleanConsumer;

//this is a listener that listens for whenever a boolean value is toggled.
// You continually provide a boolean into the listener and when the boolean transitions states, the consumer is called.
public class ToggleListener {
    // Whenever the callback gets executed, it is provided the latest value (value provided into set function).
    public BooleanConsumer callback;
    private boolean lastValue = false; //default last value is false.

    // You can override this in subclasses
    // You never call this method directly from a user.
    // The on toggle method calls the callback by default.
    protected void onToggle(final boolean value) {
        callback.accept(value);
    }

    public ToggleListener() {
        //
    }
    public ToggleListener(final BooleanConsumer callback) {
        this.callback = callback;
    }

    // The on toggle method is run when a value that is opposite from the latest value is inputted.
    // The on toggle method is provided the value inputted into the set function (latest value).
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
