package pl.dplewa.shoppinglistapp.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * @author Dominik Plewa
 */
public class NewEntryNotificationService extends Service {

    @Override
    public void onCreate() {
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // SDK >= 26
            String name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            String channelId = getString(R.string.channel_id);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager nm = getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    public void notifyAboutNewEntry(int entryId, String name, String price) {
        Intent editItemIntent = new Intent("pl.dplewa.shoppinglistapp.action.EDIT");
        editItemIntent.putExtra("entryId", entryId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, entryId, editItemIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notif = new NotificationCompat.Builder(this, getString(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("New product on your shopping list")
                .setContentText(name + " " + NumberFormat.getCurrencyInstance().format(new BigDecimal(price)))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        nm.notify(entryId, notif.build());
    }

    class Binder extends android.os.Binder {

        NewEntryNotificationService getService() {
            return NewEntryNotificationService.this;
        }
    }
}
