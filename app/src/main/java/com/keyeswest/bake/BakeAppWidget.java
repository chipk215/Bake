package com.keyeswest.bake;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.keyeswest.bake.utilities.RecipeWriter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakeAppWidget extends AppWidgetProvider {

    private static final String TAG="BAKEAPPWIDGET";

    private static final String mSharedPrefFile =
            "com.keyeswest.bake";
    private static final String COUNT_KEY = "count";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences recipeData = context.getSharedPreferences(RecipeWriter.RECIPE_FILENAME,
                0);

        int recipeCount = recipeData.getInt(RecipeWriter.RECIPE_COUNT_KEY, 0);
        List<String> recipeNames = new ArrayList<>();
        for (int i=0; i< recipeCount; i++){
            recipeNames.add(recipeData.getString(Integer.toString(i),""));
            Log.i(TAG, "Recipe: " + Integer.toString(i) + "--> " + recipeNames.get(i));

        }



        SharedPreferences prefs = context.getSharedPreferences(
                mSharedPrefFile, 0);
        int count = prefs.getInt(COUNT_KEY + appWidgetId, 0);
        count++;

        String dateString =
                DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_app_widget);

     //   views.setTextViewText(R.id.appwidget_id,String.valueOf(appWidgetId) );
        views.setTextViewText(R.id.appwidget_id,Integer.toString(recipeCount) );

        views.setTextViewText(R.id.appwidget_update,
                context.getResources().getString(
                        R.string.date_count_format, count, dateString));

        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putInt(COUNT_KEY + appWidgetId, count);
        prefEditor.apply();

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


}

