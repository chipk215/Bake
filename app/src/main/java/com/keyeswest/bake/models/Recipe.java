package com.keyeswest.bake.models;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe {

    @SerializedName("ingredients")
    private List<Ingredient> mIngredients;

    @SerializedName("id")
    private String mId;

    @SerializedName("servings")
    private String mServings;

    @SerializedName("name")
    private String mName;

    @SerializedName("image")
    private String mImage;

    @SerializedName("steps")
    private List<Step> steps;

    public List<Ingredient> getIngredients ()
    {
        return mIngredients;
    }

    public void setIngredients (List<Ingredient> ingredients)
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

    public List<Step> getSteps ()
    {
        return steps;
    }

    public void setSteps (List<Step> steps)
    {
        this.steps = steps;
    }

    @Override
    public String toString()
    {
        return "Recipe Ingredients = "+ mIngredients +", mId = "+ mId +", mServings = "+ mServings +", mName = "+ mName +", mImage = "+ mImage +", steps = "+steps+"]";
    }
}
