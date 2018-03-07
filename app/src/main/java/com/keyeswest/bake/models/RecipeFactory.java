package com.keyeswest.bake.models;


import android.content.Context;

import com.keyeswest.bake.tasks.RecipeJsonDeserializer;

public class RecipeFactory {

    private RecipeFactory(){};

    public static void readRecipes(Context context,
                                   RecipeJsonDeserializer.RecipeResultsCallback results){
        RecipeJsonDeserializer deserializer =
                new RecipeJsonDeserializer(context, results);

        deserializer.execute();

    }
}
