package com.keyeswest.bake.models;

import android.graphics.drawable.Drawable;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;



import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable{
    private static final String TAG="Recipe";


    @SerializedName("ingredients")
    private List<Ingredient> mIngredients;

    @SerializedName("id")
    private int mId;

    @SerializedName("servings")
    private int mServings;

    @SerializedName("name")
    private String mName;


    @SerializedName("steps")
    private List<Step> mSteps;


    @SerializedName("image")
    private String mRecipeImageUriString;


    //Attribution: Recipe descriptions were copied from similar recipes on the web. Just to
    // provide content for this assignment.
    @SerializedName("description")
    private String mDescription;

    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {

        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Recipe(){}

    private Recipe(Parcel in){

        mIngredients = new ArrayList<>();
        in.readTypedList(mIngredients, Ingredient.CREATOR);

        mId = in.readInt();

        mServings = in.readInt();

        mName = in.readString();

        mSteps = new ArrayList<>();
        in.readTypedList(mSteps, Step.CREATOR);

        mRecipeImageUriString = in.readString();

        mDescription = in.readString();


    }



    private Drawable thumbnail;

    public List<Ingredient> getIngredients ()
    {
        return mIngredients;
    }

    public void setIngredients (List<Ingredient> ingredients)
    {
        this.mIngredients = ingredients;
    }

    public int getId ()
    {
        return mId;
    }

    public void setId (int id)
    {
        this.mId = id;
    }

    public int getServings ()
    {
        return mServings;
    }

    public void setServings (int servings)
    {
        this.mServings = servings;
    }

    public String getName ()
    {
        return mName;
    }

    public void setName (String name)
    {
        this.mName = name;
    }

    public List<Step> getSteps ()
    {
        return mSteps;
    }

    public void setSteps (List<Step> steps)
    {
        this.mSteps = steps;
    }

    public String getRecipeImageUriString() {
        return mRecipeImageUriString;
    }

    public void setRecipeImageUriString(String recipeImageUriString) {
        mRecipeImageUriString = recipeImageUriString;
    }


    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getIngredientRecipeHash(){
        return "R_Ingredient_" + Integer.toString(getId()) ;
    }


    @Override
    public String toString()
    {
        return "Recipe Ingredients = "+ mIngredients +", mId = "+ mId +", mServings = "
                + mServings +", mName = "+ mName +", mImage = "+ mRecipeImageUriString
                +", mSteps = "+ mSteps +"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mIngredients);
        dest.writeInt(mId);
        dest.writeInt(mServings);
        dest.writeString(mName);
        dest.writeTypedList(mSteps);
        dest.writeString(mRecipeImageUriString);
        dest.writeString(mDescription);
    }
}
