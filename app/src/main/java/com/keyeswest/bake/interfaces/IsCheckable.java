package com.keyeswest.bake.interfaces;


/**
 * Enables a generic asynch task to handle both ingredients and steps when saving checkbox state
 * to Shared Preferences.
 */
public interface IsCheckable {
    String getUniqueId();
    void setCheckedState(boolean state);
    boolean getCheckedState();
}
