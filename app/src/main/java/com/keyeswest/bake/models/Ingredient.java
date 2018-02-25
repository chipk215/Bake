package com.keyeswest.bake.models;


import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("measure")
    private String mMeasure;

    @SerializedName("ingredient")
    private String mIngredientName;

    @SerializedName("quantity")
    private String mQuantity;

    public String getMeasure ()
    {
        return mMeasure;
    }

    public void setMeasure (String measure)
    {
        this.mMeasure = measure;
    }

    public String getIngredientName()
    {
        return mIngredientName;
    }

    public void setIngredientName(String ingredientName)
    {
        this.mIngredientName = ingredientName;
    }

    public String getQuantity ()
    {
        return mQuantity;
    }

    public void setQuantity (String quantity)
    {
        this.mQuantity = quantity;
    }

    @Override
    public String toString()
    {
        return "Ingredient [mMeasure = "+ mMeasure +", mIngredientName = "+ mIngredientName +", mQuantity = "+ mQuantity +"]";
    }
}
