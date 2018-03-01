package com.keyeswest.bake.models;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.keyeswest.bake.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class RecipeViewModel {
    private final static String TAG= "RecipeViewModel";

    private Context mContext;

    private Recipe mRecipe;

    public RecipeViewModel(Context context, Recipe recipe){
        mContext = context;
        mRecipe = recipe;
    }

    public String getName(){
        return mRecipe.getName();
    }

    public String getNumberSteps(){
        return Integer.toString(mRecipe.getSteps().size());
    }

    public String getServings(){
        return Integer.toString(mRecipe.getServings());
    }

    public String getDescription(){
        return mRecipe.getDescription();
    }

    public Drawable getDrawableRecipeImage(){
        Drawable result;
        try{
            Uri uri = Uri.parse(mRecipe.getRecipeImageUriString());
            InputStream inputStream =
                    mContext.getContentResolver().openInputStream(uri);
            result = Drawable.createFromStream(inputStream, uri.toString());
            return result;

        }catch(FileNotFoundException fne){
            Log.e(TAG, "Error accessing Recipe Image Drawable" + fne);
            result = mContext.getResources().getDrawable(R.drawable.baking);
            return result;
        }
    }
}
