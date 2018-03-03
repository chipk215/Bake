package com.keyeswest.bake.models;


import android.content.Context;

import com.keyeswest.bake.interfaces.HasUniqueId;

public class StepViewModel implements HasUniqueId {

    private Context mContext;
    private Step mStep;

    private boolean mCheckedState;

    public StepViewModel(Context context, Step step){
        mContext = context;
        mStep = step;

    }

    public String getShortDescription(){
        return mStep.getShortDescription();
    }

    public String getDescription(){
        return mStep.getDescription();
    }

    public String getVideoURL ()
    {
        return mStep.getVideoURL();
    }

    public boolean getCheckedState(){
        return mCheckedState;
    }

    public void setCheckedState(boolean checkedState){
        mCheckedState = checkedState;

    }

    public String getId(){
        return Integer.toString(mStep.getId());
    }

    @Override
    public String getUniqueId() {
        return mStep.getUniqueId();
    }
}
