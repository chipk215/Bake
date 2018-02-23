package com.keyeswest.bake.Loaders;


import android.content.Context;
import android.os.Bundle;

import android.support.v4.content.AsyncTaskLoader;


import com.keyeswest.bake.utilities.NetworkUtilities;

import java.io.IOException;
import java.net.URL;

public class RecipeLoader extends AsyncTaskLoader<String> {
    private Bundle mArgs;

    public static final String RECIPE_URL_EXTRA = "recipe_url";

    public RecipeLoader(Context context, Bundle args) {
        super(context);
        mArgs = args;
    }

    @Override
    protected void onStartLoading(){

        if (mArgs == null){
            return;
        }
        // set loading indicator to visible

        forceLoad();
    }

    @Override
    public String loadInBackground() {

        String recipeUrlString = mArgs.getString(RECIPE_URL_EXTRA);

        if ((recipeUrlString == null) || recipeUrlString.isEmpty()){
            return null;
        }

        try{
            URL recipeUrl = new URL(recipeUrlString);
            String jsonRecipes = NetworkUtilities.getResponseFromHttpsUrl(recipeUrl);
            return jsonRecipes;
        }catch (IOException ioe){
            ioe.printStackTrace();
            return null;
        }
    }


}
