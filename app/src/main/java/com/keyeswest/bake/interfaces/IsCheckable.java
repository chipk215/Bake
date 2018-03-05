package com.keyeswest.bake.interfaces;


public interface IsCheckable {
    String getUniqueId();
    void setCheckedState(boolean state);
    boolean getCheckedState();
}
