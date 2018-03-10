package com.keyeswest.bake.Widget;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;


public class RecipeWidgetService extends RemoteViewsService {

    private static final String TAG="RecipeWidgetService";

    private Hashtable<Integer, Boolean> mUserIngredientsRead = new Hashtable<>();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(this.getApplicationContext(), intent);
    }


    class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

        private Context mContext;
        private int mAppWidgetId;
        private  int mRecipeIndex = -1;
        private boolean mIsRecipeRequest;

        private List<Recipe> mRecipes = new ArrayList<>();
        private List<Ingredient> mIngredients = new ArrayList<>();



        RecipeRemoteViewsFactory(Context context, Intent intent){
            mContext = context;

            String scheme_specific_part = intent.getData().getSchemeSpecificPart();

            int colonIndex = scheme_specific_part.indexOf(":");
            int commaIndex = scheme_specific_part.indexOf(",");

            if (commaIndex == -1){
                // we have a recipe request
                mIsRecipeRequest = true;
            }else{
                // we have an ingredient request
                mIsRecipeRequest = false;
                String recipeIndexString = scheme_specific_part.substring(commaIndex+1);
                mRecipeIndex = Integer.valueOf(recipeIndexString);

            }

            String appWidgetId = scheme_specific_part.substring(0,colonIndex );
            mAppWidgetId = Integer.valueOf(appWidgetId);



        }

        @Override
        public void onCreate() {
            Log.d(TAG, "Entering onCreate in RecipeRemotesViewFactory");
            String json = readJsonFromAssets();
            if (json != null) {
                Gson gson = new Gson();
                Recipe[] recipeArray = gson.fromJson(json, Recipe[].class);
                mRecipes.addAll(Arrays.asList(recipeArray));

                if (! mIsRecipeRequest){
                    mIngredients = mRecipes.get(mRecipeIndex).getIngredients();
                }

                Log.d(TAG, "Recipes read.. First recipe: " + mRecipes.get(0).getName());

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

        @Override
        public RemoteViews getViewAt(int position) {

            Log.d(TAG, "getViewAt invoked for position" + Integer.toString(position));

            RemoteViews remoteView = new RemoteViews(mContext.getPackageName(),
                    R.layout.recipe_row_item);

            if (mIsRecipeRequest) {
                remoteView.setTextViewText(R.id.item, mRecipes.get(position).getName());

                // Next, we set a fill-intent which will be used to fill-in the pending intent template
                // which is set on the collection view in StackWidgetProvider.
                Bundle extras = new Bundle();
                extras.putInt(BakeAppWidget.EXTRA_ITEM_POSITION, position);
                extras.putString(BakeAppWidget.EXTRA_ITEM_RECIPE_NAME,mRecipes.get(position).getName());
                Intent fillInIntent = new Intent();
                fillInIntent.putExtras(extras);
                remoteView.setOnClickFillInIntent(R.id.item, fillInIntent);
                // You can do heaving lifting in here, synchronously. For example, if you need to
                // process an image, fetch something from the network, etc., it is ok to do it here,
                // synchronously. A loading view will show up in lieu of the actual contents in the
                // interim.
            }else{
                IngredientViewModel viewModel = new IngredientViewModel(mContext, mIngredients.get(position));
                remoteView.setTextViewText(R.id.item,viewModel.getIngredientInfo());


                if ( ! mUserIngredientsRead.containsKey(mRecipeIndex)){
                    readUserIngredients();
                }

                if (! mIngredients.get(position).getCheckedState()){
                        remoteView.setTextColor(R.id.item, Color.RED);
                               // mContext.getResources().getColor(R.color.colorSecondary));
                }else{
                    remoteView.setTextColor(R.id.item, Color.DKGRAY );
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
    }
}
