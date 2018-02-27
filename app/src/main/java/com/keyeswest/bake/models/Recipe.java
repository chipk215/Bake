package com.keyeswest.bake.models;


import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;
import com.keyeswest.bake.R;

import java.util.List;

public class Recipe {

    private static final int EASY_THRESHOLD = 15;
    private static final int AVERAGE_THRESHOLD = 25;



    @SerializedName("ingredients")
    private List<Ingredient> mIngredients;

    @SerializedName("id")
    private int mId;

    @SerializedName("servings")
    private int mServings;

    @SerializedName("name")
    private String mName;

    @SerializedName("image")
    private String mImage;

    @SerializedName("steps")
    private List<Step> mSteps;

    public Drawable getThumbnail(Context context) {
        switch(mId){
            case 1: return  context.getResources().getDrawable(R.drawable.nutella);
            case 2: return  context.getResources().getDrawable(R.drawable.brownie);
            case 3: return  context.getResources().getDrawable(R.drawable.yellowcake);
            case 4: return  context.getResources().getDrawable(R.drawable.cheesecake);
            default:
                 return context.getResources().getDrawable(R.drawable.baking);

        }
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
        return mSteps;
    }

    public void setSteps (List<Step> steps)
    {
        this.mSteps = steps;
    }

    public String getComplexity(Context context){
         int sum = mSteps.size() + mIngredients.size();
          if (sum < EASY_THRESHOLD){
              return context.getResources().getString(R.string.easy);
          } else if(sum < AVERAGE_THRESHOLD){
              return context.getResources().getString(R.string.average);
          }

          return context.getResources().getString(R.string.difficult);

    }

    @Override
    public String toString()
    {
        return "Recipe Ingredients = "+ mIngredients +", mId = "+ mId +", mServings = "+ mServings +", mName = "+ mName +", mImage = "+ mImage +", mSteps = "+ mSteps +"]";
    }
}
