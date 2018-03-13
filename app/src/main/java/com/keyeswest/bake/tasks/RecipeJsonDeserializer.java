package com.keyeswest.bake.tasks;


import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.keyeswest.bake.R;
import com.keyeswest.bake.models.Recipe;
import com.keyeswest.bake.models.Step;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeJsonDeserializer extends AsyncTask<Void, Void, List<Recipe>> {
    private final static String TAG = "RecipeJsonDeserializer";


    private List<Recipe> recipes;
    final private RecipeResultsCallback mCallback;
    final private Context mContext;

    public interface RecipeResultsCallback{
        void recipeResult(List<Recipe> recipeList);
    }

    public RecipeJsonDeserializer(Context context, RecipeResultsCallback callback) {

        mCallback = callback;
        recipes = new ArrayList<>();
        mContext = context;
    }

    @Override
    protected List<Recipe> doInBackground(Void... parameters) {

        String jsonData = readJsonFromAssets();
        if (jsonData != null){
            Gson gson = new Gson();
            Recipe[] recipeArray = gson.fromJson(jsonData, Recipe[].class);
            recipes = Arrays.asList(recipeArray);

            // set the number of steps in the recipe on each step to facilitate
            // presenting step 1/N
            for (Recipe recipe : recipes){

                int lastStep = recipe.getSteps().size();


                for (Step step : recipe.getSteps()){
                    step.setNumberOfStepsInRecipe(lastStep);
                }
            }

        }

        return recipes;
    }


    @Override
    protected void onPostExecute(List<Recipe> recipeList){
        mCallback.recipeResult(recipeList);
    }

    private String readJsonFromAssets() {

        try{
            AssetManager manager = mContext.getAssets();

            InputStream inputStream = manager.open(mContext.getString(R.string.recipe_json));
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer, "UTF-8");
        }catch(IOException ioe){
            Log.e(TAG, "Failed to read recipe.json file from assets. " + ioe);
            return null;
        }

    }
}
