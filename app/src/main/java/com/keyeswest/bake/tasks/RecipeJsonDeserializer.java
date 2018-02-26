package com.keyeswest.bake.tasks;


import android.os.AsyncTask;

import com.google.gson.Gson;
import com.keyeswest.bake.models.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeJsonDeserializer extends AsyncTask<String, Void, List<Recipe>> {

    private List<Recipe> recipes;
    final private RecipeResultsCallback mCallback;

    public interface RecipeResultsCallback{
        void recipeResult(List<Recipe> recipeList);
    }

    public RecipeJsonDeserializer(RecipeResultsCallback callback) {

        mCallback = callback;
        recipes = new ArrayList<>();
    }

    @Override
    protected List<Recipe> doInBackground(String... strings) {
        if ((strings.length == 1) && (strings[0] != null)){
            Gson gson = new Gson();
            Recipe[] recipeArray = gson.fromJson(strings[0], Recipe[].class);

            recipes = Arrays.asList(recipeArray);
        }
        return recipes;
    }

    @Override
    protected void onPostExecute(List<Recipe> recipeList){
        mCallback.recipeResult(recipeList);
    }
}
