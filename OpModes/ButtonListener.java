package org.firstinspires.ftc.teamcode.OpModes;

import org.firstinspires.ftc.teamcode.LithiumCore.Utils.BooleanConsumer;

// This class is a version of a toggle listener that listens specifically
// for when the boolean transitions to a specific state (the trigger state).
public class ButtonListener extends ToggleListener {
    public ButtonListener() {
        //
    }
    public ButtonListener(final BooleanConsumer callback) {
        super(callback);
    }

    // The trigger state is the value when the callback should be called.
    // By default, it is false, which means that the callback triggers
    // when the boolean goes from true to false (button is pressed to not pressed).
    private boolean triggerState = false;

    // The method used to set the trigger state.
    public void setTriggerState(final boolean value) {
        triggerState = value;
    }

    @Override
    protected void onToggle(final boolean value) {
        if (value == triggerState) callback.accept(value);
    }
}