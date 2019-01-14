package pl.dplewa.shoppinglistapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.Preconditions;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of App Widget functionality.
 */
public class MultimediaWidget extends AppWidgetProvider {

    private static final String TAG = MultimediaWidget.class.getSimpleName();
    private static final String ACTION_IMAGE_NEXT = "pl.dplewa.shoppinglistapp.widget.action.image.next";
    private static final String ACTION_IMAGE_PREV = "pl.dplewa.shoppinglistapp.widget.action.image.prev";
    private static final String OPTION_IMAGE_INDEX = "pl.dplewa.shoppinglistapp.widget.option.image.index";
    private static final List<Integer> IMAGES;

    static {
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.cat1);
        images.add(R.drawable.cat2);
        images.add(R.drawable.cat3);
        IMAGES = Collections.unmodifiableList(images);
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {
        final Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.multimedia_widget_main);

        views.setOnClickPendingIntent(R.id.widget_web_button, getWebViewPendingIntent(context));
        views.setOnClickPendingIntent(R.id.widget_image_prev, getSelfPendingIntent(context, ACTION_IMAGE_PREV, appWidgetId));
        views.setOnClickPendingIntent(R.id.widget_image_next, getSelfPendingIntent(context, ACTION_IMAGE_NEXT, appWidgetId));

        final int imageIndex = options.getInt(OPTION_IMAGE_INDEX, 0);
        views.setImageViewResource(R.id.widget_image, IMAGES.get(imageIndex));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private PendingIntent getWebViewPendingIntent(Context context) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    private PendingIntent getSelfPendingIntent(Context context, String action, int appWidgetId) {
        final Intent intent = new Intent(action);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setComponent(new ComponentName(context, MultimediaWidget.class));
        return PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        final Bundle extras = intent.getExtras();
        if (extras == null || !extras.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
            return;
        }

        final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        final int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
        final String action = intent.getAction();

        if (ACTION_IMAGE_PREV.equals(action)) {
            switchImage(-1, appWidgetManager, appWidgetId);
        }

        if (ACTION_IMAGE_NEXT.equals(action)) {
            switchImage(1, appWidgetManager, appWidgetId);
        }

        updateAppWidget(context, appWidgetManager, appWidgetId);
    }

    private void switchImage(int step, AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int currentIndex = options.getInt(OPTION_IMAGE_INDEX, 0);
        int newIndex = (currentIndex + step + IMAGES.size()) % IMAGES.size();
        options.putInt(OPTION_IMAGE_INDEX, newIndex);
        appWidgetManager.updateAppWidgetOptions(appWidgetId, options);
        Log.d(TAG, "Image index switched from " + currentIndex + " to " + newIndex);
    }
}

