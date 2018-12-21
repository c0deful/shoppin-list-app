package pl.dplewa.shoppinglistapp.activity.shop;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.function.BiConsumer;

import pl.dplewa.shoppinglistapp.R;
import pl.dplewa.shoppinglistapp.data.Shop;
import pl.dplewa.shoppinglistapp.data.ShopDatabase;

/**
 * @author Dominik Plewa
 */
public class ShopTransitionsIntentService extends IntentService {

    private static final String TAG = ShopTransitionsIntentService.class.getSimpleName();
    private static final String CHANNEL_ID = "pl.dplewa.shoppinglistapp";

    private NotificationManager mNotificationManager;
    private ShopDatabase mShopDb;

    public ShopTransitionsIntentService() {
        super(ShopTransitionsIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mShopDb = new ShopDatabase();
        mNotificationManager = getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "Geofencing error with code " + geofencingEvent.getErrorCode());
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition != Geofence.GEOFENCE_TRANSITION_ENTER &&
                geofenceTransition != Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.e(TAG, "Invalid geofence transition type " + geofenceTransition);
            return;
        }

        for (Geofence geofence : geofencingEvent.getTriggeringGeofences()) {
            switch (geofenceTransition) {
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    sendEnterNotification(geofence);
                    break;
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    sendExitNotification(geofence);
                    break;
                default:
                    throw new IllegalStateException("Unhandled geofence transition type");
            }
        }
    }

    private void sendEnterNotification(final Geofence geofence) {
        sendNotification(geofence, R.string.shop_enter_notification_title, R.string.shop_enter_notification_content);
    }


    private void sendExitNotification(final Geofence geofence) {
        sendNotification(geofence, R.string.shop_exit_notification_title, R.string.shop_exit_notification_content);
    }

    private void sendNotification(final Geofence geofence, final int titleResourceId, final int contentResourceId) {
        final String shopId = geofence.getRequestId();
        mShopDb.getShop(shopId)
                .whenComplete(new BiConsumer<Shop, Throwable>() {
                    @Override
                    public void accept(Shop shop, Throwable throwable) {
                        if (throwable != null) {
                            Log.e(TAG, "Error when getting shop from DB", throwable);
                        } else {
                            NotificationCompat.Builder builder =
                                    new NotificationCompat.Builder(ShopTransitionsIntentService.this, CHANNEL_ID)
                                            .setContentTitle(getString(titleResourceId, shop.name))
                                            .setContentText(getString(contentResourceId))
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .setAutoCancel(true);

                            mNotificationManager.notify(geofence.getRequestId().hashCode(), builder.build());
                        }
                    }
                });
    }
}