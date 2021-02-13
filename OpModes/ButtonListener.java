package org.firstinspires.ftc.teamcode.OpModes;

// This class is a version of a toggle listener that listens specifically
// for when the boolean transitions to a specific state (the trigger state).
public class ButtonListener extends ToggleListener {

    // The trigger state is the value when the listener should indicate that
    // The value has changed.
    // By default, it is false, which means that the callback triggers
    // when the boolean goes from true to false (button is pressed to not pressed).
    public boolean triggerState = false;
    
    // Returns true when the value shifts to the trigger state.
    @Override
    public boolean set(final boolean value) {
        return super.set(value) && value == triggerState;
    }
}