package com.keyeswest.bake.models;


import android.content.Context;

import com.keyeswest.bake.tasks.RecipeJsonDeserializer;


/**
 * Encapsulates the gson deserialization, makes it easy to change the source of the data.
 */
public class RecipeFactory {

    private RecipeFactory(){};

    public static void readRecipes(Context context,
                                   RecipeJsonDeserializer.RecipeResultsCallback results){
        RecipeJsonDeserializer deserializer =
                new RecipeJsonDeserializer(context, results);

        deserializer.execute();

    }
}
