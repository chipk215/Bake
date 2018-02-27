package com.keyeswest.bake.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable{

    @SerializedName("measure")
    private String mMeasure;

    @SerializedName("ingredient")
    private String mIngredientName;

    @SerializedName("quantity")
    private String mQuantity;

    public static final Parcelable.Creator<Ingredient> CREATOR
            = new Parcelable.Creator<Ingredient>(){

        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    private Ingredient(Parcel in){
        mMeasure = in.readString();
        mIngredientName = in.readString();
        mQuantity = in.readString();

    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mIngredientName);
        dest.writeString(mMeasure);
        dest.writeString(mQuantity);
    }
}
