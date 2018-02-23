package com.keyeswest.bake.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.keyeswest.bake.Loaders.RecipeLoader;

/**
 * Handles fetching the network recipe resource json file
 */
public class RecipeFetcher implements LoaderManager.LoaderCallbacks<String>{

    private static final String TAG= "RecipeFetcher";

    private static final int RECIPE_LOADER = 214;

    private static final String RECIPE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private Context mContext;

    private RecipeJsonResults mRecipeCallback;

    public interface RecipeJsonResults {
        void handleRecipeJSON(String recipeJson);
    }

    public RecipeFetcher(Context context, RecipeJsonResults callback){
        mContext = context;
        mRecipeCallback = callback;
    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        // verify the callback is not null
        if (mRecipeCallback == null){
            return null;
        }

        return new RecipeLoader(mContext, args);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        // turn off loading indicator

        if (data == null){
            // show error message
        }else{

            mRecipeCallback.handleRecipeJSON(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // not used but override required
    }


    public void fetchRecipes(LoaderManager loaderManager){
        Bundle queryBundle = new Bundle();
        queryBundle.putString(RecipeLoader.RECIPE_URL_EXTRA, RECIPE_URL);

       // LoaderManager loaderManager = mContext.getSupportLoaderManager();
        Loader<String> recipeLoader = loaderManager.getLoader(RECIPE_LOADER);

        if (recipeLoader == null){
            loaderManager.initLoader(RECIPE_LOADER, queryBundle, this);
        } else{
            loaderManager.restartLoader(RECIPE_LOADER, queryBundle, this);
        }


    }

}
