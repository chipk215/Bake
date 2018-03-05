package com.keyeswest.bake.models;


import android.content.Context;


public class StepViewModel {

    private Context mContext;
    private Step mStep;

    public StepViewModel(Context context, Step step){
        mContext = context;
        mStep = step;

    }


    public String getDescription(){
        return mStep.getDescription();
    }

    public String getVideoURL ()
    {
        return mStep.getVideoURL();
    }

    public boolean getCheckedState(){
        return mStep.getCheckedState();
    }

    public void setCheckedState(boolean checkedState){
      mStep.setCheckedState(checkedState);
    }

    private String getLabelId(){
        return Integer.toString(mStep.getId()+1);
    }

    private String getLastStep(){
        return Integer.toString(mStep.getNumberOfStepsInRecipe());
    }

    public String getListLabel(){
        return "(" + getLabelId() + "/" + getLastStep() + ") " + mStep.getShortDescription();
    }

    public Step getStep(){
        return mStep;
    }


    public String getUniqueId() {
        return mStep.getUniqueId();
    }
}
