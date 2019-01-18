package pl.dplewa.shoppinglistapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class MultimediaWidget extends AppWidgetProvider {

    private static final String TAG = MultimediaWidget.class.getSimpleName();
    private static final String PREFIX_COMMON = "pl.dplewa.shoppinglistapp.widget.";
    private static final String PREFIX_ACTION_IMAGE = PREFIX_COMMON + "action.image.";
    private static final String PREFIC_ACTION_SOUND = "action.sound.";
    private static final String ACTION_IMAGE_NEXT = PREFIX_ACTION_IMAGE + "next";
    private static final String ACTION_IMAGE_PREV = PREFIX_ACTION_IMAGE + "prev";
    private static final String ACTION_SOUND_PREV = PREFIC_ACTION_SOUND + "prev";
    private static final String ACTION_SOUND_NEXT = PREFIC_ACTION_SOUND + "next";
    private static final String ACTION_SOUND_PAUSE_RESUME = PREFIC_ACTION_SOUND + "pauseresume";
    private static final String ACTION_SOUND_STOP = PREFIC_ACTION_SOUND + "stop";
    private static final String OPTION_IMAGE_INDEX = PREFIX_COMMON + "option.image.index";
    private static final List<Integer> IMAGES;
    private static final List<Integer> SOUNDS;

    static {
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.cat1);
        images.add(R.drawable.cat2);
        images.add(R.drawable.cat3);
        IMAGES = Collections.unmodifiableList(images);

        List<Integer> sounds = new ArrayList<>();
        sounds.add(R.raw.bensound_brazilsamba);
        sounds.add(R.raw.bensound_jazzyfrenchy);
        sounds.add(R.raw.bensound_theduel);
        SOUNDS = Collections.unmodifiableList(sounds);
    }

    private static MediaPlayer currentMediaPlayer;
    private static int soundIndex = 0;

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {
        final Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.multimedia_widget);

        views.setOnClickPendingIntent(R.id.widget_web_button, getWebViewPendingIntent(context));
        views.setOnClickPendingIntent(R.id.widget_image_prev, getSelfPendingIntent(context, ACTION_IMAGE_PREV, appWidgetId));
        views.setOnClickPendingIntent(R.id.widget_image_next, getSelfPendingIntent(context, ACTION_IMAGE_NEXT, appWidgetId));
        views.setOnClickPendingIntent(R.id.widget_sound_prev, getSelfPendingIntent(context, ACTION_SOUND_PREV, appWidgetId));
        views.setOnClickPendingIntent(R.id.widget_sound_next, getSelfPendingIntent(context, ACTION_SOUND_NEXT, appWidgetId));
        views.setOnClickPendingIntent(R.id.widget_sound_pause_resume, getSelfPendingIntent(context, ACTION_SOUND_PAUSE_RESUME, appWidgetId));
        views.setOnClickPendingIntent(R.id.widget_sound_stop, getSelfPendingIntent(context, ACTION_SOUND_STOP, appWidgetId));

        final int imageIndex = options.getInt(OPTION_IMAGE_INDEX, 0);
        views.setImageViewResource(R.id.widget_image, IMAGES.get(imageIndex));

        if (getCurrentMediaPlayer(context).isPlaying()) {
            views.setTextViewText(R.id.widget_sound_pause_resume, context.getString(R.string.widget_sound_pause));
        } else {
            views.setTextViewText(R.id.widget_sound_pause_resume, context.getString(R.string.widget_sound_resume));
        }

        // progress counter for debug purposes
        views.setTextViewText(R.id.widget_sound_progress, currentMediaPlayer.getCurrentPosition() + " / " + currentMediaPlayer.getDuration());

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private synchronized MediaPlayer getCurrentMediaPlayer(Context context) {
        if (currentMediaPlayer == null) {
            currentMediaPlayer = createMediaPlayer(context, soundIndex);
        }
        return currentMediaPlayer;
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
        boolean mustUpdateAll = false;

        if (ACTION_IMAGE_PREV.equals(action)) {
            switchImage(-1, appWidgetManager, appWidgetId);
        }

        if (ACTION_IMAGE_NEXT.equals(action)) {
            switchImage(1, appWidgetManager, appWidgetId);
        }

        if (ACTION_SOUND_PREV.equals(action)) {
            switchMusic(-1, context);
            mustUpdateAll = true;
        }

        if (ACTION_SOUND_NEXT.equals(action)) {
            switchMusic(1, context);
            mustUpdateAll = true;
        }

        if (ACTION_SOUND_PAUSE_RESUME.equals(action)) {
            if (getCurrentMediaPlayer(context).isPlaying()) {
                getCurrentMediaPlayer(context).pause();
            } else {
                getCurrentMediaPlayer(context).start();
            }
            mustUpdateAll = true;
        }

        if (ACTION_SOUND_STOP.equals(action) && getCurrentMediaPlayer(context).isPlaying()) {
            getCurrentMediaPlayer(context).stop();
            getCurrentMediaPlayer(context).release();
            currentMediaPlayer = null;
            soundIndex = 0;
            mustUpdateAll = true;
        }

        if (mustUpdateAll) {
            int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, MultimediaWidget.class));
            for (int id : ids) {
                updateAppWidget(context, appWidgetManager, id);
            }
        } else {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void switchImage(int step, AppWidgetManager appWidgetManager, int appWidgetId) {
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int currentIndex = options.getInt(OPTION_IMAGE_INDEX, 0);
        int newIndex = (currentIndex + step + IMAGES.size()) % IMAGES.size();
        options.putInt(OPTION_IMAGE_INDEX, newIndex);
        appWidgetManager.updateAppWidgetOptions(appWidgetId, options);
        Log.d(TAG, "Image index switched from " + currentIndex + " to " + newIndex);
    }

    private void switchMusic(int step, Context context) {
        boolean isPlaying = getCurrentMediaPlayer(context).isPlaying();
        if (isPlaying) {
            getCurrentMediaPlayer(context).stop();
        }
        getCurrentMediaPlayer(context).release();
        final int currentIndex = soundIndex;
        final int newIndex = (soundIndex + step + SOUNDS.size()) % SOUNDS.size();
        soundIndex = newIndex;
        currentMediaPlayer = createMediaPlayer(context, soundIndex);
        if (isPlaying) {
            currentMediaPlayer.start();
        }
        Log.d(TAG, "Sound index switched from " + currentIndex + " to " + newIndex);

    }

    private MediaPlayer createMediaPlayer(final Context context, final int pos) {
        final MediaPlayer player = MediaPlayer.create(context, SOUNDS.get(pos));
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "Player finished");
                switchMusic(1, context);
                getCurrentMediaPlayer(context).start();
            }
        });
        return player;
    }
}

