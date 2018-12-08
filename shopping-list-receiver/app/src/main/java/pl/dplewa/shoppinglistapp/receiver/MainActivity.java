package pl.dplewa.shoppinglistapp.receiver;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

/**
 * @author Dominik Plewa
 */
public class MainActivity extends Activity {

    private NewEntryNotificationService notificationService;
    private ShoppingListNewEntryReceiver receiver;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            notificationService = ((NewEntryNotificationService.Binder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            notificationService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.receiver = new ShoppingListNewEntryReceiver();
        registerReceiver(receiver, new IntentFilter("pl.dplewa.shoppinglistapp.NEW_ENTRY"),
                "pl.dplewa.shoppinglistapp.permissions.NEW_ENTRY_INTENT", null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, NewEntryNotificationService.class), connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        if (notificationService != null) {
            unbindService(connection);
        }
    }
}
