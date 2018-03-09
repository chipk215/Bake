package com.keyeswest.bake;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
    public static final String EXTRA_ITEM = "com.keyeswest.bake.BakeAppWidget.EXTRA_ITEM";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setData(Uri.fromParts("content", String.valueOf(appWidgetId), null));

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_app_widget);

        views.setRemoteAdapter( R.id.recipe_list, intent);

        // The empty view is displayed when the collection has no items. It should be a sibling
        // of the collection view.
        views.setEmptyView(R.id.recipe_list, R.id.empty_view);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


}

