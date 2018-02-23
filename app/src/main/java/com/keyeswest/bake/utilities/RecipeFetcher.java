package com.keyeswest.bake.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;


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


    //Implementing NetworkUtilities as a lazily loaded property for testing.
    // Don't know how to do dependency injection in Android yet
    private NetworkUtilities mNetworkUtilities;

    public NetworkUtilities getNetworkUtilities() {
        if (mNetworkUtilities == null){
            mNetworkUtilities =  new NetworkUtilities();
        }
        return mNetworkUtilities;
    }


    public void setNetworkUtilities(NetworkUtilities networkUtilities) {
        mNetworkUtilities = networkUtilities;
    }



    public interface RecipeJsonResults {
        void handleRecipeJSON(String recipeJson);
        void networkUnavailable();
    }

    public RecipeFetcher(Context context, RecipeJsonResults callback){
        mContext = context;
        mRecipeCallback = callback;

    }



    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        return new RecipeLoader(mContext, args);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String result) {
        // turn off loading indicator

        if (result == null){
            // show error message
        }else{

            mRecipeCallback.handleRecipeJSON(result);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // not used but override required
    }


    public void fetchRecipes(LoaderManager loaderManager) throws IllegalArgumentException{

        // verify the callback is not null
        if (mRecipeCallback == null){
            throw new IllegalArgumentException("Callback method not provided");
        }

        // check network availability
        if (! getNetworkUtilities().isNetworkAvailable(mContext) ){
            mRecipeCallback.networkUnavailable();
            return;
        }

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
