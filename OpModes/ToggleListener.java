package org.firstinspires.ftc.teamcode.OpModes;

import org.firstinspires.ftc.teamcode.LithiumCore.Utils.BooleanConsumer;

//this is a listener that listens for whenever a boolean value is toggled.
public class ToggleListener {
    // Whenever the callback gets executed, it is provided the latest value (value provided into set function).
    public BooleanConsumer callback;
    private boolean lastValue = false; //default last value is false.

    public ToggleListener() {
        //
    }
    public ToggleListener(final BooleanConsumer callback) {
        this.callback = callback;
    }

    //the callback is provided the value provided into the set function.
    public void set(final boolean value) {
        if (value != lastValue) {
            lastValue = value;
            callback.accept(value);
        }
    }
    public void setLastValue(final boolean value) {
        lastValue = value;
    }
}
