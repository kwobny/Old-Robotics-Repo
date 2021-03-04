package org.firstinspires.ftc.teamcode.utils;

import java.util.Objects;

// This class is a version of a toggle listener that listens specifically
// for when the value transitions to a specific state (the trigger state).
public class ButtonListener<T> extends ToggleListener<T> {
    
    public ButtonListener() {}
    public ButtonListener(final T value) {
        super(value);
    }

    // The trigger state is the value when the listener should indicate that
    // the value has changed.
    // By default, it is null, which means that the listener only triggers
    // when the value transitions to null.
    private T triggerState;
    
    // Returns true only when the value shifts to the trigger state.
    @Override
    public boolean set(final T value) {
        return super.set(value) && Objects.equals(value, triggerState);
    }

    public void setTriggerState(final T value) {
        triggerState = value;
    }
    public T getTriggerState() {
        return triggerState;
    }
    
}
