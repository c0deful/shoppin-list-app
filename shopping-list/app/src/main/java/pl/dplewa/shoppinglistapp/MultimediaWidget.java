package pl.dplewa.shoppinglistapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class MultimediaWidget extends AppWidgetProvider {

    private static final String TAG = MultimediaWidget.class.getSimpleName();
    private static final String EXTRA_STATE = "pl.dplewa.shoppinglistapp.widget.state";

    private static final int STATE_MAIN = 1;

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {
        int currentState = appWidgetManager.getAppWidgetOptions(appWidgetId).getInt(EXTRA_STATE, STATE_MAIN);
        Log.d(TAG, "updateAppWidget: widget id " + appWidgetId + " state " + currentState);
        RemoteViews views;
        switch (currentState) {
            case STATE_MAIN:
                views = new RemoteViews(context.getPackageName(), R.layout.multimedia_widget_main);
                views.setOnClickPendingIntent(R.id.widget_web_button, getWebViewPendingIntent(context));
                break;
            default:
                Log.wtf(TAG, "updateAppWidget: invalid widget state " + currentState);
                return;
        }


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private PendingIntent getWebViewPendingIntent(Context context) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
        return PendingIntent.getActivity(context, 0, intent, 0);
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
        updateAppWidget(context, appWidgetManager, appWidgetId);
    }
}

