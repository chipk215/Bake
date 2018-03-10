package com.keyeswest.bake.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.keyeswest.bake.R;

import java.util.Hashtable;

/**
 * Implementation of App Widget functionality.
 */
public class BakeAppWidget extends AppWidgetProvider {

    private static final String TAG="BAKEAPPWIDGET";
    private static final int INVALID_INDEX = -1;

    public static final String SELECT_ACTION =
            "com.keyeswest.bake.Widget.BakeAppWidget.SELECT_ACTION";

    public static final String EXTRA_ITEM_POSITION =
            "com.keyeswest.bake.Widget.BakeAppWidget.EXTRA_ITEM_POSITION";

    public static final String EXTRA_ITEM_RECIPE_NAME =
            "com.keyeswest.bake.Widget.BakeAppWidget.EXTRA_ITEM_NAME";


    private static Hashtable<Integer, SelectedRecipe> sSelectedRecipe = new Hashtable<>();


    class SelectedRecipe{
        String recipeName;
        int index;

        SelectedRecipe(){
            recipeName = "";
            index = INVALID_INDEX;
        }

        SelectedRecipe(String name, int index){
            recipeName = name;
            this.index = index;
        }
    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Log.d(TAG, "Entering updateAppWidget");
        RemoteViews views;
        SelectedRecipe selectedRecipe = sSelectedRecipe.get(appWidgetId);

        if (selectedRecipe.index == INVALID_INDEX) {
            // this is the path for displaying recipes

            Intent intent = new Intent(context, RecipeWidgetService.class);
            // scheme -  widgetId:R|I, integer
            // I, integer encodes recipe index to get the corresponding ingredients
            String scheme_specific_part = String.valueOf(appWidgetId) + ":" + "R";
            intent.setData(Uri.fromParts("content", scheme_specific_part, null));

            // Construct the RemoteViews object
            views = new RemoteViews(context.getPackageName(), R.layout.bake_app_widget);

            views.setTextViewText(R.id.recipe_label_tv, context.getResources().getString(R.string.recipes));

            views.setRemoteAdapter(R.id.recipe_list, intent);

            // The empty view is displayed when the collection has no items. It should be a sibling
            // of the collection view.
            views.setEmptyView(R.id.recipe_list, R.id.empty_view);


            // This section makes it possible for items to have individualized behavior.
            // It does this by setting up a pending intent template. Individuals items of a collection
            // cannot set up their own pending intents. Instead, the collection as a whole sets
            // up a pending intent template, and the individual items set a fillInIntent
            // to create unique behavior on an item-by-item basis.
            Intent selectIntent = new Intent(context, BakeAppWidget.class);
            // Set the action for the intent.
            // When the user touches a particular view, it will have the effect of
            // broadcasting SELECT_ACTION.
            selectIntent.setAction(BakeAppWidget.SELECT_ACTION);
            selectIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent selectRecipePendingIntent = PendingIntent.getBroadcast(context, 0, selectIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            views.setPendingIntentTemplate(R.id.recipe_list, selectRecipePendingIntent);

        }else{
            // this is the path for displaying ingredients

            // show the ingredient list corresponding to the recipe
            Intent intent = new Intent(context, RecipeWidgetService.class);

            // scheme -  widgetId:R|I, integer
            // I, integer encodes recipe index to get the corresponding ingredients
            String scheme_specific_part = String.valueOf(appWidgetId) + ":" + "I" + ","
                    + Integer.toString(selectedRecipe.index);

            intent.setData(Uri.fromParts("content", scheme_specific_part, null));

            // Construct the RemoteViews object
            views = new RemoteViews(context.getPackageName(), R.layout.bake_app_widget);

            views.setTextViewText(R.id.recipe_label_tv, context.getResources().getString(R.string.ingredients));

            views.setRemoteAdapter(R.id.recipe_list, intent);

            // The empty view is displayed when the collection has no items. It should be a sibling
            // of the collection view.
            views.setEmptyView(R.id.recipe_list, R.id.empty_view);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            if (! sSelectedRecipe.containsKey(appWidgetId)){
                sSelectedRecipe.put(appWidgetId, new SelectedRecipe() );
            }

            updateAppWidget(context, appWidgetManager, appWidgetId);

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Entering onReceive");
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);

        if (intent.getAction().equals(SELECT_ACTION)) {

            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);


            int selectedRecipeViewIndex = intent.getIntExtra(EXTRA_ITEM_POSITION, INVALID_INDEX);
            String selectedRecipeName = intent.getStringExtra(EXTRA_ITEM_RECIPE_NAME);

            sSelectedRecipe.put(appWidgetId,new SelectedRecipe(selectedRecipeName, selectedRecipeViewIndex) );

            Log.d(TAG, "mSelectedRecipeIndex = " + selectedRecipeViewIndex);
            Log.d(TAG, "mSelectedRecipeName = " + selectedRecipeName);
            updateAppWidget(context, mgr, appWidgetId);

            // temporary reset the recipe index back to -1 until back button implemented
            SelectedRecipe recipe = sSelectedRecipe.get(appWidgetId);
            recipe.index = INVALID_INDEX;
            sSelectedRecipe.put(appWidgetId,recipe);

        }
        super.onReceive(context, intent);
    }

}

