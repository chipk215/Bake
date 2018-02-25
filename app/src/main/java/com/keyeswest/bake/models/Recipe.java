package com.keyeswest.bake.models;


import com.google.gson.annotations.SerializedName;

public class Recipe {

    @SerializedName("ingredients")
    private Ingredient[] mIngredients;

    @SerializedName("id")
    private String mId;

    @SerializedName("servings")
    private String mServings;

    @SerializedName("name")
    private String mName;

    @SerializedName("image")
    private String mImage;

    @SerializedName("steps")
    private Step[] steps;

    public Ingredient[] getIngredients ()
    {
        return mIngredients;
    }

    public void setIngredients (Ingredient[] ingredients)
    {
        this.mIngredients = ingredients;
    }

    public String getId ()
    {
        return mId;
    }

    public void setId (String id)
    {
        this.mId = id;
    }

    public String getServings ()
    {
        return mServings;
    }

    public void setServings (String servings)
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

    public String getImage ()
    {
        return mImage;
    }

    public void setImage (String image)
    {
        this.mImage = image;
    }

    public Step[] getSteps ()
    {
        return steps;
    }

    public void setSteps (Step[] steps)
    {
        this.steps = steps;
    }

    @Override
    public String toString()
    {
        return "Recipe [mIngredients = "+ mIngredients +", mId = "+ mId +", mServings = "+ mServings +", mName = "+ mName +", mImage = "+ mImage +", steps = "+steps+"]";
    }
}
