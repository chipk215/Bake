package com.keyeswest.bake;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.keyeswest.bake.models.Recipe;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RecipeWidgetService extends RemoteViewsService {

    private static final String TAG="RecipeWidgetService";
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(this.getApplicationContext(), intent);
    }


    class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{



        private Context mContext;
        private int mAppWidgetId;

        private List<Recipe> mRecipes = new ArrayList<>();


        RecipeRemoteViewsFactory(Context context, Intent intent){
            mContext = context;
            mAppWidgetId = Integer.valueOf(intent.getData().getSchemeSpecificPart());


        }

        @Override
        public void onCreate() {
            Log.d(TAG, "Entering onCreate in RecipeRemotesViewFactory");
            String json = readJsonFromAssets();
            if (json != null) {
                Gson gson = new Gson();
                Recipe[] recipeArray = gson.fromJson(json, Recipe[].class);
                mRecipes = Arrays.asList(recipeArray);
                Log.d(TAG, "Recipes read.. First recipe: " + mRecipes.get(0).getName());



            }

           // mRecipes.add("Nutella Pie");
          //  mRecipes.add("Brownies");
           // mRecipes.add("Yellow Cake");
           // mRecipes.add("Cheesecake");

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

        }

        @Override
        public void onDestroy() {
            Log.d(TAG,"onDestroy invoked " );

        }

        @Override
        public int getCount() {
            Log.d(TAG,"getCount invoked returning: " + Integer.toString(mRecipes.size()));
            return mRecipes.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            Log.d(TAG, "getViewAt invoked for position" + Integer.toString(position));

            RemoteViews remoteView = new RemoteViews(mContext.getPackageName(),
                    R.layout.recipe_row_item);

            remoteView.setTextViewText(R.id.item, mRecipes.get(position).getName());

            // Next, we set a fill-intent which will be used to fill-in the pending intent template
            // which is set on the collection view in StackWidgetProvider.
            Bundle extras = new Bundle();
            extras.putInt(BakeAppWidget.EXTRA_ITEM, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            remoteView.setOnClickFillInIntent(R.id.item, fillInIntent);
            // You can do heaving lifting in here, synchronously. For example, if you need to
            // process an image, fetch something from the network, etc., it is ok to do it here,
            // synchronously. A loading view will show up in lieu of the actual contents in the
            // interim.



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
            return true;
        }
    }
}
