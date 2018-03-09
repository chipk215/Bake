package com.keyeswest.bake.utilities;


import android.content.Context;
import android.content.SharedPreferences;

import com.keyeswest.bake.models.Recipe;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecipeWriter {

    public static final String RECIPE_FILENAME="RECIPE_LIST";
    public static final String RECIPE_COUNT_KEY = "RECIPE_COUNT_KEY";

    public static void writeToSharedPreferences(Context context, List<Recipe> recipes){
        SharedPreferences.Editor editor = context.getSharedPreferences(RECIPE_FILENAME, MODE_PRIVATE).edit();

        editor.putInt(RECIPE_COUNT_KEY, recipes.size());
        for (int i=0;i< recipes.size(); i++){

            editor.putString(Integer.toString(i), recipes.get(i).getName());
        }

        editor.apply();
    }
}
