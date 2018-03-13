package com.keyeswest.bake.Widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.keyeswest.bake.R;
import com.keyeswest.bake.models.Ingredient;
import com.keyeswest.bake.models.IngredientViewModel;
import com.keyeswest.bake.models.Recipe;


import java.io.IOException;
import java.io.InputStream;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Provides recipe and ingredient list data to the widget.
 *
 * Recipe data is read from the json file in onCreate so that the list sizes can be set.
 *
 * The ingredients list is read from shared preferences storage and uses the ingredient
 * checked state property to identify whether the user has checked the ingredient as being
 * available or not.
 */
public class RecipeWidgetService extends RemoteViewsService {

    private static final String TAG="RecipeWidgetService";

    private static final int INVALID_INDEX = -1;

    // Tracks whether the shared preference ingredients file for the corresponding
    // recipe has been read. The key is the recipe index.
    private static Hashtable<Integer, Boolean> mUserIngredientsRead = new Hashtable<>();

    public static void refreshIngredients(){
        mUserIngredientsRead.clear();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(this.getApplicationContext(), intent);
    }


    class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

        private Context mContext;

        private  int mRecipeIndex = INVALID_INDEX;
        // indicates whether the request is for recipes or ingredients
        private boolean mIsRecipeRequest;
        private List<Recipe> mRecipes;
        private List<Ingredient> mIngredients;


        /*
         * The intents from the widget are encoded so that the service can return either a list
         * of recipes or a list of ingredients. The scheme specific part of the data URI is used to
         * encode the request using the format:
         *    <appWidgetId>: (<R> or <I>),<recipe index>
         *       - where the recipe index is only included if the request is for ingredients (I)
         * During coding it was noticed that the comma is only present when the request is for
         * ingredients and is used to simplify parsing.
         */
        RecipeRemoteViewsFactory(Context context, Intent intent){
            mContext = context;

            parseIntent(intent);

            // Our implementation has no specific app widget dependencies, if it did
            // the appWidgetId is contained in the intent
            //String appWidgetId = scheme_specific_part.substring(0,colonIndex );
        }




        @Override
        public void onCreate() {
            Log.d(TAG, "Entering onCreate in RecipeRemotesViewFactory");

            // This is a synchronous read on the invoking thread. The problem with using a
            // background thread is that the onCreate method will exit before the list sizes
            //  are known creating a race condition with the subsequent invocation of getCount.
            //
            // Perhaps a background task could be used and in completion handler invoke
            // onDataSetChanged? - TODO research this
            //
            String json = readJsonFromAssets();
            if (json != null) {
                Gson gson = new Gson();
                Recipe[] recipeArray = gson.fromJson(json, Recipe[].class);
                mRecipes = Arrays.asList(recipeArray);

                if (! mIsRecipeRequest){
                    // The request is for ingredients so set an ingredients list property
                    // to the corresponding list of requested ingredients
                    mIngredients = mRecipes.get(mRecipeIndex).getIngredients();
                }
            }
        }


        @Override
        public void onDataSetChanged() {
            Log.d(TAG, "onDataSetChanged");
        }

        @Override
        public void onDestroy() {
            Log.d(TAG,"onDestroy invoked " );

        }

        @Override
        public int getCount() {
            Log.d(TAG,"getCount invoked returning: " + Integer.toString(mRecipes.size()));
            if (mIsRecipeRequest) {
                return mRecipes.size();
            }else{
                return mIngredients.size();
            }
        }


        /**
         * Handles the remote views for lists of recipes and ingredients.
         * @param position - list position of the view
         * @return
         */
        @Override
        public RemoteViews getViewAt(int position) {

            Log.d(TAG, "getViewAt invoked for position" + Integer.toString(position));

            RemoteViews remoteView = new RemoteViews(mContext.getPackageName(),
                    R.layout.recipe_row_item);

            if (mIsRecipeRequest) {

                // The list is displaying recipes

                // set the recipe name in the view
                remoteView.setTextViewText(R.id.item, mRecipes.get(position).getName());

                // Next, we set a fill-intent which will be used to fill-in the pending intent
                // template which is set on the collection view to support clicking on
                // the recipe name.  Clicking on the recipe item will result in displaying the
                // corresponding recipe's ingredient list.
                Bundle extras = new Bundle();
                extras.putInt(BakeAppWidget.EXTRA_ITEM_POSITION, position);
                extras.putString(BakeAppWidget.EXTRA_ITEM_RECIPE_NAME,mRecipes.get(position).getName());
                Intent fillInIntent = new Intent();
                fillInIntent.putExtras(extras);
                remoteView.setOnClickFillInIntent(R.id.item, fillInIntent);

            }else{

                // The list is displaying ingredient items

                IngredientViewModel viewModel = new IngredientViewModel(mContext, mIngredients.get(position));

                // set the ingredient name
                remoteView.setTextViewText(R.id.item,viewModel.getIngredientInfo());


                // RemoteViews does not support checkboxes so we will color code the ingredient
                // based upon the checkbox state of the ingredient set by the user in the app.
                // An unchecked ingredient indicates the user needs to obtain the item, a checked
                // ingredient indicates the item is on hand.

                // To obtain the ingredient state we need to read a shared preferences file
                // corresponding to the ingredients.  An ingredients file exists for each recipe
                // and only needs to be read once per ingredient.

                if ( ! mUserIngredientsRead.containsKey(mRecipeIndex)){
                    // the ingredients file has not been read for this recipe so read it.
                    readUserIngredients();
                }


                if (! mIngredients.get(position).getCheckedState()){
                    // need to acquire ingredient
                    remoteView.setTextColor(R.id.item,
                            mContext.getResources().getColor(R.color.colorOutOfItem));
                }else{
                    // ingredient is on hand
                    remoteView.setTextColor(R.id.item,
                            mContext.getResources().getColor(R.color.colorItemInStock));
                }
            }

            return remoteView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position ;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }


        // read shared preferences to obtain ingredient availability
        private void readUserIngredients(){
            String fileName = mRecipes.get(mRecipeIndex).getSharedPreferencesIngredientFileName();
            for (Ingredient i : mIngredients){
                Boolean isChecked = mContext.getSharedPreferences(fileName, MODE_PRIVATE)
                        .getBoolean(i.getUniqueId(), true);
                Log.d(TAG, "I-name: " + i.getIngredientName() + "checked: " + Boolean.toString(isChecked));
                i.setCheckedState(isChecked);
            }

            mUserIngredientsRead.put(mRecipeIndex,true);

        }

        // Decode the intent scheme to determine if request is for recipe or ingredients
        private void parseIntent(Intent intent){

            String scheme_specific_part = intent.getData().getSchemeSpecificPart();

            int commaIndex = scheme_specific_part.indexOf(",");

            if (commaIndex == INVALID_INDEX){
                // we have a recipe request
                mIsRecipeRequest = true;
            }else{
                // we have an ingredient request
                mIsRecipeRequest = false;
                String recipeIndexString = scheme_specific_part.substring(commaIndex+1);
                mRecipeIndex = Integer.valueOf(recipeIndexString);

            }
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
}
